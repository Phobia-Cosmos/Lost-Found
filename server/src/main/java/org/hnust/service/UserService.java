package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hnust.constant.MessageConstant;
import org.hnust.context.BaseContext;
import org.hnust.dto.LoginDTO;
import org.hnust.dto.PasswordEditDTO;
import org.hnust.dto.UserDTO;
import org.hnust.dto.UserPageQueryDTO;
import org.hnust.entity.User;
import org.hnust.exception.*;
import org.hnust.file.service.FileStorageService;
import org.hnust.mapper.UserMapper;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.utils.RegexUtils;
import org.hnust.utils.SendMailUtil;
import org.hnust.vo.UserVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hnust.constant.RedisConstants.LOGIN_CODE_KEY;
import static org.hnust.constant.RedisConstants.LOGIN_CODE_TTL;


@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 现阶段用户只能提供Email和Phone来注册
    public void register(LoginDTO loginDTO, int role) {
        String phone = loginDTO.getPhone();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        String username = loginDTO.getUsername();

        // 优化为分开的提示，账号、手机号提示不同的错误
        validateInfo(phone, username, email, password);

        // 校验Email或者phone格式，然后调取三方服务，验证码；这两个必须提供，否则不可以注册
        String code = loginDTO.getCode();

        // 现在从Redis中获取
        String phoneCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        // TODO:这里还没有判断是phone还是email注册，因此我们使用username（前端传的）
        // TODO:要先判断是email还是phone
        String emailCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + username);

        String cachedCode = phoneCode == null ? emailCode : phoneCode;
        log.info("" + cachedCode);
        if (cachedCode == null || !cachedCode.toString().equals(code)) {
            throw new VerifyCodeWrongException(MessageConstant.VERIFY_CODE_ERROR);
        }

        // TODO:可以改为注册完直接登陆，不用在单独登陆
        String salt = UUID.randomUUID().toString();
        String saltedPassword = password + salt;
        String hashedPassword = DigestUtils.md5DigestAsHex(saltedPassword.getBytes());

        User user = BeanUtil.copyProperties(loginDTO, User.class);
        user.setSalt(salt);
        user.setPassword(hashedPassword);
        user.setReputation(0);
        user.setRole(role);
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

        userMapper.register(user);
    }

    // 不可以有管理员删除管理员或者用户，只能由个人注销自己的账户，因此不需要批量删除
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            isUserValid(id);
        }
        userMapper.deleteByIds(ids);
    }

    // 登陆不需要权限区分，因为账号、email和phone都是不相同的，因此每个人的id也不同
    public User login(LoginDTO loginDTO) {
        // TODO:现在的版本是用户登陆不能使用验证码，注册需要验证码
        String username = loginDTO.getUsername();
        String phone = loginDTO.getPhone();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // 注意这里一定要判断传入的值是否为""，否则我们就会查出多个值，因为数据库中存在很多""；或者我们在Mapper中添加条件，确保值不为""
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            user = userMapper.selectByEmail(email);
        }
        if (user == null) {
            user = userMapper.selectByUsername(username);
        }
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        // MD5加密前端密码数据
        String salt = user.getSalt();
        password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        if (!password.equals(user.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 3、返回实体对象
        return user;
    }

    // 用户和管理员都可以修改信息，调用接口不同但是逻辑是一样的，不需要区分Role，该字段不可以更改
    public void update(UserDTO userDTO) {
        // 这里不可能出现不存在的用户，因为我们的Id都是在前端限定了；但是我们还是可以加一层保险
        User byId = userMapper.getById(userDTO.getId());
        if (byId == null) {
            throw new UserIdNotExistsException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        isUserValid(byId.getId());

        String username = userDTO.getUsername();
        String phone = userDTO.getPhone();
        String email = userDTO.getEmail();
        validateInfo(phone, username, email, "password");
        User user = BeanUtil.copyProperties(userDTO, User.class);
        userMapper.update(user);
    }

    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // TODO:注意前端要传递员工ID参数
        Long userId = passwordEditDTO.getUserId();
        User user = userMapper.getById(userId);
        if (userId == null) {
            throw new UserIdNotExistsException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        isUserValid(userId);

        String oldPassword = passwordEditDTO.getOldPassword();
        String newPassword = passwordEditDTO.getNewPassword();

        String salt = user.getSalt();
        String password = user.getPassword();
        if (!DigestUtils.md5DigestAsHex((oldPassword + salt).getBytes()).equals(password)) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        String saltedPassword = newPassword + salt;
        String hashedPassword = DigestUtils.md5DigestAsHex(saltedPassword.getBytes());
        User newUser = User.builder().password(hashedPassword).id(userId).build();

        userMapper.update(newUser);
    }

    // 用户和管理员存在一张表，ID不相同；不论是用户还是管理员，如果查询个人信息，就只能查自己的
    public User getById(Long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // isUserValid(user.getId());

        user.setPassword("****");
        user.setSalt("****");
        return user;
    }

    // 这里只有管理员可以操作，但是管理员不可以点击用户的详细信息；这个方法只暴露给Admin，Admin可以看到所有详细信息，但是不可以查询单个
    // 用户不存在分页查询接口
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<UserVO> page = userMapper.pageQuery(userPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    private static void isUserValid(Long id) {
        if (id != BaseContext.getCurrentUser().getId()) {
            throw new ModificationNotAllowedException(MessageConstant.USER_INVALID);
        }
    }

    private void validateInfo(String phone, String username, String email, String password) {

        if (StrUtil.isBlank(phone) && StrUtil.isBlank(username) && StrUtil.isBlank(email)) {
            throw new InfoNotProvidedException(MessageConstant.INFO_NOT_PROVIDED);
        }

        // 因为用户注册或登录时填写的数据有些可能为空（比如使用email而未使用phone），因此要先判不为空
        if (StrUtil.isNotBlank(phone) && RegexUtils.isPhoneInvalid(phone)) {
            throw new PhoneInvalidException(MessageConstant.PHONE_INVALID);
        }
        User byPhone = userMapper.selectByPhone(phone);
        if (byPhone != null) {
            throw new PhoneUsedException(MessageConstant.PHONE_ALREADY_EXISTS);
        }

        if (StrUtil.isNotBlank(email) && RegexUtils.isEmailInvalid(email)) {
            throw new EmailInvalidException(MessageConstant.EMAIL_INVALID);
        }
        User byEmail = userMapper.selectByEmail(email);
        if (byEmail != null) {
            throw new EmailUsedException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }

        User byUsername = userMapper.selectByUsername(username);
        if (byUsername != null) {
            throw new UsernameUsedException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }

        if (RegexUtils.isPasswordInvalid(password) || (StrUtil.isBlank(password))) {
            throw new PasswordInvalidException(MessageConstant.PASSWORD_INVALID);
        }
    }

    public String uploadPicture(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new ParamInvalidException(MessageConstant.PARAM_INVALID);
        }

        String fileName = java.util.UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }

        // 3.保存到数据库中
        return fileId;
    }

    public Result getCode(String targetEmail) {
        System.out.println(targetEmail);
        if (RegexUtils.isEmailInvalid(targetEmail)) {
            return Result.error("邮箱号格式错误！");
        }
        String codeKey = LOGIN_CODE_KEY + targetEmail;
        String authCode = stringRedisTemplate.opsForValue().get(codeKey);
        // 其实前端只要禁止2min中内发送多次就好了，但是为了防止恶意破坏，还是可以判断
        if (authCode == null) {
            authCode = RandomUtil.randomNumbers(6);
            SendMailUtil.sendEmailCode(targetEmail, "你的验证码为:" + authCode + "(一分钟内有效)");

            stringRedisTemplate.opsForValue().set(codeKey, authCode, LOGIN_CODE_TTL, TimeUnit.MINUTES);

            log.info("验证码为：" + authCode);
            return Result.success("发送邮箱验证码成功！");
        }
        return Result.error("请勿重复发送验证码");
    }
}
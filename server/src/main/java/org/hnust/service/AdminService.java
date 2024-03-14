package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.hnust.constant.MessageConstant;
import org.hnust.dto.LoginDTO;
import org.hnust.dto.PasswordEditDTO;
import org.hnust.dto.UserDTO;
import org.hnust.dto.UserPageQueryDTO;
import org.hnust.entity.User;
import org.hnust.exception.AccountNotFoundException;
import org.hnust.exception.PasswordErrorException;
import org.hnust.mapper.AdminMapper;
import org.hnust.result.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hnust.constant.RoleConstant.ADMIN;


@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public void register(LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        String username = loginDTO.getUsername();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // TODO:优化为分开的提示
        if ((StrUtil.isNotBlank(phone) || StrUtil.isNotBlank(username) || StrUtil.isNotBlank(email)) && StrUtil.isNotBlank(password)) {
            User byUsername = adminMapper.selectByUsername(username);
            User byPhone = adminMapper.selectByPhone(phone);
            User byEmail = adminMapper.selectByEmail(email);
            if ((ObjectUtil.isNotNull(byUsername) || ObjectUtil.isNotNull(byPhone) || ObjectUtil.isNotNull(byEmail))) {
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }

            // Generate a random UUID as the salt
            String salt = UUID.randomUUID().toString();
            // Combine salt and password
            String saltedPassword = password + salt;
            String hashedPassword = DigestUtils.md5DigestAsHex(saltedPassword.getBytes());

            User user = BeanUtil.copyProperties(loginDTO, User.class);
            user.setSalt(salt);
            user.setPassword(hashedPassword);
            user.setReputation(0);
            user.setRole(ADMIN);
            user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

            adminMapper.register(user);
        }
    }

    // TODO：可能会存在危险，删除仅需要根据ID暂时
    public void deleteByIds(List<Long> ids) {
        adminMapper.deleteByIds(ids);
    }

    // 登陆不需要，因为账号、email和phone都是不相同的，因此每个人的id也不同
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String phone = loginDTO.getPhone();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        // 1、根据用户名、手机号或者email查询数据库中的数据
        User user = adminMapper.selectByCriteria(username, phone, email);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            // 账号不存在
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
        User user = BeanUtil.copyProperties(userDTO, User.class);
        adminMapper.update(user);
    }

    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // TODO:注意前端要传递员工ID参数
        Long userId  = passwordEditDTO.getUserId();
        User user = adminMapper.getById(userId);

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

        adminMapper.update(newUser);
    }

    // 用户和管理员存在一张表，ID不相同
    public User getById(Long id) {
        User user = adminMapper.getById(id);
        user.setPassword("****");
        user.setSalt("****");
        return user;
    }

    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<User> page = adminMapper.pageQuery(userPageQueryDTO);
        for (User user : page) {
            user.setPassword("***");
            user.setSalt("****");
        }
        return new PageResult(page.getTotal(), page.getResult());
    }
}
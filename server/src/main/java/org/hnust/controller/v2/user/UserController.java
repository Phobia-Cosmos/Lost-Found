package org.hnust.controller.v2.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hnust.dto.LoginDTO;
import org.hnust.dto.PasswordEditDTO;
import org.hnust.dto.UserDTO;
import org.hnust.entity.User;
import org.hnust.properties.JwtProperties;
import org.hnust.result.Result;
import org.hnust.service.UserService;
import org.hnust.utils.JwtUtil;
import org.hnust.vo.LoginVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hnust.constant.RedisConstants.LOGIN_USER_KEY;
import static org.hnust.constant.RedisConstants.LOGIN_USER_TTL;
import static org.hnust.constant.RoleConstant.USER;

@RestController("UserControllerV2")
@Slf4j
@Api(tags = "用户端信息相关接口")
@RequestMapping("/user/v2/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // TODO:这个也是Admin用户权限，用户只能删除自己的数据
    // TODO：要加入一个字段，可以根据Username查询
    @DeleteMapping
    @ApiOperation("批量删除用户")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除用户：{}", ids);
        userService.deleteByIds(ids);
        return Result.success("批量删除了" + ids + "用户");
    }

    // TODO:前端判断密码不能相同
    // TODO:还要加一个直接修改忘记密码的接口
    @PutMapping("editPassword")
    @ApiOperation("修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改{}号用户密码...", passwordEditDTO.getUserId());
        userService.editPassword(passwordEditDTO);
        return Result.success("修改密码成功");
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询用户")
    public Result<User> getById(@PathVariable Long id) {
        log.info("查询{}号用户...", id);
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO);

        User user = userService.login(loginDTO);
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        claims.put(jwtProperties.getUserTokenName(), userDTO);
        // TODO:每一次产生的token均不同，如果用户非正常退出，我们的token就不处理吗？等待他过期？
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        LoginVO adminLoginVO = LoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .role(USER)
                .token(token)
                .build();

        Map<String, Object> userMap = BeanUtil.beanToMap(adminLoginVO, new HashMap<>(), CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName, fieldValue) -> {
                    if (fieldName != null && fieldName.equals("token"))
                        return null;
                    return fieldValue != null ? fieldValue.toString() : null;
                }));

        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.SECONDS);

        return Result.success(adminLoginVO, "用户登录成功");
    }

    @PostMapping("/getCode")
    @ApiOperation("发送邮箱验证码")
    public ResponseEntity<Result> mail(@RequestBody Map<String, String> requestBody) {
        String targetEmail = requestBody.get("targetEmail");
        log.info("用户请求发送邮箱验证码," + targetEmail);
        Result result = userService.getCode(targetEmail);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    @ApiOperation("新增用户")
    public Result register(@RequestBody LoginDTO loginDTO) {
        log.info("新增用户: {}", loginDTO);
        userService.register(loginDTO, USER);
        return Result.success("账号注册成功！");
    }

    // 注意：更新一定要传递特定的ID，如果我们不指定ID，这表中所有数据一起被更新
    @PutMapping
    @ApiOperation("编辑用户信息")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("编辑用户信息, {}", userDTO);
        userService.update(userDTO);
        return Result.success("修改信息成功");
    }

    @PostMapping("/upload")
    @ApiOperation("上传照片")
    public Result upload(@ApiParam(value = "用户图像图片", required = true) @RequestPart("file") MultipartFile multipartFile) {
        log.info("用户上传了{}照片", multipartFile.getOriginalFilename());
        String pictureURL = userService.uploadPicture(multipartFile);
        return Result.success(pictureURL, "上传照片成功！");
    }
}

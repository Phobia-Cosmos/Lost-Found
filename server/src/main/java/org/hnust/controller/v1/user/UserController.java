package org.hnust.controller.v1.user;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.dto.LoginDTO;
import org.hnust.dto.PasswordEditDTO;
import org.hnust.dto.UserDTO;
import org.hnust.dto.UserPageQueryDTO;
import org.hnust.entity.User;
import org.hnust.properties.JwtProperties;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.UserService;
import org.hnust.utils.JwtUtil;
import org.hnust.vo.LoginVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hnust.constant.RoleConstant.USER;

@RestController
@Slf4j
@Api(tags = "用户端信息相关接口")
@RequestMapping("/user/v1/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    @ApiOperation("新增用户")
    public Result register(@RequestBody LoginDTO loginDTO) {
        log.info("新增用户: {}", loginDTO);
        userService.register(loginDTO, USER);
        return Result.success("账号注册成功！");
    }

    // TODO:这个也是Admin用户权限，用户只能删除自己的数据
    @DeleteMapping
    @ApiOperation("批量删除用户")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除用户：{}", ids);
        userService.deleteByIds(ids);
        return Result.success("批量删除了" + ids + "用户");
    }

    // 注意：更新一定要传递特定的ID，如果我们不指定ID，这表中所有数据一起被更新
    @PutMapping
    @ApiOperation("编辑用户信息")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("编辑用户信息, {}", userDTO);
        userService.update(userDTO);
        return Result.success("修改信息成功");
    }

    // TODO:前端判断密码不能相同
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

    // TODO：这个应该是Admin的权限吧，要修改！
    // TODO：这里不可以使用User作为返回信息，我们不能将数据库字段暴露！要使用VO
    @GetMapping("/page")
    @ApiOperation("用户分页查询")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO) {
        log.info("用户分页查询，参数为: {}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO);
        User user = userService.login(loginDTO);

        // JwtUtil jwtUtil = new JwtUtil(jwtProperties);
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        // claims.put(JwtClaimsConstant.USER_ID, user.getId());
        // claims.put("Role", USER);
        claims.put("user", userDTO);
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        LoginVO adminLoginVO = LoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .reputation(user.getReputation())
                .school(user.getSchool())
                .token(token)
                .build();

        return Result.success(adminLoginVO, "用户登录成功");
    }

//     下方是测试接口参数与注解关系的注解， 来自于V2

    // @PostMapping("/register1")
    // @ApiOperation("新增用户1")
    // public Result register1(String username, String password, String phone, String email) {
    //     LoginDTO loginDTO = new LoginDTO(username, password, phone, email);
    //     log.info("新增用户: {}", loginDTO);
    //     userService.register(loginDTO, USER);
    //     return Result.success("账号注册成功！");
    // }
    //
    // @PostMapping("/register2/{username}/{password}/{phone}/{email}")
    // @ApiOperation("新增用户2")
    // public Result register2(@PathVariable String username, @PathVariable String password, @PathVariable String phone, @PathVariable String email) {
    //     LoginDTO loginDTO = new LoginDTO(username, password, phone, email);
    //     log.info("新增用户: {}", loginDTO);
    //     userService.register(loginDTO, USER);
    //     return Result.success("账号注册成功！");
    // }

    // @PutMapping("editPassword1")
    // @ApiOperation("修改密码1")
    // public Result editPassword1(@RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword, @RequestParam("userId") Long userId) {
    //     log.info("{}", userId);
    //     PasswordEditDTO passwordEditDTO = new PasswordEditDTO(userId, oldPassword, newPassword);
    //     log.info("修改{}号用户密码...", passwordEditDTO.getUserId());
    //     userService.editPassword(passwordEditDTO);
    //     return Result.success("修改密码成功");
    // }

    // @GetMapping("/page1")
    // @ApiOperation("用户分页查询1")
    // public Result<PageResult> page1(String name, String phone, String email, String school,
    //                                 int page, int pageSize) {
    //     UserPageQueryDTO userPageQueryDTO = new UserPageQueryDTO(name, phone,
    //             email, school, page, pageSize);
    //     log.info("用户分页查询，参数为: {}", userPageQueryDTO);
    //     PageResult pageResult = userService.pageQuery(userPageQueryDTO);
    //     return Result.success(pageResult);
    // }
}

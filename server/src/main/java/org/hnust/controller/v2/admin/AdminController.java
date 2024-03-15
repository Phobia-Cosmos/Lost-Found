package org.hnust.controller.v2.admin;

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
import org.hnust.service.AdminService;
import org.hnust.utils.JwtUtil;
import org.hnust.vo.LoginVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController("AdminControllerV2")
@RequestMapping("/admin/v2/user")
@Api(tags = "管理端信息相关接口")
public class AdminController {

    @Resource
    private AdminService adminService;
    @Resource
    private JwtProperties jwtProperties;

    @PostMapping
    @ApiOperation("新增管理员")
    public Result register(@RequestBody LoginDTO loginDTO) {
        log.info("新增管理员: {}", loginDTO);
        adminService.register(loginDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除管理员")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除管理员：{}", ids);
        adminService.deleteByIds(ids);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("编辑管理员信息")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("编辑管理员信息, {}", userDTO);
        adminService.update(userDTO);
        return Result.success();
    }

    @PutMapping("editPassword")
    @ApiOperation("修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改{}号管理员密码...", passwordEditDTO.getUserId());
        adminService.editPassword(passwordEditDTO);
        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询管理员")
    public Result<User> getById(@PathVariable Long id) {
        log.info("查询{}号管理员...", id);
        User user = adminService.getById(id);
        return Result.success(user);
    }

    @GetMapping("/page")
    @ApiOperation("管理员分页查询")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO) {
        log.info("管理员分页查询，参数为: {}", userPageQueryDTO);
        PageResult pageResult = adminService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/login")
    @ApiOperation("管理员登陆")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("管理员登录：{}", loginDTO);
        User user = adminService.login(loginDTO);

        // JwtUtil jwtUtil = new JwtUtil(jwtProperties);
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        // 把用户ID存ThreadLocal中，后续不用查询
        // claims.put(JwtClaimsConstant.ADMIN_ID, user.getId());
        // claims.put("Role", ADMIN);
        claims.put("user", userDTO);
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

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

        return Result.success(adminLoginVO);
    }
}

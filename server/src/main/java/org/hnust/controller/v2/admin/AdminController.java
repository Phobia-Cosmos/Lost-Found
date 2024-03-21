package org.hnust.controller.v2.admin;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hnust.constant.RoleConstant.ADMIN;

@Slf4j
@RestController("AdminControllerV2")
@RequestMapping("/admin/v2/user")
@Api(tags = "管理端信息相关接口")
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    @ApiOperation("新增管理员")
    public Result register(@RequestBody LoginDTO loginDTO) {
        log.info("新增管理员: {}", loginDTO);
        userService.register(loginDTO, ADMIN);
        return Result.success("账号注册成功！");
    }

    @PostMapping("/login")
    @ApiOperation("管理员登陆")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("管理员登录：{}", loginDTO);
        User user = userService.login(loginDTO);

        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        claims.put(jwtProperties.getAdminTokenName(), userDTO);
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
                .role(ADMIN)
                .token(token)
                .build();

        return Result.success(adminLoginVO, "管理员登录成功");
    }

    @PostMapping("/upload")
    @ApiOperation("上传照片")
    public Result upload(MultipartFile file) {
        log.info("照片信息{}", file);
        // TODO yonghuxinxicundaoredis
        log.info("用户上传了{}照片", file.getOriginalFilename());
        String pictureURL = userService.uploadPicture(file);
        return Result.success(pictureURL, "上传照片成功！");
    }

    @DeleteMapping
    @ApiOperation("批量删除管理员")
    // 不可以有管理员删除管理员或者用户，只能由个人注销自己的账户，因此不需要批量删除
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除管理员：{}", ids);
        userService.deleteByIds(ids);
        return Result.success("批量删除了" + ids + "管理员");
    }

    @PutMapping
    @ApiOperation("编辑管理员信息")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("编辑管理员信息, {}", userDTO);
        userService.update(userDTO);
        return Result.success("修改信息成功");
    }

    @PutMapping("editPassword")
    @ApiOperation("修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改{}号管理员密码...", passwordEditDTO.getUserId());
        userService.editPassword(passwordEditDTO);
        return Result.success("修改密码成功");
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询管理员")
    public Result<User> getById(@PathVariable Long id) {
        log.info("查询{}号管理员...", id);
        User user = userService.getById(id);
        return Result.success(user);
    }

    @GetMapping("/page")
    @ApiOperation("管理员分页查询")
    // TODO：这里不可以使用User作为返回信息，我们不能将数据库字段暴露！要使用VO
    // 注意：前端一定要限制显示，否则管理员就可以操作用户数据了
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO) {
        userPageQueryDTO.setRole(ADMIN);
        log.info("管理员分页查询，参数为: {}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }
}

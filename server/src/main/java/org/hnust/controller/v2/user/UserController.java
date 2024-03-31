package org.hnust.controller.v2.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.system.UserInfo;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.PageFans;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.PageFansBean;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.javabeans.weibo.Company;
import com.qq.connect.oauth.Oauth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
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

    // 需要等到备案通过才可以使用

    // private static String returnUrl = "";
    //
    // @GetMapping("/qqlogin")
    // @ApiOperation("QQ登陆")
    // public void qqLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     returnUrl = request.getParameter("returnUrl");
    //     response.setContentType("text/html;charset=utf-8");
    //     try {
    //         response.sendRedirect(new Oauth().getAuthorizeURL(request));// 将页面重定向到qq第三方的登录页面
    //         log.info("QQ");
    //     } catch (QQConnectException e) {
    //         log.warn("请求QQ登录失败, {}", e.getMessage());
    //     }
    // }

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

    @PostMapping("/upload")
    @ApiOperation("上传照片")
    public Result upload(@ApiParam(value = "用户图像图片", required = true) @RequestPart("file") MultipartFile multipartFile) {
        log.info("{}用户上传了{}照片", BaseContext.getCurrentUser().getUsername(), multipartFile.getOriginalFilename());
        String pictureURL = userService.uploadPicture(multipartFile);
        return Result.success(pictureURL, "上传照片成功！");
    }

    // TODO:这个也是Admin用户权限，用户只能删除自己的数据
    // TODO：要加入一个字段，可以根据Username查询
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

    // // 获取登录者的基础信息
    // @RequestMapping("/afterLogin")
    // public void QQAfterlogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     System.out.println("AfterLogin=======================================================");
    //     response.setContentType("text/html; charset=utf-8");  // 响应编码
    //     PrintWriter out = response.getWriter();
    //
    //     Enumeration<String> parameterNames = request.getParameterNames();
    //     while (parameterNames.hasMoreElements()) {
    //         String parameterName = parameterNames.nextElement();// code
    //         System.out.println(parameterName + ":" + request.getParameter(parameterName));// state
    //     }
    //     System.out.println("qq_connect_state:" + request.getSession().getAttribute("qq_connect_state"));
    //
    //     try {
    //         // 获取AccessToken(AccessToken用于获取OppendID)
    //         AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
    //
    //         System.out.println("accessTokenObj:" + accessTokenObj);
    //         // 用于接收AccessToken
    //         String accessToken = null,
    //                 openID = null;
    //         long tokenExpireIn = 0L; // AccessToken有效时长
    //
    //         if (accessTokenObj.getAccessToken().equals("")) {
    //             //                我们的网站被CSRF攻击了或者用户取消了授权
    //             //                做一些数据统计工作
    //             System.out.print("没有获取到响应参数");
    //         } else {
    //             accessToken = accessTokenObj.getAccessToken();  // 获取AccessToken
    //             tokenExpireIn = accessTokenObj.getExpireIn();
    //
    //             request.getSession().setAttribute("demo_access_token", accessToken);
    //             request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));
    //
    //             // 利用获取到的accessToken 去获取当前用的openid -------- start
    //             OpenID openIDObj = new OpenID(accessToken);
    //             // 通过对象获取[OpendId]（OpendID用于获取QQ登录用户的信息）
    //             openID = openIDObj.getUserOpenID();
    //
    //             out.println("欢迎你，代号为 " + openID + " 的用户!");
    //             request.getSession().setAttribute("demo_openid", openID);
    //             out.println("<a href=" + "/shuoshuoDemo.html" + " target=\"_blank\">去看看发表说说的demo吧</a>");
    //             // 利用获取到的accessToken 去获取当前用户的openid --------- end
    //
    //             out.println("<p> start -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- start </p>");
    //             // 通过OpenID获取QQ用户登录信息对象(Oppen_ID代表着QQ用户的唯一标识)
    //             UserInfo qzoneUserInfo = new UserInfo();
    //             // 获取用户信息对象(只获取nickename与Gender)
    //             UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
    //             out.println("<br/>");
    //             if (userInfoBean.getRet() == 0) {
    //                 out.println(userInfoBean.getNickname() + "<br/>");
    //                 out.println(userInfoBean.getGender() + "<br/>");
    //                 out.println("黄钻等级： " + userInfoBean.getLevel() + "<br/>");
    //                 out.println("会员 : " + userInfoBean.isVip() + "<br/>");
    //                 out.println("黄钻会员： " + userInfoBean.isYellowYearVip() + "<br/>");
    //                 out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
    //                 out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
    //                 out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
    //             } else {
    //                 out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
    //             }
    //             out.println("<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- end </p>");
    //
    //
    //             out.println("<p> start ----------------------------------- 验证当前用户是否为认证空间的粉丝------------------------------------------------ start <p>");
    //             PageFans pageFansObj = new PageFans(accessToken, openID);
    //             PageFansBean pageFansBean = pageFansObj.checkPageFans("97700000");
    //             if (pageFansBean.getRet() == 0) {
    //                 out.println("<p>验证您" + (pageFansBean.isFans() ? "是" : "不是") + "QQ空间97700000官方认证空间的粉丝</p>");
    //             } else {
    //                 out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + pageFansBean.getMsg());
    //             }
    //             out.println("<p> end ----------------------------------- 验证当前用户是否为认证空间的粉丝------------------------------------------------ end <p>");
    //
    //
    //             out.println("<p> start -----------------------------------利用获取到的accessToken,openid 去获取用户在微博的昵称等信息 ---------------------------- start </p>");
    //             com.qq.connect.api.weibo.UserInfo weiboUserInfo = new com.qq.connect.api.weibo.UserInfo(accessToken, openID);
    //             com.qq.connect.javabeans.weibo.UserInfoBean weiboUserInfoBean = weiboUserInfo.getUserInfo();
    //             if (weiboUserInfoBean.getRet() == 0) {
    //                 // 获取用户的微博头像----------------------start
    //                 out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
    //                 out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
    //                 out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
    //                 // 获取用户的微博头像 ---------------------end
    //
    //                 // 获取用户的生日信息 --------------------start
    //                 out.println("<p>尊敬的用户，你的生日是： " + weiboUserInfoBean.getBirthday().getYear()
    //                         + "年" + weiboUserInfoBean.getBirthday().getMonth() + "月" +
    //                         weiboUserInfoBean.getBirthday().getDay() + "日");
    //                 // 获取用户的生日信息 --------------------end
    //
    //                 StringBuffer sb = new StringBuffer();
    //                 sb.append("<p>所在地:" + weiboUserInfoBean.getCountryCode() + "-" + weiboUserInfoBean.getProvinceCode() + "-" + weiboUserInfoBean.getCityCode()
    //                         + weiboUserInfoBean.getLocation());
    //
    //                 // 获取用户的公司信息---------------------------start
    //                 ArrayList<Company> companies = weiboUserInfoBean.getCompanies();
    //                 if (companies.size() > 0) {
    //                     // 有公司信息
    //                     for (int i = 0, j = companies.size(); i < j; i++) {
    //                         sb.append("<p>曾服役过的公司：公司ID-" + companies.get(i).getID() + " 名称-" +
    //                                 companies.get(i).getCompanyName() + " 部门名称-" + companies.get(i).getDepartmentName() + " 开始工作年-" +
    //                                 companies.get(i).getBeginYear() + " 结束工作年-" + companies.get(i).getEndYear());
    //                     }
    //                 } else {
    //                     // 没有公司信息
    //                 }
    //                 // 获取用户的公司信息---------------------------end
    //                 out.println(sb.toString());
    //             } else {
    //                 out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + weiboUserInfoBean.getMsg());
    //             }
    //             out.println("<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在微博的昵称等信息 ---------------------------- end </p>");
    //         }
    //     } catch (QQConnectException e) {
    //     }
    // }
}

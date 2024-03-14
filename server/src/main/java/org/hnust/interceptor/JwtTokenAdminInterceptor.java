package org.hnust.interceptor;

import org.hnust.constant.JwtClaimsConstant;
import org.hnust.context.BaseContext;
import org.hnust.dto.UserDTO;
import org.hnust.properties.JwtProperties;
import org.hnust.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO：HandlerInterceptor？这个什么时候发挥作用？除了处理JWT，还可以处理哪些内容？
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        // JwtUtil jwtUtil = new JwtUtil(jwtProperties);
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            // Long empId = Long.valueOf(claims.get(JwtClaimsConstant.ADMIN_ID).toString());
            UserDTO user =(UserDTO) claims.get("user");
            log.info("当前用户：{}", user);
            BaseContext.setCurrentUser(user);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}

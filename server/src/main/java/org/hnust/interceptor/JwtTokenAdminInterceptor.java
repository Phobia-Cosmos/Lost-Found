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
import java.util.Map;

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
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            // Long userID = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("Jwt Claims: {}", claims);
            // 这里要小心Null Pointer
            Object user = claims.get("user");

            // TODO:这里获取Jwt中对象数据有哪些方法？
            UserDTO userDTO = new UserDTO();
            if (user instanceof Map) {
                Map<String, Object> userMap = (Map<String, Object>) user;
                // TODO：Long型数字从Object推断时，会优先选择Integer？
                userDTO.setId(((Number) userMap.get("id")).longValue());
                userDTO.setUsername((String) userMap.get("username"));
                userDTO.setName((String) userMap.get("name"));
                userDTO.setPhone((String) userMap.get("phone"));
                userDTO.setEmail((String) userMap.get("email"));
                userDTO.setAvatar((String) userMap.get("avatar"));
                userDTO.setSchool((String) userMap.get("school"));
            }

            BaseContext.setCurrentUser(userDTO);
            log.info("当前用户信息：{}", BaseContext.getCurrentUser());
            return true;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}

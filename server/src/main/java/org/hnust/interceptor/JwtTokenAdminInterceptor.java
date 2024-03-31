package org.hnust.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.UserDTO;
import org.hnust.properties.JwtProperties;
import org.hnust.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        System.out.println(request.getHeaderNames());
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            System.out.println(claims);
            Object user = claims.get(jwtProperties.getAdminTokenName());

            // 如果这个数据存在前端传递过来，我们想要去除数据也很难，要反序列化Json
            UserDTO userDTO = new UserDTO();
            if (user instanceof Map) {
                Map<String, Object> userMap = (Map<String, Object>) user;
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

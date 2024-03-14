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
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    // 注意：这里只要是我们以前测试的token都可以使用，比如我们以前使用过期时间很长的token，则这里会一直存留不会过期；
    // TODO：为什么我在JSON Web Token修改了我的token中的数据部分（这个token已经时不一样的了），为什么我还可以通过token校验？
    // 上方使用修改的token去校验后，得到的claims就是我们修改后的数据。所以说只要我们的claims数据不同，token就会一定不同。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // TODO:如何在测试方法中获取当前的Jwt，我想在启动后查看当前的Jwt（我以及启动了我的Springboot项目），Test类如何写？
        // JwtUtil jwtUtil = new JwtUtil(jwtProperties);
        String token = request.getHeader(jwtProperties.getUserTokenName());

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            // Long userID = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("Jwt Claims: {}", claims);
            // 这里要小心Null Pointer
            Object user = claims.get("user");

            // TODO:这里获取Jwt中对象数据有哪些方法？
            // UserDTO userDTO = new UserDTO();
            // if (user instanceof Map) {
            //     Map<String, Object> userMap = (Map<String, Object>) user;
            //     try {
            //         BeanUtil.populate(userDTO, userMap);
            //     } catch (IllegalAccessException | InvocationTargetException e) {
            //         // Handle exception
            //     }
            // }
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
            return true;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}

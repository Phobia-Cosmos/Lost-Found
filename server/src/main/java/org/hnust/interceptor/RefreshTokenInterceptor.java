package org.hnust.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hnust.constant.RedisConstants.LOGIN_USER_KEY;
import static org.hnust.constant.RedisConstants.LOGIN_USER_TTL;

@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    // 要注意这个对象并不是由Spring管理的，要手动注入
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从Session中获取
        // HttpSession session = request.getSession();
        // Object user = session.getAttribute("user");

        // TODO:将这个Key设为常数
        String header = request.getHeader("authentication");
        if (StrUtil.isBlank(header)) {
            return true;
        }

        String key = LOGIN_USER_KEY + header;
        // 从Redis中获取
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            return true;
        }

        UserDTO userDTO = BeanUtil.fillBeanWithMap(entries, new UserDTO(), false);
        // if (user == null) {
        //     response.setStatus(401);
        //     return false;
        // }
        // UserDTO userDTO = new UserDTO();
        // BeanUtil.copyProperties(user, userDTO);
        BaseContext.setCurrentUser(userDTO);

        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    // 一个请求被handler和controller成功处理后该方法将就会被调用
    // 为什么要每一个请求都重新删除User然后重新存入？为什么不在logout删除？
    // 用户的登陆信息只和redis有关，redis过期用户登陆就过期，不管你是否logout；所以不可以在logout时清空，用户可能一直不logout；redis的信息是什么，用户信息就是什么
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContext.removeCurrentUser();
    }
}

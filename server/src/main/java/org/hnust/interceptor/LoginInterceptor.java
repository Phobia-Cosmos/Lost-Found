package org.hnust.interceptor;

import org.hnust.context.BaseContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        System.out.println("here");

        if (BaseContext.getCurrentUser() == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}

package org.hnust.handler;

import org.hnust.constant.MessageConstant;
import org.hnust.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.hnust.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

// 这个注解是干什么的？以及该类中的注解都是？handler的处理逻辑是什么？
// 如果我们使用的是RestController，则我们使用RestControllerAdvice，但是一般开发都是Restful，因此使用这个来捕获所有Controller中出现的异常
// 我们可以将所有可能出现的异常都继承我们自己定义的异常类，统一在一个方法内捕获并处理

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}

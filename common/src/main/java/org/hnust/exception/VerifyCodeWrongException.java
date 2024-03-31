package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class VerifyCodeWrongException extends BaseException {

    public VerifyCodeWrongException() {
    }

    public VerifyCodeWrongException(String msg) {
        super(msg);
    }

}

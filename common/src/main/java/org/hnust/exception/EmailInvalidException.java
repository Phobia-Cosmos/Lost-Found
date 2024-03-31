package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class EmailInvalidException extends BaseException {

    public EmailInvalidException() {
    }

    public EmailInvalidException(String msg) {
        super(msg);
    }

}

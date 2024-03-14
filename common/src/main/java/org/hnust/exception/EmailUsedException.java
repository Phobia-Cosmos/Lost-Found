package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class EmailUsedException extends BaseException {

    public EmailUsedException() {
    }

    public EmailUsedException(String msg) {
        super(msg);
    }

}

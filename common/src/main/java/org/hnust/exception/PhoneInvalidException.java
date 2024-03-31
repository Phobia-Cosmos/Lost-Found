package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class PhoneInvalidException extends BaseException {

    public PhoneInvalidException() {
    }

    public PhoneInvalidException(String msg) {
        super(msg);
    }

}

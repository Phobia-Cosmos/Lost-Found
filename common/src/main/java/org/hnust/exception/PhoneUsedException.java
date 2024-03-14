package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class PhoneUsedException extends BaseException {

    public PhoneUsedException() {
    }

    public PhoneUsedException(String msg) {
        super(msg);
    }

}

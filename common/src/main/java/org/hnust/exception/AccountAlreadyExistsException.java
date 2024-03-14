package org.hnust.exception;

/**
 * 注册账号已存在异常
 */
public class AccountAlreadyExistsException extends BaseException {

    public AccountAlreadyExistsException() {
    }

    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }

}

package org.hnust.exception;

/**
 * 注册用户名已存在异常
 */
public class UsernameUsedException extends BaseException {

    public UsernameUsedException() {
    }

    public UsernameUsedException(String msg) {
        super(msg);
    }

}

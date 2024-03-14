package org.hnust.exception;

/**
 * 账号id不存在异常
 */
public class UserIdNotExistsException extends BaseException {

    public UserIdNotExistsException() {
    }

    public UserIdNotExistsException(String msg) {
        super(msg);
    }

}

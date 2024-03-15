package org.hnust.exception;

/**
 * 注册账号已存在异常
 */
public class InfoNotProvidedException extends BaseException {

    public InfoNotProvidedException() {
    }

    public InfoNotProvidedException(String msg) {
        super(msg);
    }

}

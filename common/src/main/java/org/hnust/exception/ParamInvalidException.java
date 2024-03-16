package org.hnust.exception;

/**
 * 注册手机号已存在异常
 */
public class ParamInvalidException extends BaseException {

    public ParamInvalidException() {
    }

    public ParamInvalidException(String msg) {
        super(msg);
    }

}

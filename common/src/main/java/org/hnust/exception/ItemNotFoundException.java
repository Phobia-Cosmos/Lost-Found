package org.hnust.exception;

/**
 * 建议不存在异常
 */
public class ItemNotFoundException extends BaseException {

    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String msg) {
        super(msg);
    }

}

package org.hnust.exception;

/**
 * 建议不存在异常
 */
public class MessageNotFoundException extends BaseException {

    public MessageNotFoundException() {
    }

    public MessageNotFoundException(String msg) {
        super(msg);
    }

}

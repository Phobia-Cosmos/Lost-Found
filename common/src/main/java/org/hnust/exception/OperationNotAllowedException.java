package org.hnust.exception;

/**
 * 建议不存在异常
 */
public class OperationNotAllowedException extends BaseException {

    public OperationNotAllowedException() {
    }

    public OperationNotAllowedException(String msg) {
        super(msg);
    }

}

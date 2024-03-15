package org.hnust.exception;

/**
 * 不允许修删除异常
 */
public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException() {
    }

    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}

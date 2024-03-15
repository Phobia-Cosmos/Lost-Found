package org.hnust.exception;

/**
 * 不允许修改异常
 */
public class ModificationNotAllowedException extends BaseException {

    public ModificationNotAllowedException() {
    }

    public ModificationNotAllowedException(String msg) {
        super(msg);
    }

}

package org.hnust.exception;

/**
 * 投票不存在异常
 */
public class VoteNotFoundException extends BaseException {

    public VoteNotFoundException() {
    }

    public VoteNotFoundException(String msg) {
        super(msg);
    }

}

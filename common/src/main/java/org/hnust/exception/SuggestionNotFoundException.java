package org.hnust.exception;

/**
 * 建议不存在异常
 */
public class SuggestionNotFoundException extends BaseException {

    public SuggestionNotFoundException() {
    }

    public SuggestionNotFoundException(String msg) {
        super(msg);
    }

}

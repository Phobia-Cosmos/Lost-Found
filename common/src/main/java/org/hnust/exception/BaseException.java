package org.hnust.exception;

//TODO：为什么要定义此基类？为什么要继承RuntimeException？
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}

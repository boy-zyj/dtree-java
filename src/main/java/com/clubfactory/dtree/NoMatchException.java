package com.clubfactory.dtree;

/**
 * 该异常表示没有找到对应的触发动作(AbstractRunner)
 *
 * @author yao
 * @date 2019/03/19
 */
public class NoMatchException extends RuntimeException {

    public NoMatchException() {
        super();
    }

    public NoMatchException(String message) {
        super(message);
    }

    public NoMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMatchException(Throwable cause) {
        super(cause);
    }

}

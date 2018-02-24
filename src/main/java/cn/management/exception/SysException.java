package cn.management.exception;

/**
 * 自定义异常类，封装自定义异常信息
 * @author ZhouJiaKai
 * @date 2018-02-24
 */
public class SysException extends Exception {

    private String message;

    public SysException(String message) {
        this.message  = message;
    }

    public String getMessage() {
        return message;
    }

}

package cn.management.enums;

/**
 * Result返回结果枚举类
 * @author ZhouJiaKai
 * @since  2018/02/23
 */
public enum ResultEnum {

    SUCCESS(1000, "操作成功."),
    DATA_ERROR(1001, "数据格式错误"),
    FAIL(1002, "操作失败.请重试"),
    NO_AUTHORITY(1003,"用户无访问权限."),
    UNKONW_ERROR(1004, "系统异常."),
    NO_RECORDS(1005, "无符合数据."),
    NO_LOGIN(1006, "请先登录.");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

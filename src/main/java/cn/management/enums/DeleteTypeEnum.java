package cn.management.enums;

/**
 * 已删除|未删除 枚举
 * @author ZhouJiaKai
 * @since  2018/02/24
 */
public enum DeleteTypeEnum {

    DELETED_TRUE(0, "已删除"),
    DELETED_FALSE(1, "未删除");

    private Integer val;

    private String name;

    DeleteTypeEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }
}

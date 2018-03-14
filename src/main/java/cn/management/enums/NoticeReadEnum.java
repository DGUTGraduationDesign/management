package cn.management.enums;

/**
 * 通知是否已读 枚举
 * @author ZhouJiaKai
 * @since  2018/03/14
 */
public enum NoticeReadEnum {

    UNREAD("未读", 0),
    READ("已读", 1);

    private String name;

    private Integer value;

    NoticeReadEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NoticeReadEnum{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public static String getName(Integer value) {
        for (NoticeReadEnum noticeReadEnum : NoticeReadEnum.values()) {
            if (noticeReadEnum.getValue().equals(value)) {
                return noticeReadEnum.getName();
            }
        }
        return null;
    }

}

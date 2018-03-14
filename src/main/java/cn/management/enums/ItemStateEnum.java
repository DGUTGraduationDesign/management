package cn.management.enums;

/**
 * 项目状态 枚举
 * @author ZhouJiaKai
 * @since  2018/03/14
 */
public enum ItemStateEnum {

    UNFINISHED("未完成", 0),
    FINISHED("已完成", 1);

    private String name;

    private Integer value;

    ItemStateEnum(String name, Integer value) {
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

    public static String getName(Integer value) {
        for (ItemStateEnum itemStateEnum : ItemStateEnum.values()) {
            if (itemStateEnum.getValue().equals(value)) {
                return itemStateEnum.getName();
            }
        }
        return null;
    }

}

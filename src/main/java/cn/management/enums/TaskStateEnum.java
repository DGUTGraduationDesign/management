package cn.management.enums;

/**
 * 任务状态 枚举
 * @author ZhouJiaKai
 * @since  2018/03/14
 */
public enum TaskStateEnum {

    UNCOMPLETE("未完成", 0),
    COMPLETE("已完成", 1),
    DELAY("延期完成", 2),
    CANCEL("已取消", 3);

    private String name;

    private Integer value;

    TaskStateEnum (String name, Integer value) {
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
        for (TaskStateEnum taskStateEnum : TaskStateEnum.values()) {
            if (taskStateEnum.value.equals(value)) {
                return taskStateEnum.getName();
            }
        }
        return null;
    }

}

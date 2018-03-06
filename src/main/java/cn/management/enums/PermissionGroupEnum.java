package cn.management.enums;

/**
 * 权限分组枚举
 * @author ZhouJiaKai
 * @since  2018/02/27
 */
public enum PermissionGroupEnum {

    SYSTEM("系统管理", "system"),
    ATTENDANCE("考勤管理", "attendance"),
    MEETING("会议管理", "meeting");

    private String name;

    private String value;

    PermissionGroupEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * 根据value获取name
     * @param value
     * @return
     */
    public static String getName(String value) {
    	for (PermissionGroupEnum groupEnum : PermissionGroupEnum.values()) {
    		if (value.equals(groupEnum.getValue())) {
    			return groupEnum.getName();
    		}
    	}
    	return null;
    }

}

package cn.management.enums;

/**
 * 会议通知方式枚举类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public enum InformWayEnum {
    
    NONE(0, "无"),
    
    MAIL(1, "邮件通知"),
    
    MESSAGE(2, "短信通知"),

    ALL(3, "邮件、短信通知");
    
    private Integer value;
    
    private String name;
    
    private InformWayEnum(Integer value, String name) {
         this.value = value;
         this.name = name;
    }

    public static String getName(Integer value) {
    	for (InformWayEnum informWayEnum : InformWayEnum.values()) {
    		if (informWayEnum.getValue().equals(value)) {
    			return informWayEnum.getName();
    		}
    	}
		return null;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public String getName() {
        return name;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}

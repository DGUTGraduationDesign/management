package cn.management.enums;

/**
 * 会议室预约状态枚举类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public enum BespeakStatusEnum {
    
    BESPEAK(0, "预约中"),
    
    OVERDUE(1, "已过期"),
    
    CANCEL(2, "已取消");
    
    private Integer value;
    
    private String name;
    
    private BespeakStatusEnum(Integer value, String name) {
         this.value = value;
         this.name = name;
    }
    
    public static String getName(Integer value) {
    	for (BespeakStatusEnum bespeakStatusEnum : BespeakStatusEnum.values()) {
    		if (bespeakStatusEnum.getValue().equals(value)) {
    			return bespeakStatusEnum.getName();
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

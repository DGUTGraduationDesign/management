package cn.management.enums;

/**
 * 考勤申请类型 枚举
 * @author ZhouJiaKai
 * @since  2018/02/28
 */
public enum ApplicationTypeEnum {
	
	/**
	 * 申请类型
	 * 0 请假申请
	 * 1 加班申请
	 */
	TYPE_LEAVE("请假申请", 0),
	TYPE_OVERTIME("加班申请", 1);
	
	private String name;
	
	private Integer value;
	
	ApplicationTypeEnum(String name, Integer value) {
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
		for (ApplicationTypeEnum applicationType : ApplicationTypeEnum.values()) {
			if (value.intValue() == applicationType.getValue().intValue()) {
				return applicationType.getName();
			}
		}
		return null;
	}
	
}

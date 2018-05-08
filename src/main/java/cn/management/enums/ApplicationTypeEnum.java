package cn.management.enums;

/**
 * 考勤申请类型 枚举
 * @author ZhouJiaKai
 * @since  2018/02/28
 */
public enum ApplicationTypeEnum {
	
	/**
	 * 申请类型
	 * 0 请假申请(事假)
	 * 1 请假申请(病假)
	 * 2 请假申请(产假)
	 * 3 请假申请(婚假)
	 * 4 出差申请
	 * 5 加班申请
	 */
	TYPE_MATTER("请假申请(事假)", 0),
	TYPE_SICK("请假申请(病假)", 1),
	TYPE_BIRTH("请假申请(产假)", 2),
	TYPE_MARRAY("请假申请(婚假)", 3),
	TYPE_LEAVE("出差申请", 4),
	TYPE_OVERTIME("加班申请", 5);
	
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

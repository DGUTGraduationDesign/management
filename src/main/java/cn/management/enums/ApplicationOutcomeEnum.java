package cn.management.enums;

/**
 * 考勤申请连线 枚举
 * @author ZhouJiaKai
 * @since  2018/03/06
 */
public enum ApplicationOutcomeEnum {
	
	/**
	 * 连线
	 * 0 驳回
	 * 1 批准
	 */
	OUTCOME_REJECTED("驳回", 0),
	OUTCOME_APPROVAL("批准", 1);
	
	private String name;
	
	private Integer value;
	
	ApplicationOutcomeEnum(String name, Integer value) {
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
		for (ApplicationOutcomeEnum applicationState : ApplicationOutcomeEnum.values()) {
			if (value.intValue() == applicationState.getValue().intValue()) {
				return applicationState.getName();
			}
		}
		return null;
	}
	
}

package cn.management.enums;

/**
 * 考勤申请状态类型 枚举
 * @author ZhouJiaKai
 * @since  2018/02/28
 */
public enum ApplicationStateEnum {
	
	/**
	 * 考勤申请状态
	 * 0 申请已提交
	 * 1 上级已审核
	 * 2 总监已审核
	 * 3 申请被驳回
	 */
	STATUS_COMMIT_SELF("申请已提交", 0),
    STATUS_COMMIT_MGR("上级已审核", 1),
    STATUS_COMMIT_HEAD("总监已审核", 2),
    STATUS_ROLL_BACK("申请被驳回", 3);;
	
	private String name;
	
	private Integer value;
	
	ApplicationStateEnum(String name, Integer value) {
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
		for (ApplicationStateEnum applicationState : ApplicationStateEnum.values()) {
			if (value.intValue() == applicationState.getValue().intValue()) {
				return applicationState.getName();
			}
		}
		return null;
	}
	
}

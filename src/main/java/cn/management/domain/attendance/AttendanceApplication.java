package cn.management.domain.attendance;

import java.util.Date;

import javax.persistence.Table;

import cn.management.domain.BaseEntity;
import cn.management.enums.ApplicationStateEnum;
import cn.management.enums.ApplicationTypeEnum;

/**
 * 考勤申请表实体类
 */
@Table(name = "attendance_application")
public class AttendanceApplication extends BaseEntity<Integer> {

	/**
	 * 申请类型，0表示请假申请，1表示加班申请
	 */
	private ApplicationTypeEnum type;
	
	/**
	 * 申请理由
	 */
	private String reason;
	
	/**
	 * 开始日期
	 */
	private Date startDate;
	
	/**
	 * 结束日期
	 */
	private Date endDate;

	/**
	 * 总天数(只计算工作日)
	 */
	private Double totalDays;
	
	/**
	 * 状态，0表示申请已提交，1表示上级已审核，2表示总监已审核，3表示申请被驳回
	 */
	private ApplicationStateEnum state;

	public ApplicationTypeEnum getType() {
		return type;
	}

	public String getReason() {
		return reason;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Double getTotalDays() {
		return totalDays;
	}

	public ApplicationStateEnum getState() {
		return state;
	}

	public void setType(ApplicationTypeEnum type) {
		this.type = type;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setTotalDays(Double totalDays) {
		this.totalDays = totalDays;
	}

	public void setState(ApplicationStateEnum state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "AttendanceApplication [type=" + type + ", reason=" + reason + ", startDate=" + startDate + ", endDate="
				+ endDate + ", totalDays=" + totalDays + ", state=" + state + ", id=" + id + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
	}

}

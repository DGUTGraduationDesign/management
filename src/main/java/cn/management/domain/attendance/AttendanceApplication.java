package cn.management.domain.attendance;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.management.domain.BaseEntity;

/**
 * 考勤申请表实体类
 */
@Table(name = "attendance_application")
public class AttendanceApplication extends BaseEntity<Integer> {

	/**
	 * 申请类型，0表示请假申请，1表示加班申请
	 */
	private Integer type;

	/**
	 * 申请类型名称
	 */
	@Transient
	private String typeName;
	
	/**
	 * 申请理由
	 */
	private String reason;
	
	/**
	 * 开始日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
	private Date startDate;
	
	/**
	 * 结束日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
	private Date endDate;

	/**
	 * 总天数(只计算工作日)
	 */
	private Double totalDays;
	
	/**
	 * 状态，0表示新建申请，1表示申请已提交，2表示上级已审核，3表示总监已审核，4表示申请被驳回
	 */
	private Integer state;

	/**
	 * 状态名称
	 */
	@Transient
	private String stateName;
	
	/**
	 * 申请人id
	 */
	private Integer userId;
	
	/**
	 * 申请人姓名
	 */
	@Transient
	private String realName;
	
	/**
	 * 直接上级id
	 */
	private Integer leaderId;
	
	/**
	 * 部门负责人id
	 */
	private Integer headerId;
	
	/**
	 * 连线名称：批准/驳回
	 */
	@Transient
	private String outcome;
	
	/**
	 * 批注
	 */
	@Transient
	private String comment;

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

	public Integer getState() {
		return state;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getRealName() {
		return realName;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public Integer getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getStateName() {
		return stateName;
	}

	public String getOutcome() {
		return outcome;
	}

	public String getComment() {
		return comment;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public void setState(Integer state) {
		this.state = state;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	@Override
	public String toString() {
		return "AttendanceApplication [type=" + type + ", typeName=" + typeName + ", reason=" + reason + ", startDate="
				+ startDate + ", endDate=" + endDate + ", totalDays=" + totalDays + ", state=" + state + ", stateName="
				+ stateName + ", userId=" + userId + ", realName=" + realName + ", leaderId=" + leaderId + ", headerId="
				+ headerId + ", outcome=" + outcome + ", comment=" + comment + ", id=" + id + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
	}

}

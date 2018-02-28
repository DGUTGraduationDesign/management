package cn.management.domain.attendance;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.management.domain.BaseEntity;

/**
 * 工时统计表实体类
 */
@Table(name = "attendance_hour")
public class AttendanceHour extends BaseEntity<Integer> {

	/**
	 * 统计年月
	 */
    @JsonFormat(pattern = "yyyy-MM", timezone="GMT+8")
	private Date recordDate;
	
	/**
	 * 工时
	 */
	private Double hours;
	
	/**
	 * 对应员工id
	 */
	private Integer userId;

	/**
	 * 对应员工姓名
	 */
	@Transient
	private String userName;

	public Date getRecordDate() {
		return recordDate;
	}

	public Double getHours() {
		return hours;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AttendanceHour [recordDate=" + recordDate + ", hours=" + hours + ", userId=" + userId + ", id=" + id
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
	}
	
}

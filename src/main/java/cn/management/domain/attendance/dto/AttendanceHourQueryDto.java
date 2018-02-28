package cn.management.domain.attendance.dto;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 工时统计条件查询dto
 */
public class AttendanceHourQueryDto {

	/**
	 * 工号
	 */
	private String number;
	
	/**
	 * 员工姓名
	 */
	private String realName;
	
	/**
	 * 部门id
	 */
	private Integer deptId;
	
	/**
	 * 职位id
	 */
	private Integer postId;
	
	/**
	 * 统计年月
	 */
	@JSONField(format = "yyyy-MM")
	private Date recordDate;

	public String getNumber() {
		return number;
	}

	public String getRealName() {
		return realName;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public Integer getPostId() {
		return postId;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	@Override
	public String toString() {
		return "AttendanceHourQueryDto [number=" + number + ", realName=" + realName + ", deptId=" + deptId
				+ ", postId=" + postId + ", recordDate=" + recordDate + "]";
	}
	
}

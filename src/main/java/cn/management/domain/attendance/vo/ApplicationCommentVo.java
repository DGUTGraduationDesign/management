package cn.management.domain.attendance.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 考勤申请批注
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class ApplicationCommentVo {

	/**
	 * 批注时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date time;

	/**
	 * 批注人
	 */
	private String userId;

	/**
	 * 批注信息
	 */
	private String fullMessage;
	
	public Date getTime() {
		return time;
	}

	public String getUserId() {
		return userId;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	@Override
	public String toString() {
		return "ApplicationCommentVo [time=" + time + ", userId=" + userId + ", fullMessage=" + fullMessage + "]";
	}
	
}

package cn.management.domain.meeting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.management.domain.BaseEntity;
import cn.management.domain.admin.AdminUser;

/**
 * 会议室预约记录数据模型
 */
public class MeetingBespeak extends BaseEntity<Integer> implements Serializable {

    /**
     * 会议主题
     */
    private String meetingTheme;

    /**
     * 预约开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date beginTime;

    /**
     * 预约结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone="GMT+8")
    private Date endTime;
    
    /**
     * 预约状态
     * 0 表示 "预约中"
     * 1 表示 "已过期"
     * 2 表示 "已取消"
     */
    private Integer bespeakStatus;

    /**
     * 预约状态
     */
    @Transient
    private String bespeakStatusName;

    /**
     * 通知方式
     * 0 表示 "无"
     * 1 表示 "邮件通知"
     * 2 表示 "短信通知"
     * 3 表示 "邮件、短信通知"
     */
    private Integer informWay;
    
    /**
     * 通知方式
     */
    @Transient
    private String informWayName;
    
    /**
     * 预约人
     */
    private Integer userId;
    
    /**
     * 预约人姓名
     */
    @Transient
    private String userName;
    
    /**
     * 对应的会议室id
     */
    private Integer meetingRoomId;
    
    /**
     * 对应的会议室
     */
    @Transient
    private MeetingRoom meetingRoom;
    
    /**
     * 参会人员id,json字符串
     */
    private String userIds;

    /**
     * 参会人员
     */
    @Transient
    private List<AdminUser> users;

	public String getMeetingTheme() {
		return meetingTheme;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public Integer getBespeakStatus() {
		return bespeakStatus;
	}

	public String getBespeakStatusName() {
		return bespeakStatusName;
	}

	public Integer getInformWay() {
		return informWay;
	}

	public String getInformWayName() {
		return informWayName;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getMeetingRoomId() {
		return meetingRoomId;
	}

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public String getUserIds() {
		return userIds;
	}

	public List<AdminUser> getUsers() {
		return users;
	}

	public void setUsers(List<AdminUser> users) {
		this.users = users;
	}

	public void setMeetingTheme(String meetingTheme) {
		this.meetingTheme = meetingTheme;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setBespeakStatus(Integer bespeakStatus) {
		this.bespeakStatus = bespeakStatus;
	}

	public void setBespeakStatusName(String bespeakStatusName) {
		this.bespeakStatusName = bespeakStatusName;
	}

	public void setInformWay(Integer informWay) {
		this.informWay = informWay;
	}

	public void setInformWayName(String informWayName) {
		this.informWayName = informWayName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setMeetingRoomId(Integer meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return "MeetingBespeak [meetingTheme=" + meetingTheme + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", bespeakStatus=" + bespeakStatus + ", bespeakStatusName=" + bespeakStatusName + ", informWay="
				+ informWay + ", informWayName=" + informWayName + ", userId=" + userId + ", userName=" + userName
				+ ", meetingRoomId=" + meetingRoomId + ", meetingRoom=" + meetingRoom + ", userIds=" + userIds
				+ ", users=" + users + ", id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", delFlag=" + delFlag + "]";
	}

}

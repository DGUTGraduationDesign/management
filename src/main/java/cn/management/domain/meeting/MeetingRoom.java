package cn.management.domain.meeting;

import java.io.Serializable;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;

/**
 * 会议室数据模型
 */
@Table(name = "meeting_room")
public class MeetingRoom extends BaseEntity<Integer> implements Serializable {

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 可容纳人数
     */
    private Integer seatNumber;
    
    /**
     * 会议室地点
     */
    private String roomPlace;
    
    public String getRoomName() {
        return roomName;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public String getRoomPlace() {
        return roomPlace;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setRoomPlace(String roomPlace) {
        this.roomPlace = roomPlace;
    }

	@Override
	public String toString() {
		return "MeetingRoom [roomName=" + roomName + ", seatNumber=" + seatNumber + ", roomPlace=" + roomPlace + ", id="
				+ id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
	}

}

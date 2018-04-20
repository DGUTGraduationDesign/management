package cn.management.util.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.domain.meeting.MeetingRoom;
import cn.management.enums.BespeakStatusEnum;
import cn.management.exception.SysException;
import cn.management.util.SendMeetingInformUtil;
import org.junit.Test;

import cn.management.util.MailUtil;

/**
 * MailUtil测试类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
public class MailUtilTest {

	@Test
	public void sendMailTest() {
//		String toAddr = "598959863@qq.com";
//		String subject = "会议通知";
//		String content = "时间：2018-03-06 08:30 - 09:30，地点：会议室2";
//		try {
//			//MailUtil.sendMail(toAddr, subject, content);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            MeetingBespeak meetingBespeak = new MeetingBespeak();
//            meetingBespeak.setUserId(1);
//            meetingBespeak.setMeetingTheme("test");
//            meetingBespeak.setMeetingRoomId(1);
//            meetingBespeak.setBeginTime(sdf.parse("2018-04-20 15:00"));
//            meetingBespeak.setEndTime(sdf.parse("2018-04-20 15:00"));
//            MeetingRoom meetingRoom = new MeetingRoom();
//            meetingRoom.setRoomName("会议室1");
//            meetingBespeak.setMeetingRoom(meetingRoom);
//            meetingBespeak.setCreateTime(new Date());
//            meetingBespeak.setUpdateTime(new Date());
//            List<AdminUser> list = new ArrayList<AdminUser>();
//            AdminUser user = new AdminUser();
//            user.setRealName("test");
//            user.setMail("598959863@qq.com");
//            list.add(user);
//            meetingBespeak.setUsers(list);
//            meetingBespeak.setBespeakStatus(BespeakStatusEnum.BESPEAK.getValue());
//            SendMeetingInformUtil.sendMeetingInformMail(meetingBespeak);
//		} catch (MessagingException | ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SysException e) {
//            e.printStackTrace();
//        }
    }
	
}

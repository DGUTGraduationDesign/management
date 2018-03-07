package cn.management.service.meeting.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.meeting.MeetingBespeak;
import cn.management.domain.meeting.MeetingRoom;
import cn.management.enums.BespeakStatusEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.InformWayEnum;
import cn.management.enums.MeetingBespeakJobEnum;
import cn.management.exception.SysException;
import cn.management.job.MeetingJobManager;
import cn.management.job.QuartzManager;
import cn.management.mapper.meeting.MeetingBespeakMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.meeting.MeetingBespeakService;
import cn.management.service.meeting.MeetingRoomService;
import cn.management.util.SendMeetingInformUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * 会议室预约service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-06
 */
@Service
public class MeetingBespeakServiceImpl extends BaseServiceImpl<MeetingBespeakMapper, MeetingBespeak> implements MeetingBespeakService {

	private final static Logger logger =  LoggerFactory.getLogger(MeetingBespeakServiceImpl.class);
	
	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private MeetingRoomService meetingRoomService;
	
	/**
	 * 条件查询会议室预约列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<MeetingBespeak> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<MeetingBespeak> list = mapper.selectByExample(example);
        for (MeetingBespeak meetingBespeak : list) {
        	//设置预约状态、通知方式、预约人、对应的会议室
        	setName(meetingBespeak);
        }
        return list;
    }
	
	/**
	 * 查询会议室预约详情
     * @param condition
     * @return
	 */
	@Override
	public MeetingBespeak getItem(MeetingBespeak condition) {
		List<MeetingBespeak> items = getItemsByPage(condition, 1, 1);
		if (CollectionUtils.isEmpty(items)) {
            return null;
        }
		MeetingBespeak meetingBespeak = items.get(0);
    	//设置预约状态、通知方式、预约人、对应的会议室
    	setName(meetingBespeak);
    	//参会人员
    	setUserList(meetingBespeak);
		return meetingBespeak;
	}

	/**
	 * 根据id查询会议室预约
     * @param id
     * @return
	 */
	@Override
	public MeetingBespeak getItemById(Object id) {
		MeetingBespeak meetingBespeak = mapper.selectByPrimaryKey(id);
    	//设置预约状态、通知方式、预约人、对应的会议室
    	setName(meetingBespeak);
    	//参会人员
    	setUserList(meetingBespeak);
		return meetingBespeak;
	}
	
	/**
	 * 设置预约状态、通知方式、预约人、对应的会议室
	 * @param meetingBespeak
	 */
	private void setName(MeetingBespeak meetingBespeak) {
		//预约状态
		meetingBespeak.setBespeakStatusName(BespeakStatusEnum.getName(meetingBespeak.getBespeakStatus()));
		//通知方式
		meetingBespeak.setInformWayName(InformWayEnum.getName(meetingBespeak.getInformWay()));
		//预约人姓名
		AdminUser user = adminUserService.getItemById(meetingBespeak.getUserId());
		if (null != user) {
			meetingBespeak.setUserName(user.getRealName());
		}
		//对应会议室
		MeetingRoom meetingRoom = meetingRoomService.getItemById(meetingBespeak.getMeetingRoomId());
		if (null != meetingRoom) {
			meetingBespeak.setMeetingRoom(meetingRoom);
		}
	}
	
	/**
	 * 添加参会人员
	 * @param meetingBespeak
	 */
	private void setUserList(MeetingBespeak meetingBespeak) {
    	//参会人员
    	List<AdminUser> users = new ArrayList<AdminUser>(5);
    	List<Integer> userIds = JSONObject.parseArray(meetingBespeak.getUserIds(), Integer.class);
    	for (Integer userId : userIds) {
    		AdminUser user = adminUserService.getItemById(userId);
    		if (null != user) {
    			users.add(user);
    		}
    	}
    	meetingBespeak.setUsers(users);
	}

	/**
	 * 查询会议室在对应时间段内是否被预约
	 * @param meetingBespeak
	 * @return
	 */
	@Override
	public boolean existsBespeakByIdAndTime(MeetingBespeak meetingBespeak) {
		if (0 != mapper.countBespeakByIdAndTime(meetingBespeak)) {
			return  false;
		}
		return true;
	}

	/**
	 * 预约会议室
	 * @param meetingBespeak
	 * @return
	 * @throws SysException 
	 */
	@Override
	@Transactional
	public MeetingBespeak doBespeak(MeetingBespeak meetingBespeak) throws SysException {
	    //会议室预约常规判断
		checkBespeak(meetingBespeak);
		//更新数据库
		MeetingBespeak bespeak = addSelectiveMapper(meetingBespeak);
		if (null != bespeak) {
			//查询会议室
			setName(bespeak);
			//查询参会人员
			setUserList(bespeak);
			//通知方式
			Integer informWay = meetingBespeak.getInformWay();
	        try {
	        	//添加定时任务
				if (!InformWayEnum.NONE.getValue().equals(informWay)) {
					//添加会前发送提醒通知定时任务
					MeetingJobManager.addSendMeetingInformJob(meetingBespeak);
				}
				//添加会议过期自动修改预约状态定时任务
				MeetingJobManager.addOverdueBespeakJob(meetingBespeak);
	        } catch (SchedulerException e) {
	            throw new SysException("添加定时任务失败！请重试");
	        }
	        
			//发送通知
			if (!InformWayEnum.NONE.getValue().equals(informWay)) {
	            //再开启一个新的线程发送会议邮件通知、短信通知
	            Thread th = new Thread(new Runnable() {
	                public void run() {
	                    try {
	                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送邮件通知
	                            SendMeetingInformUtil.sendMeetingInformMail(bespeak);
	                        }
	                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送短信通知
	                            SendMeetingInformUtil.sendMeetingInformMessage(bespeak);
	                        }
	                    } catch (Exception e) {
	                    	logger.error(bespeak.toString(), e);
	                    }
	                }
	            });
	            th.start();
	        }
		}
		
		return bespeak;
	}

	/**
	 * 修改会议室预约
	 * @param meetingBespeak
	 * @param loginUserId
	 * @return
	 * @throws SysException 
	 */
	@Override
	@Transactional
	public boolean doUpdate(MeetingBespeak meetingBespeak, Integer loginUserId) throws SysException {
		//查询有无该对象
		MeetingBespeak condition = new MeetingBespeak();
		condition.setId(meetingBespeak.getId());
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		//后面判断时间是否有修改会用到
		MeetingBespeak findMeetingBespeak = getItem(condition);
		if (null == findMeetingBespeak) {
			throw new SysException("所编辑对象不存在.");
		}
		//判断是否是预约者本人操作
		if (!loginUserId.equals(findMeetingBespeak.getUserId())) {
			throw new SysException("非预约者本人操作.");
		}
		//判断预约状态
		if (!BespeakStatusEnum.BESPEAK.getValue().equals(findMeetingBespeak.getBespeakStatus())) {
			throw new SysException("该预约记录已失效.");
		}
		//会议室预约常规判断
		checkBespeak(meetingBespeak);
		//更新数据库
		boolean flag = update(meetingBespeak);
		if (flag) {
			Integer informWay = findMeetingBespeak.getInformWay();
			//设置参会人员
			setUserList(meetingBespeak);
			//设置会议室
			setName(meetingBespeak);
	        try {
				//判断会议开始时间是否有修改
	            if (findMeetingBespeak.getBeginTime().getTime() != meetingBespeak.getBeginTime().getTime()) {
	                //判断通知方式是否是无
	                if (!InformWayEnum.NONE.getValue().equals(informWay)) {
	                    //修改定时任务触发时间
	                    MeetingJobManager.editSendMeetingInformJob(meetingBespeak);
	                }
	            }
	            //判断会议结束时间是否有修改
	            if (findMeetingBespeak.getEndTime().getTime() != meetingBespeak.getEndTime().getTime()) {
	                //若有更改则修改会议过期自动修改预约状态定时任务
	                String triggerName = MeetingBespeakJobEnum.OVERDUE_JOB_TIGGER_NAME + meetingBespeak.getId();
	                QuartzManager.modifyJobTime(triggerName, meetingBespeak.getEndTime());
	            }
	        } catch (SchedulerException e) {
	            throw new SysException("修改定时任务失败！请重试");
	        }
	        
	       //判断是否发送邮件通知和短信通知
	        if (!InformWayEnum.NONE.getValue().equals(informWay)) {
	            //再开启一个新的线程发送会议邮件通知、短信通知
	            Thread th = new Thread(new Runnable() {
	                public void run() {
	                	try {
	                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送邮件通知
	                            SendMeetingInformUtil.sendUpdateMeetingMail(meetingBespeak, findMeetingBespeak.getBeginTime());
	                        }
	                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送短信通知
	                            SendMeetingInformUtil.sendUpdateMeetingMessage(meetingBespeak, findMeetingBespeak.getBeginTime());
	                        }
	                    } catch (Exception e) {
	                    	logger.error(meetingBespeak.toString(), e);
	                    }
	                }
	            });
	            th.start();
	        }
		}
		return flag;
	}
	
	/**
	 * 取消预约
	 * @param bespeakId
	 * @param loginUserId
	 * @return
	 * @throws SysException 
	 */
	@Override
	public boolean doCancel(Integer bespeakId, Integer loginUserId) throws SysException {
		//查询有无该对象
		MeetingBespeak condition = new MeetingBespeak();
		condition.setId(bespeakId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		//后面判断会用到
		MeetingBespeak findMeetingBespeak = getItem(condition);
		if (null == findMeetingBespeak) {
			throw new SysException("所编辑对象不存在.");
		}
		//判断是否是预约者本人操作
		if (!loginUserId.equals(findMeetingBespeak.getUserId())) {
			throw new SysException("非预约者本人操作.");
		}
		//判断预约状态
		if (!BespeakStatusEnum.BESPEAK.getValue().equals(findMeetingBespeak.getBespeakStatus())) {
			throw new SysException("该预约记录已失效.");
		}
		//修改预约状态
		findMeetingBespeak.setUpdateTime(new Date());
		findMeetingBespeak.setBespeakStatus(BespeakStatusEnum.CANCEL.getValue());
		//更新数据库
		boolean flag = update(findMeetingBespeak);
		if (flag) {
			try {
	            //移除会议预约过期自动修改预约状态任务名
	            MeetingJobManager.removeOverdueBespeakJob(bespeakId);
	            //移除该会议预约记录的会前通知定时任务
	            MeetingJobManager.removeSendMeetingInformJob(bespeakId);
	        } catch (SchedulerException e1) {
	            throw new SysException("取消定时任务失败！请重试");
	        }
	        
	        //判断是否发送邮件通知和短信通知
	        Integer informWay = findMeetingBespeak.getInformWay();
	        if (!InformWayEnum.NONE.getValue().equals(informWay)) {
	            //再开启一个新的线程发送会议邮件通知、短信通知
	            Thread th = new Thread(new Runnable() {
	                public void run() {
	                    try {
	                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送邮件通知
	                            SendMeetingInformUtil.sendMeetingInformMail(findMeetingBespeak);
	                        }
	                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
	                            //发送短信通知
	                            SendMeetingInformUtil.sendMeetingInformMessage(findMeetingBespeak);
	                        }
	                    } catch (Exception e) {
	                    	logger.error(findMeetingBespeak.toString(), e);
	                    }
	                }
	            });
	            th.start();
	        }
		}
		return flag;
	}

	/**
	 * 修改通知方式
	 * @param bespeakId
	 * @param loginUserId
	 * @param informWay
	 * @return
	 * @throws SysException
	 */
	@Override
	public boolean doChangeInformWay(Integer bespeakId, Integer loginUserId, Integer informWay) throws SysException {
		//查询有无该对象
		MeetingBespeak condition = new MeetingBespeak();
		condition.setId(bespeakId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		//后面判断会用到
		MeetingBespeak findMeetingBespeak = getItem(condition);
		if (null == findMeetingBespeak) {
			throw new SysException("所编辑对象不存在.");
		}
		//判断是否是预约者本人操作
		if (!loginUserId.equals(findMeetingBespeak.getUserId())) {
			throw new SysException("非预约者本人操作.");
		}
		//判断预约状态
		if (!BespeakStatusEnum.BESPEAK.getValue().equals(findMeetingBespeak.getBespeakStatus())) {
			throw new SysException("该预约记录已失效.");
		}
		//修改通知方式
		MeetingBespeak meetingBespeak = new MeetingBespeak();
		meetingBespeak.setId(bespeakId);
		meetingBespeak.setUpdateTime(new Date());
		meetingBespeak.setInformWay(informWay);
		boolean flag = update(meetingBespeak);
		if (flag) {
			try {
	            if (InformWayEnum.NONE.getValue().equals(informWay) && 
	            	!InformWayEnum.NONE.getValue().equals(findMeetingBespeak.getInformWay())) {
	                //移除该会议预约记录的会前通知定时任务
	                MeetingJobManager.removeSendMeetingInformJob(bespeakId);
	            } else if (!InformWayEnum.NONE.getValue().equals(informWay) &&
	            	InformWayEnum.NONE.getValue().equals(findMeetingBespeak.getInformWay())) {
	                //查询会议记录信息
	                MeetingBespeak bespeak = getItemById(bespeakId);
	                //添加会前通知定时任务
	                MeetingJobManager.addSendMeetingInformJob(bespeak);
	            }
	        } catch (SchedulerException e) {
	            throw new SysException("修改定时任务失败！请重试");
	        }
		}
		return flag;
	}
	
	/**
	 * 会议室预约常规判断
	 * @param meetingBespeak
	 * @throws SysException
	 */
	public void checkBespeak(MeetingBespeak meetingBespeak) throws SysException {
		//判断预约时间是否大于当前时间
		Date date = new Date();
		if (meetingBespeak.getBeginTime().getTime() >= meetingBespeak.getEndTime().getTime()) {
			throw new SysException("结束时间必须大于开始时间.");
		}
		if (meetingBespeak.getBeginTime().getTime() <= date.getTime()) {
			throw new SysException("请提前预约.");
		}
		//查询所选会议室是否存在
        MeetingRoom findRoom = meetingRoomService.getItemById(meetingBespeak.getMeetingRoomId());
        if (findRoom == null) {
            throw new SysException("所选会议室不存在.");
        }
        //查询该会议室该时间段内有无预约
        if (0 != mapper.countBespeakByIdAndTime(meetingBespeak)) {
            throw new SysException("该会议室在该时间段内已被预约.");
        }
	}

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
		Example example = new Example(MeetingBespeak.class);
		example.createCriteria().andCondition("id IN(" + ids + ")");
		MeetingBespeak meetingBespeak = new MeetingBespeak();
		meetingBespeak.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		return updateByExampleSelective(meetingBespeak, example);
	}

}

package cn.management.service.attendance.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;

import cn.management.domain.admin.AdminDataDict;
import cn.management.domain.admin.AdminPosition;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.attendance.AttendanceApplication;
import cn.management.domain.attendance.vo.ApplicationCommentVo;
import cn.management.enums.ApplicationOutcomeEnum;
import cn.management.enums.ApplicationStateEnum;
import cn.management.enums.ApplicationTypeEnum;
import cn.management.enums.AttendanceIdentityEnum;
import cn.management.enums.DeleteTypeEnum;
import cn.management.exception.SysException;
import cn.management.mapper.attendance.AttendanceApplicationMapper;
import cn.management.service.admin.AdminDataDictService;
import cn.management.service.admin.AdminPositionService;
import cn.management.service.admin.AdminUserService;
import cn.management.service.attendance.AttendanceApplicationService;
import cn.management.service.impl.BaseServiceImpl;
import tk.mybatis.mapper.entity.Example;

/**
 * 考勤申请Service层接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-02
 */
@Service
public class AttendanceApplicationServiceImpl extends BaseServiceImpl<AttendanceApplicationMapper, AttendanceApplication>
		implements AttendanceApplicationService {

	@Autowired
	private AdminUserService adminUserService;
	
	@Autowired
	private AdminPositionService adminPositionService;

	@Autowired
	private AdminDataDictService adminDataDictService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 条件查询考勤申请列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
	@Override
    public List<AttendanceApplication> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<AttendanceApplication> list = mapper.selectByExample(example);
        //设置申请类型名称、状态名称、申请人姓名、上级姓名、总监姓名
		for (AttendanceApplication attendanceApplication : list) {
			setName(attendanceApplication);
        }
        return list;
    }
	
	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     * @param condition
     * @return
	 */
	@Override
	public AttendanceApplication getItem(AttendanceApplication condition) {
		List<AttendanceApplication> items = getItemsByPage(condition, 1, 1);
		if (CollectionUtils.isEmpty(items)) {
            return null;
        }
		AttendanceApplication attendanceApplication =  items.get(0);
		//设置申请类型名称、状态名称、申请人姓名、上级姓名、总监姓名
		setName(attendanceApplication);
		return attendanceApplication;
	}

	/**
	 * 使用考勤申请id，查询历史批注信息
	 * @param id
	 * @return
	 */
	@Override
	public List<ApplicationCommentVo> findCommentByLeaveBillId(Integer id) {
		/**1:使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID*/
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
						.processInstanceBusinessKey(id.toString()) //使用BusinessKey字段查询
						.singleResult();
		String processInstanceId = hpi.getId();
		List<Comment> commentList = taskService.getProcessInstanceComments(processInstanceId);
		List<ApplicationCommentVo> voList = new ArrayList<ApplicationCommentVo>(5);
		//entity to vo
		for (Comment comment : commentList) {
			ApplicationCommentVo vo = new ApplicationCommentVo();
			vo.setTime(comment.getTime());
			vo.setUserId(comment.getUserId());
			vo.setFullMessage(comment.getFullMessage());
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 设置申请类型名称、状态名称、申请人姓名、上级姓名、总监姓名
	 * @param list
	 */
    private void setName(AttendanceApplication attendanceApplication) {
		//设置申请类型名称
		attendanceApplication.setTypeName(ApplicationTypeEnum.getName(attendanceApplication.getType()));
		//状态名称
		attendanceApplication.setStateName(ApplicationStateEnum.getName(attendanceApplication.getState()));
		//设置申请人姓名
		AdminUser user = adminUserService.getItemById(attendanceApplication.getUserId());
		if (user != null) {
			attendanceApplication.setRealName(user.getRealName());
		}
		//直接上级姓名
		AdminUser leader = adminUserService.getItemById(attendanceApplication.getLeaderId());
		if (leader != null) {
			attendanceApplication.setLeaderName(leader.getRealName());
		}
		//部门总监姓名
		AdminUser header = adminUserService.getItemById(attendanceApplication.getHeaderId());
		if (header != null) {
			attendanceApplication.setHeaderName(header.getRealName());
		}
	}

	/**
	 * 添加考勤申请
	 * @param attendanceApplication
	 * @return
	 * @throws SysException 
	 */
	@Override
	@Transactional
	public boolean doAdd(AttendanceApplication attendanceApplication, Integer loginUserId) throws SysException {
    	//个人id
		attendanceApplication.setUserId(loginUserId);
		//设置直属上级
		AdminUser adminUser = adminUserService.getItemById(attendanceApplication.getUserId());
		if (null == adminUser.getLeaderId()) {
			throw new SysException("无直属上级.");
		}
		attendanceApplication.setLeaderId(adminUser.getLeaderId());
		//设置部门总监
		AdminPosition adminPosition = adminPositionService.getItemById(adminUser.getPostId());
		if (null == adminPosition || null == adminPosition.getMgrId() || 0 == adminPosition.getMgrId()) {
			throw new SysException("所在岗位无负责人.");
		}
		AdminUser headUser = new AdminUser();
		headUser.setPostId(adminPosition.getMgrId());
		List<AdminUser> header = adminUserService.getItems(headUser);
		if (null == header || 1 != header.size()) {
			throw new SysException("部门总监不唯一.");
		}
		attendanceApplication.setHeaderId(header.get(0).getId());
		//设置状态
		attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_START.getValue());
		attendanceApplication.setCreateTime(new Date());
		attendanceApplication.setUpdateTime(new Date());
		//添加考勤申请
		if(null == addSelectiveMapper(attendanceApplication)) {
			return false;
		}
		//执行任务
		return excuteTask(attendanceApplication, loginUserId);
	}
	
	/**
	 * 考勤申请审核
	 * @param attendanceApplication
	 * @return
	 * @throws SysException 
	 */
	@Override
	@Transactional
	public boolean doAudit(AttendanceApplication attendanceApplication, String identity, Integer loginUserId) throws SysException {
		//检查权限
		SecurityUtils.getSubject().checkPermission(AttendanceIdentityEnum.getPermission(identity));
		AttendanceApplication condition = new AttendanceApplication();
		condition.setId(attendanceApplication.getId());
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		AttendanceApplication application = getItem(condition);
		if (null == application) {
			throw new SysException("考勤申请不存在.");
		}
		if (ApplicationStateEnum.STATUS_COMMIT_END.getValue().equals(application.getState())) {
			throw new SysException("审核已结束.");
		}
		if (ApplicationStateEnum.STATUS_COMMIT_CANCEL.getValue().equals(application.getState())) {
			throw new SysException("申请已取消.");
		}
		//批准or驳回
		application.setOutcome(attendanceApplication.getOutcome());
		//批注
		application.setComment(attendanceApplication.getComment());
		application.setUpdateTime(new Date());
		//执行任务
		return excuteTask(application, loginUserId);
	}

	/**
	 * 考勤申请取消
	 * @param applicationId
	 * @param loginUserId
	 * @return
	 * @throws SysException 
	 */
	@Override
	@Transactional
	public boolean doCancel(Integer applicationId, Integer loginUserId) throws SysException {
		AttendanceApplication application = new AttendanceApplication();
		application.setId(applicationId);
		application.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		AttendanceApplication findApplication = getItem(application);
		if (null == findApplication) {
			throw new SysException("考勤申请不存在.");
		}
		if (ApplicationStateEnum.STATUS_COMMIT_END.getValue().equals(application.getState())) {
			throw new SysException("申请已结束.");
		}
		if (!findApplication.getUserId().equals(loginUserId)) {
			throw new SysException("不是本人不能取消.");
		}
		ProcessInstance instance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(applicationId.toString())
				.singleResult();
    	if (null != instance && null != instance.getProcessInstanceId()) {
    		runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), "deleted");
    	}
    	application.setState(ApplicationStateEnum.STATUS_COMMIT_CANCEL.getValue());
    	return update(application);
	}
	
	/**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
	@Override
	@Transactional
	public boolean logicalDelete(String ids) {
        Example example = new Example(AttendanceApplication.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        AttendanceApplication attendanceApplication = new AttendanceApplication();
        attendanceApplication.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
		if (updateByExampleSelective(attendanceApplication, example)) {
			String[] list = ids.split(",");
			for (String id : list) {
	        	ProcessInstance instance = runtimeService.createProcessInstanceQuery()
						.processInstanceBusinessKey(id)
						.singleResult();
	        	if (null != instance && null != instance.getProcessInstanceId()) {
	        		runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), "deleted");
	        	}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 执行考核任务
	 * @param attendanceApplication
	 * @throws SysException 
	 */
	@Transactional
	public boolean excuteTask(AttendanceApplication attendanceApplication, Integer loginUserId) throws SysException {
		//查找考核流程定义key
		AdminDataDict condition = new AdminDataDict();
		condition.setDictKey("BPMN_KEY_APPLICATION");
		AdminDataDict adminDataDict = adminDataDictService.getItem(condition);
		String key = adminDataDict.getDictVal();
		//添加添加当前任务的审核人(展示批注时用),使用Authentication.setAuthenticatedUserId();
		Authentication.setAuthenticatedUserId(adminUserService.getItemById(loginUserId).getRealName());
		//设置流程变量
		Map<String, Object> variables = new HashMap<>(8);
		if (ApplicationStateEnum.STATUS_COMMIT_START.getValue().equals(attendanceApplication.getState()) ||
				ApplicationStateEnum.STATUS_ROLL_BACK.getValue().equals(attendanceApplication.getState())) {
			//完成个人任务
			Task task = taskService.createTaskQuery()
							.taskAssignee(loginUserId.toString())
							.processInstanceBusinessKey(attendanceApplication.getId().toString())
							.singleResult();
			if (null == task) {
				//用户id
				variables.put("userId", attendanceApplication.getUserId().toString());
				//直属上级id
				variables.put("leaderId", attendanceApplication.getLeaderId().toString());
				//部门总监id
				variables.put("headerId", attendanceApplication.getHeaderId().toString());
				try {
					//开启流程实例 第二个参数是表act_ru_execution有个字段BUSINESS_KEY_对应类中的businessKey,关联业务
					runtimeService.startProcessInstanceByKey(key, attendanceApplication.getId().toString(), variables);
					task = taskService.createTaskQuery()
								.taskAssignee(loginUserId.toString())
								.processInstanceBusinessKey(attendanceApplication.getId().toString())
								.singleResult(); 
				} catch (ActivitiObjectNotFoundException ex) {
					throw new SysException("开启流程错误，请检查流程部署");
				}
			}
			//添加批注
			taskService.addComment(task.getId(), task.getProcessInstanceId(), attendanceApplication.getComment());
			//完成任务
			taskService.complete(task.getId());
			//更改状态
			attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_SELF.getValue());
			return update(attendanceApplication);
		} else if (ApplicationStateEnum.STATUS_COMMIT_SELF.getValue().equals(attendanceApplication.getState())) {
			//直属上级审核
			Task task = taskService.createTaskQuery()
					.taskAssignee(loginUserId.toString())
					.processInstanceBusinessKey(attendanceApplication.getId().toString())
					.singleResult();
			if (task == null) {
				throw new SysException("未查询到activiti任务");
			}
			//添加批注
			taskService.addComment(task.getId(), task.getProcessInstanceId(), attendanceApplication.getComment());
			variables.put("outcome", ApplicationOutcomeEnum.getName(attendanceApplication.getOutcome()));
			variables.put("leadEqHead", attendanceApplication.getLeaderId().equals(attendanceApplication.getHeaderId()));
			//完成任务
			taskService.complete(task.getId(), variables);
			//更改状态,如果上级和部门总监是同一人且上级批准则直接结束审核
			if (ApplicationOutcomeEnum.OUTCOME_APPROVAL.getValue().equals(attendanceApplication.getOutcome()) && attendanceApplication.getLeaderId().equals(attendanceApplication.getHeaderId())) {
				attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_END.getValue());
			} else if (ApplicationOutcomeEnum.OUTCOME_REJECTED.getValue().equals(attendanceApplication.getOutcome())) {
				attendanceApplication.setState(ApplicationStateEnum.STATUS_ROLL_BACK.getValue());
			} else {
				attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_LEAD.getValue());
			}
			return update(attendanceApplication);
		} else if (ApplicationStateEnum.STATUS_COMMIT_LEAD.getValue().equals(attendanceApplication.getState())) {
			//部门负责人审核
			Task task = taskService.createTaskQuery()
					.taskAssignee(loginUserId.toString())
					.processInstanceBusinessKey(attendanceApplication.getId().toString())
					.singleResult();
			if (task == null) {
				throw new SysException("未查询到activiti任务");
			}
			//添加批注
			taskService.addComment(task.getId(), task.getProcessInstanceId(), attendanceApplication.getComment());
			variables.put("outcome", ApplicationOutcomeEnum.getName(attendanceApplication.getOutcome()));
			//完成任务
			taskService.complete(task.getId(), variables);
			//更改状态
			if (ApplicationOutcomeEnum.OUTCOME_APPROVAL.getValue().equals(attendanceApplication.getOutcome())) {
				attendanceApplication.setState(ApplicationStateEnum.STATUS_COMMIT_END.getValue());
			} else {
				attendanceApplication.setState(ApplicationStateEnum.STATUS_ROLL_BACK.getValue());
			}
			return update(attendanceApplication);
		}
		return false;
	}

}

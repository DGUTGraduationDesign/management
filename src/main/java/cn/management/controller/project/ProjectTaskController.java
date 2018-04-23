package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectTask;
import cn.management.domain.project.dto.ProjectMyTaskDto;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.enums.TaskIdentityEnum;
import cn.management.enums.TaskStateEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.project.ProjectTaskService;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 * 
 * @author ZhouJiaKai
 * @date 2018-03-15
 */
@Controller
@RequestMapping("project/task")
public class ProjectTaskController extends BaseController<ProjectTaskService, ProjectTask> {

	static Logger logger = LoggerFactory.getLogger(ProjectTaskController.class);

    /**
     * 我的任务
     * @param models
     * @param request
     * @return
     */
    @RequestMapping("/self/index")
    @RequiresPermissions("projectTask:self")
    @ResponseBody
    public Result selfIndex(@RequestBody Map<String, Object> models, HttpServletRequest request) {
        // 拼接条件
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        ProjectTask projectTask = JSON.parseObject((String) models.get("projectTask"), ProjectTask.class);
        Integer page = (Integer) models.get("page");
        // 我的任务
        List<ProjectMyTaskDto> list = service.getMyTaskByPage(projectTask, loginId, page, getPageSize());
        if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        }
        PageInfo<ProjectMyTaskDto> pageInfo = new PageInfo<ProjectMyTaskDto>(list);
        return new Result(ResultEnum.SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageNum(), getPageSize());
    }

    /**
     * 发布的任务
     * @param models
     * @param request
     * @return
     */
	@RequestMapping("/publish/index")
    @RequiresPermissions("projectTask:publish")
	@ResponseBody
	public Result publishIndex(@RequestBody Map<String, Object> models, HttpServletRequest request) {
		// 拼接条件
		Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		ProjectTask projectTask = JSON.parseObject((String) models.get("projectTask"), ProjectTask.class);
		Integer page = (Integer) models.get("page");
		Example example = new Example(ProjectTask.class);
		Example.Criteria criteria = example.createCriteria();
		// 发布的任务
        criteria.andEqualTo("createBy", loginId);
		if (null != projectTask.getTaskState()) {
			criteria.andEqualTo("taskState", projectTask.getTaskState());
		}
		criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
		setExample(example);
		return list(page);
	}
	
	/**
	 * 查询任务详情
	 * @param models
	 * @return
	 */
	@RequestMapping("/findTaskById")
	@RequiresPermissions("projectTask:findTaskById")
	@ResponseBody
	public Result findApplicationById(@RequestBody Map<String, Object> models) {
		Integer taskId = (Integer) models.get("taskId");
		ProjectTask condition = new ProjectTask();
		condition.setId(taskId);
		condition.setDelFlag(DeleteTypeEnum.DELETED_FALSE.getVal());
		ProjectTask projectTask = service.getItem(condition);
		if (null == projectTask) {
			return new Result(ResultEnum.NO_RECORDS);
		} else {
			return new Result(ResultEnum.SUCCESS, projectTask, 1, 0, 1);
		}
	}

	/**
	 * 发布任务
	 * @param projectTask
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/publish")
	@RequiresPermissions("projectTask:publish")
	@ResponseBody
	public Result publish(@RequestBody ProjectTask projectTask, HttpServletRequest request)
			throws SysException {
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		projectTask.setCreateTime(new Date());
		projectTask.setUpdateTime(new Date());
		if (null != service.doPublish(projectTask, loginUserId)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

	/**
	 * 编辑任务
	 * @param projectTask
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/edit")
	@RequiresPermissions("projectTask:edit")
	@ResponseBody
	public Result edit(@RequestBody ProjectTask projectTask, HttpServletRequest request) throws SysException {
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		projectTask.setUpdateTime(new Date());
		if (service.doUpdate(projectTask, loginUserId)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

	/**
	 * 完成任务/取消任务
	 * @param models
	 * @param request
	 * @return
	 * @throws SysException
	 */
	@RequestMapping("/{identity}")
	@ResponseBody
	public Result complete(@RequestBody Map<String, Object> models, HttpServletRequest request, @PathVariable String identity) throws SysException {
		// 检查权限
		SecurityUtils.getSubject().checkPermission(TaskIdentityEnum.getPermission(identity));
		Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
		Integer projectTaskId = (Integer) models.get("projectTaskId");
		ProjectTask projectTask = new ProjectTask();
		projectTask.setId(projectTaskId);
		boolean flag = false;
		if (TaskIdentityEnum.COMPLETE.getIdentity().equals(identity)) {
			//完成任务
			flag = service.doComplete(projectTaskId, loginUserId);
		} else if (TaskIdentityEnum.CANCEL.getIdentity().equals(identity)) {
			//取消任务
			flag = service.doCancel(projectTaskId, loginUserId);
		}
		if (flag) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

	/**
	 * 批量删除任务
	 * @param models
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("protjectTask:delete")
	@ResponseBody
	public Result delete(@RequestBody Map<String, Object> models) throws SysException {
		String ids = (String) models.get("ids");
		if (!StringUtils.isNotBlank(ids)) {
			return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
		}
		if (service.logicalDelete(ids)) {
			return new Result(ResultEnum.SUCCESS);
		} else {
			return new Result(ResultEnum.FAIL);
		}
	}

}

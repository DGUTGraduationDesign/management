package cn.management.controller.attendance;

import cn.management.domain.attendance.WorkflowDeployment;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.attendance.WorkflowService;
import cn.management.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

/**
 * 工作流部署管理控制器
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
@Controller
@RequestMapping("attendance/workflow")
public class WorkflowController {

    static Logger logger = LoggerFactory.getLogger(WorkflowController.class);
    
    @Autowired
    private WorkflowService workflowService;
    
    @RequestMapping("/index")
    @RequiresPermissions("attendanceWorkflow:list")
    @ResponseBody
    public Result index() {
    	//查询部署对象信息，对应表（act_re_deployment）
		Set<WorkflowDeployment> list = workflowService.list();
		if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        }
    	return new Result(ResultEnum.SUCCESS, list);
    }
    
    /**
     * 流程部署
     * @param workflowDeployment
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("attendanceWorkflow:add")
    @ResponseBody
    public Result add(WorkflowDeployment workflowDeployment) throws SysException {
        MultipartFile file = workflowDeployment.getFile();
        String pdName = workflowDeployment.getPdName();
        //判断文件大小
        if (file == null || file.getSize() == 0) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！请选择要上传的文件！");
        }
        if (file.getSize() > (10 * 1024 * 1024)) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！文件大小不能超过10M！");
        }
    	if (null != workflowService.saveNewDeploye(file, pdName)) {
    		return new Result(ResultEnum.SUCCESS.getCode(), "流程部署成功.");
    	} else {
    		return new Result(ResultEnum.FAIL.getCode(), "流程部署失败.");
    	}
    }
    
    /**
     * 删除流程部署
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("attendanceWorkflow:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) {
    	String deploymentId = (String) models.get("deploymentId");
    	workflowService.deleteByDeploymentId(deploymentId);
    	return new Result(ResultEnum.SUCCESS.getCode(), "删除流程部署成功.");
    }


}

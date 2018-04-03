package cn.management.controller.attendance;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.management.domain.attendance.WorkflowDeployment;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.attendance.WorkflowService;
import cn.management.util.Result;

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
    @ResponseBody
    public Result index() {
    	//查询部署对象信息，对应表（act_re_deployment）
		Set<WorkflowDeployment> list = workflowService.list();
    	return new Result(ResultEnum.SUCCESS, list);
    }
    
    /**
     * 流程部署
     * @param models
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Map<String, Object> models) throws SysException {
    	String fileName = (String) models.get("fileName");
    	File file = (File) models.get("file");
    	if (null != workflowService.saveNewDeploye(file, fileName)) {
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
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) {
    	String deploymentId = (String) models.get("deploymentId");
    	workflowService.deleteByDeploymentId(deploymentId);
    	return new Result(ResultEnum.SUCCESS.getCode(), "删除流程部署成功.");
    }


}

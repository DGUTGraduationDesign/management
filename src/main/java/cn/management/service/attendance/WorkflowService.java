package cn.management.service.attendance;

import cn.management.domain.attendance.WorkflowDeployment;
import cn.management.exception.SysException;
import org.activiti.engine.repository.Deployment;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Set;

/**
 * 工作流部署管理Service接口
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
public interface WorkflowService {
	
	/**
     * 获取所有已部署流程定义的流程
     * @return
     * */
	Set<WorkflowDeployment> list();
    
	/**
	 * 流程部署定义
     * @param file
	 * @param pdName
	 * @throws SysException 
	 */
	Deployment saveNewDeploye(MultipartFile file, String pdName) throws SysException;
	
	/**
	 * 查看流程图
	 * @param deploymentId
	 * @param imageName
	 * @return
	 */
	InputStream findImageInputStream(String deploymentId, String imageName);
	
	/**
	 * 使用部署对象ID，删除流程定义
	 */
	void deleteByDeploymentId(String deploymentId);
	
}

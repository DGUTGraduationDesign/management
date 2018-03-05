package cn.management.service.attendance.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.management.domain.attendance.WorkflowDeployment;
import cn.management.exception.SysException;
import cn.management.service.attendance.WorkflowService;

/**
 * 工作流部署管理Service接口实现类
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private RepositoryService repositoryService;

	/**
     * 获取所有已部署流程定义的流程
     * @return
     */
	@Override
    public Set<WorkflowDeployment> list() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().desc().orderByProcessDefinitionVersion().desc().list();
        Set<WorkflowDeployment> beans = new HashSet<WorkflowDeployment>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        if(list!=null) {
            for (ProcessDefinition processDefinition : list) {
                WorkflowDeployment workflowDeployment = new WorkflowDeployment();
                Deployment singleResult = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId()).singleResult();
                workflowDeployment.setSid(processDefinition.getId());
                workflowDeployment.setKey(processDefinition.getKey());
                workflowDeployment.setPdName(processDefinition.getName());
                workflowDeployment.setSrTime(format.format(singleResult.getDeploymentTime()));
                workflowDeployment.setVersion(processDefinition.getVersion() + "");
                workflowDeployment.setDeployment_id(processDefinition.getDeploymentId());
                beans.add(workflowDeployment);
            }
        }
        return beans;
    }
	
	/**
	 * 流程部署定义
	 * @param file
	 * @param fileName
	 * @throws SysException 
	 */
	@Override
	public Deployment saveNewDeploye(File file, String fileName) throws SysException {
		//将File类型的文件转化成ZipInputStream流
		ZipInputStream zipInputStream;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(file));
			return repositoryService.createDeployment()
						.name(fileName)
						.addZipInputStream(zipInputStream)
						.deploy();
		} catch (FileNotFoundException e) {
			throw new SysException("流程部署失败.");
		}
	}
	
	/**
	 * 查看流程图
	 * @param deploymentId
	 * @param imageName
	 * @return
	 */
	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}

	/**
	 * 使用部署对象ID，删除流程定义
	 */
	@Override
	public void deleteByDeploymentId(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}

}
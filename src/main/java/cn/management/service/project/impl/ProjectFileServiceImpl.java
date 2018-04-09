package cn.management.service.project.impl;

import cn.management.domain.project.ProjectFile;
import cn.management.mapper.project.ProjectFileMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectFileService;
import org.springframework.stereotype.Service;

/**
 * 资源文件Service层实现类
 */
@Service
public class ProjectFileServiceImpl extends BaseServiceImpl<ProjectFileMapper, ProjectFile> implements ProjectFileService {

}

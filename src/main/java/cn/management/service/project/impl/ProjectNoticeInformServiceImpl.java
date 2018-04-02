package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectItem;
import cn.management.domain.project.ProjectNoticeInform;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ItemStateEnum;
import cn.management.mapper.project.ProjectItemMapper;
import cn.management.mapper.project.ProjectNoticeInformMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectNoticeInformService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 通知知会人Service层实现类
 */
@Service
public class ProjectNoticeInformServiceImpl extends BaseServiceImpl<ProjectNoticeInformMapper, ProjectNoticeInform> implements ProjectNoticeInformService {

}

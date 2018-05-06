package cn.management.service.project.impl;

import cn.management.domain.project.ProjectNoticeInform;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.NoticeReadEnum;
import cn.management.mapper.project.ProjectNoticeInformMapper;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectNoticeInformService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * 通知知会人Service层实现类
 */
@Service
public class ProjectNoticeInformServiceImpl extends BaseServiceImpl<ProjectNoticeInformMapper, ProjectNoticeInform> implements ProjectNoticeInformService {

    /**
     * 标记为已读
     * @param projectNoticeId
     * @param loginUserId
     */
    public boolean updateReadFlag(Integer projectNoticeId, Integer loginUserId) {
        Example example = new Example(ProjectNoticeInform.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("noticeId", projectNoticeId);
        criteria.andEqualTo("userId", loginUserId);
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        ProjectNoticeInform projectNoticeInform = new ProjectNoticeInform();
        projectNoticeInform.setReadFlag(NoticeReadEnum.READ.getValue());
        return updateByExampleSelective(projectNoticeInform, example);
    }

    @Override
    public boolean logicalDeleteByNoticeId(String ids) {
        Example infromExample = new Example(ProjectNoticeInform.class);
        infromExample.createCriteria().andCondition("notice_id IN(" + ids + ")");
        ProjectNoticeInform projectNoticeInform = new ProjectNoticeInform();
        projectNoticeInform.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        updateByExampleSelective(projectNoticeInform, infromExample);
        return true;
    }

}

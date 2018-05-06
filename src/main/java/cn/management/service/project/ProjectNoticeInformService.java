package cn.management.service.project;

import cn.management.domain.project.ProjectNoticeInform;
import cn.management.mapper.project.ProjectNoticeInformMapper;
import cn.management.service.BaseService;

/**
 * 通知知会人Service层接口
 */
public interface ProjectNoticeInformService extends BaseService<ProjectNoticeInformMapper, ProjectNoticeInform> {

    /**
     * 标记为已读
     * @param projectNoticeId
     * @param loginUserId
     */
    boolean updateReadFlag(Integer projectNoticeId, Integer loginUserId);

    /**
     * 根据通知id删除通知知会人关联信息，逻辑删除
     * @param ids
     * @param loginId
     * @return
     */
    boolean logicalDeleteByNoticeId(String ids, Integer loginId);

}

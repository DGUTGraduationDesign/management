package cn.management.service.project;

import cn.management.domain.project.ProjectNotice;
import cn.management.mapper.project.ProjectNoticeMapper;
import cn.management.service.BaseService;

/**
 * 通知Service层接口
 */
public interface ProjectNoticeService extends BaseService<ProjectNoticeMapper, ProjectNotice> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}

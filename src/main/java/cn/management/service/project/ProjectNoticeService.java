package cn.management.service.project;

import cn.management.domain.project.ProjectNotice;
import cn.management.domain.project.dto.ProjectNoticeDto;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectNoticeMapper;
import cn.management.service.BaseService;

import java.util.List;

/**
 * 通知Service层接口
 */
public interface ProjectNoticeService extends BaseService<ProjectNoticeMapper, ProjectNotice> {

    /**
     * 分页查询
     * @param projectNoticeDto
     * @param page
     * @param pageSize
     * @return
     */
    List<ProjectNotice> getItemsByPage(ProjectNoticeDto projectNoticeDto, int page, int pageSize, String identity);

    /**
     * 新增通知
     * @param projectNotice
     * @return
     */
    boolean doAdd(ProjectNotice projectNotice) throws SysException;

    /**
     * 修改通知
     * @param projectNotice
     * @return
     */
    boolean doUpdate(ProjectNotice projectNotice) throws SysException;

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}

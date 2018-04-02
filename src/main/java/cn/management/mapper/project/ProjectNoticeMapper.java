package cn.management.mapper.project;

import cn.management.domain.project.ProjectNotice;
import cn.management.domain.project.dto.ProjectNoticeDto;
import cn.management.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知Mapper
 */
public interface ProjectNoticeMapper extends MyMapper<ProjectNotice> {

    /**
     * 条件查询通知信息
     * @param projectNoticeDto
     * @return
     */
    List<ProjectNotice> getItemsByCondition(ProjectNoticeDto projectNoticeDto);

    /**
     * 查询是否已读
     * @param noticeId
     * @param userId
     * @return
     */
    Integer getReadFlagById(@Param("noticeId") Integer noticeId, @Param("userId") Integer userId);

    /**
     * 根据id查询通知详细信息
     * @param projectNoticeId
     * @return
     */
    ProjectNotice getProjectNoticeById(Integer projectNoticeId);

}

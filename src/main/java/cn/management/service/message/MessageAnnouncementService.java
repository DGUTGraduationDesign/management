package cn.management.service.message;

import cn.management.domain.message.MessageAnnouncement;
import cn.management.mapper.message.MessageAnnouncementMapper;
import cn.management.service.BaseService;

/**
 * 企业公告Service层接口
 */
public interface MessageAnnouncementService extends BaseService<MessageAnnouncementMapper, MessageAnnouncement> {

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    boolean logicalDelete(String ids);

}

package cn.management.service.message.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.message.MessageAnnouncement;
import cn.management.enums.DeleteTypeEnum;
import cn.management.mapper.message.MessageAnnouncementMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.message.MessageAnnouncementService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 企业公告Service层实现类
 */
@Service
public class MessageAnnouncementServiceImpl extends BaseServiceImpl<MessageAnnouncementMapper, MessageAnnouncement> implements MessageAnnouncementService {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 条件查询企业公告列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<MessageAnnouncement> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<MessageAnnouncement> list = mapper.selectByExample(example);
        //设置创建人姓名
        for (MessageAnnouncement messageAnnouncement : list) {
            AdminUser createUser = adminUserService.getItemById(messageAnnouncement.getCreateBy());
            if (null != createUser) {
                messageAnnouncement.setCreateName(createUser.getRealName());
            }
        }
        return list;
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(MessageAnnouncement.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        MessageAnnouncement messageAnnouncement = new MessageAnnouncement();
        messageAnnouncement.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(messageAnnouncement, example);
    }

}

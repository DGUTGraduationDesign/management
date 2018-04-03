package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectNotice;
import cn.management.domain.project.ProjectNoticeInform;
import cn.management.domain.project.dto.ProjectNoticeDto;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.InformWayEnum;
import cn.management.enums.NoticeIdentityEnum;
import cn.management.enums.NoticeReadEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectNoticeMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import cn.management.service.project.ProjectNoticeInformService;
import cn.management.service.project.ProjectNoticeService;
import cn.management.util.SendProjectInformUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectNoticeServiceImpl extends BaseServiceImpl<ProjectNoticeMapper, ProjectNotice> implements ProjectNoticeService {

    private final static Logger logger =  LoggerFactory.getLogger(ProjectNoticeServiceImpl.class);

    @Autowired
    private ProjectNoticeInformService projectNoticeInformService;
    @Autowired
    private AdminUserService adminUserService;

    public List<ProjectNotice> getItemsByPage(ProjectNoticeDto projectNoticeDto, int page, int pageSize, String identity) {
        PageHelper.startPage(page, pageSize);
        List<ProjectNotice> list = mapper.getItemsByCondition(projectNoticeDto);
        //我的通知，显示是否已读、发布人
        if (NoticeIdentityEnum.SELF.getIdentity().equals(identity)) {
            for (ProjectNotice projectNotice : list) {
                //设置是否读取中文名
                Integer readFlag = mapper.getReadFlagById(projectNotice.getId(), projectNoticeDto.getUserId());
                projectNotice.setReadFlagName(NoticeReadEnum.getName(readFlag));
                //设置发布人
                AdminUser createUser  = adminUserService.getItemById(projectNotice.getCreateBy());
                projectNotice.setCreateName(createUser.getRealName());
            }
        }
        return list;
    }

    @Override
    public ProjectNotice getItemById(Object projectNoticeId) {
        ProjectNotice projectNotice = mapper.getProjectNoticeById((Integer)projectNoticeId);
        if (null != projectNotice) {
            //设置发布人
            AdminUser createUser  = adminUserService.getItemById(projectNotice.getCreateBy());
            projectNotice.setCreateName(createUser.getRealName());
            //设置知会人
            for (ProjectNoticeInform projectNoticeInform : projectNotice.getInformList()) {
                AdminUser user = adminUserService.getItemById(projectNoticeInform.getUserId());
                projectNoticeInform.setUserName(user.getRealName());
            }
        }
        return projectNotice;
    }

    @Override
    @Transactional
    public boolean doAdd(ProjectNotice projectNotice) throws SysException {
        //添加通知
        projectNotice.setCreateTime(new Date());
        projectNotice.setUpdateTime(new Date());
        addSelectiveMapper(projectNotice);
        //添加通知知会人
        List<ProjectNoticeInform> list = projectNotice.getInformList();
        for (ProjectNoticeInform projectNoticeInform : list) {
            if (null == adminUserService.getItemById(projectNoticeInform.getUserId())) {
                throw new SysException("所选知会人不存在.");
            }
            projectNoticeInform.setNoticeId(projectNotice.getId());
            projectNoticeInform.setCreateTime(new Date());
            projectNoticeInform.setUpdateTime(new Date());
            projectNoticeInformService.addSelectiveMapper(projectNoticeInform);
        }

        //判断是否需要发送通知
        Integer informWay = projectNotice.getInformWay();
        if (!InformWayEnum.NONE.getValue().equals(informWay)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //知会人
                    List<AdminUser> users = new ArrayList<AdminUser>();
                    List<ProjectNoticeInform> list = projectNotice.getInformList();
                    for (ProjectNoticeInform projectNoticeInform : list) {
                        AdminUser user = adminUserService.getItemById(projectNoticeInform.getUserId());
                        if (null != user) {
                            users.add(user);
                        }
                    }
                    //发布人
                    AdminUser createUser = adminUserService.getItemById(projectNotice.getCreateBy());
                    projectNotice.setCreateName(createUser.getRealName());
                    try {
                        projectNotice.setId(null);
                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送邮件通知
                            SendProjectInformUtil.sendProjectNoticeMail(projectNotice, users);
                        }
                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送短信通知
                            SendProjectInformUtil.sendProjectNoticeMessage(projectNotice, users);
                        }
                    } catch (Exception e) {
                        logger.error(projectNotice.toString(), e);
                    }
                }
            }).start();
        }

        return true;
    }

    @Override
    public boolean doUpdate(ProjectNotice projectNotice) throws SysException {
        if (null == mapper.getProjectNoticeById(projectNotice.getId())) {
            throw new SysException("所编辑对象不存在.");
        }
        update(projectNotice);

        //判断是否需要发送通知
        Integer informWay = projectNotice.getInformWay();
        if (!InformWayEnum.NONE.getValue().equals(informWay)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //知会人
                    List<AdminUser> users = new ArrayList<AdminUser>();
                    List<ProjectNoticeInform> list = projectNotice.getInformList();
                    for (ProjectNoticeInform projectNoticeInform : list) {
                        AdminUser user = adminUserService.getItemById(projectNoticeInform.getUserId());
                        if (null != user) {
                            users.add(user);
                        }
                    }
                    //发布人
                    AdminUser createUser = adminUserService.getItemById(projectNotice.getCreateBy());
                    projectNotice.setCreateName(createUser.getRealName());
                    try {
                        projectNotice.setId(null);
                        if (InformWayEnum.MAIL.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送邮件通知
                            SendProjectInformUtil.sendProjectNoticeMail(projectNotice, users);
                        }
                        if (InformWayEnum.MESSAGE.getValue().equals(informWay) || InformWayEnum.ALL.getValue().equals(informWay)) {
                            //发送短信通知
                            SendProjectInformUtil.sendProjectNoticeMessage(projectNotice, users);
                        }
                    } catch (Exception e) {
                        logger.error(projectNotice.toString(), e);
                    }
                }
            }).start();
        }

        return true;
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectNotice.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectNotice projectNotice = new ProjectNotice();
        projectNotice.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectNotice, example);
    }

}

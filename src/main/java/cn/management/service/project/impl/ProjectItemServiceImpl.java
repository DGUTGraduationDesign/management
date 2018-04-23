package cn.management.service.project.impl;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.project.ProjectItem;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ItemStateEnum;
import cn.management.exception.SysException;
import cn.management.mapper.project.ProjectItemMapper;
import cn.management.service.admin.AdminUserService;
import cn.management.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 项目Service层实现类
 */
@Service
public class ProjectItemServiceImpl extends BaseServiceImpl<ProjectItemMapper, ProjectItem> implements cn.management.service.project.ProjectItemService {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 条件查询项目列表
     * @param example
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<ProjectItem> getItemsByPage(Example example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ProjectItem> list = mapper.selectByExample(example);
        for (ProjectItem projectItem : list) {
            //设置项目状态名称、负责人姓名、创建人姓名
            setName(projectItem);
        }
        return list;
    }

    /**
     * 设置项目状态名称、负责人姓名、创建人姓名
     * @param projectItem
     */
    public void setName(ProjectItem projectItem) {
        //状态名称
        projectItem.setItemStateName(ItemStateEnum.getName(projectItem.getItemState()));
        //负责人
        AdminUser mainUser = adminUserService.getItemById(projectItem.getMainId());
        //创建人
        AdminUser createUser = adminUserService.getItemById(projectItem.getCreateBy());
        if (null != mainUser) {
            projectItem.setMainName(mainUser.getRealName());
        }
        if (null != createUser) {
            projectItem.setCreateName(createUser.getRealName());
        }
    }

    /**
     * 修改项目信息
     * @param projectItem
     * @param loginUserId
     * @return
     * @throws SysException
     */
    @Override
    public boolean doUpdate(ProjectItem projectItem, Integer loginUserId) throws SysException {
        ProjectItem findItem = getItemById(projectItem.getId());
        if (null == findItem) {
            throw new SysException("请选择要编辑的数据.");
        }
        if (!(loginUserId.equals(findItem.getCreateBy()) || loginUserId.equals(findItem.getMainId()))) {
            throw new SysException("只能由创建人或负责人修改.");
        }
        if (ItemStateEnum.FINISHED.getValue().equals(projectItem.getItemState())) {
            if (!findItem.getItemTask().equals(findItem.getCompleteTask())) {
                throw new SysException("还有任务未完成.");
            }
            projectItem.setEndDate(new Date());
        }
        return update(projectItem);
    }

    /**
     * 逻辑删除，更新表中del_flag字段为1
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean logicalDelete(String ids) {
        Example example = new Example(ProjectItem.class);
        example.createCriteria().andCondition("id IN(" + ids + ")");
        ProjectItem projectItem = new ProjectItem();
        projectItem.setDelFlag(DeleteTypeEnum.DELETED_TRUE.getVal());
        return updateByExampleSelective(projectItem, example);
    }

}

package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectItem;
import cn.management.domain.project.ProjectNotice;
import cn.management.domain.project.dto.ProjectNoticeDto;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.NoticeIdentityEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.project.ProjectItemService;
import cn.management.service.project.ProjectNoticeService;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目通知控制器
 * @author ZhouJiaKai
 * @date 2018-03-21
 */
@Controller
@RequestMapping("project/notice")
public class ProjectNoticeController extends BaseController<ProjectNoticeService, ProjectNotice> {

    /**
     * 条件查询通知列表
     * @param models
     * @return
     */
    @RequestMapping("/{identity}/index")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models, HttpServletRequest request, @PathVariable String identity) {
        // 检查权限
        SecurityUtils.getSubject().checkPermission(NoticeIdentityEnum.getPermission(identity));
        // 拼接条件
        Integer readFlag = JSON.parseObject((String)models.get("readFlag"), Integer.class);
        Integer page = (Integer) models.get("page");
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        ProjectNoticeDto projectNoticeDto = new ProjectNoticeDto();
        if (NoticeIdentityEnum.SELF.getIdentity().equals(identity)) {
            projectNoticeDto.setUserId(loginId);
        }
        if (NoticeIdentityEnum.PUBLISH.getIdentity().equals(identity)) {
            projectNoticeDto.setCreateBy(loginId);
        }
        if (null != readFlag) {
            projectNoticeDto.setReadFlag(readFlag);
        }
        List<ProjectNotice> list = service.getItemsByPage(projectNoticeDto, page, getPageSize(), identity);
        if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        }
        PageInfo<ProjectNotice> pageInfo = new PageInfo<ProjectNotice>(list);
        return new Result(ResultEnum.SUCCESS, pageInfo.getList(), (int) pageInfo.getTotal(), pageInfo.getPageNum(), getPageSize());
    }

    /**
     * 查询通知详细信息
     * @param models
     * @return
     */
    @RequestMapping("/findProjectNoticeById")
    @ResponseBody
    public Result findProjectNoticeById(@RequestBody Map<String, Object> models) {
        Integer projectNoticeId = (Integer) models.get("projectNoticeId");
        ProjectNotice projectNotice = service.getItemById(projectNoticeId);
        if (null == projectNotice) {
            return new Result(ResultEnum.NO_RECORDS);
        } else {
            return new Result(ResultEnum.SUCCESS, projectNotice, 1, 0, 1);
        }
    }

    /**
     * 添加通知
     * @param projectNotice
     * @param request
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("projectNotice:add")
    @ResponseBody
    public Result add(@RequestBody ProjectNotice projectNotice, HttpServletRequest request) throws SysException {
        Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        projectNotice.setCreateBy(loginUserId);
        projectNotice.setCreateTime(new Date());
        projectNotice.setUpdateTime(new Date());
        if (service.doAdd(projectNotice)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 更改通知
     * @param projectNotice
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("projectNotice:edit")
    @ResponseBody
    public Result edit(@RequestBody ProjectNotice projectNotice) throws SysException {
        projectNotice.setUpdateTime(new Date());
        if (service.doUpdate(projectNotice)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 批量删除通知
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("projectNotice:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models) {
        String ids = (String) models.get("ids");
        if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        if (service.logicalDelete(ids)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}

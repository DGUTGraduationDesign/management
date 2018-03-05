package cn.management.controller.admin;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminDataDict;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminDataDictService;
import cn.management.util.Result;
import tk.mybatis.mapper.entity.Example;

/**
 * 数据字典控制器
 * @author ZhouJiaKai
 * @date 2018-03-05
 */

@Controller
@RequestMapping("admin/dataDict")
public class AdminDataDictController extends BaseController<AdminDataDictService, AdminDataDict> {
	
	/**
     * 条件数据字典列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    //@RequiresPermissions("adminDataDict:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
    	AdminDataDict adminDataDict = JSON.parseObject((String)models.get("dataDict"), AdminDataDict.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminDataDict.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(adminDataDict.getDictDesc())) {
            criteria.andLike("dictDesc", "%" + adminDataDict.getDictDesc() + "%");
        }
        if (StringUtils.isNotBlank(adminDataDict.getDictKey())) {
            criteria.andLike("dictKey", "%" + adminDataDict.getDictKey() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }
    
    /**
     * 添加数据字典
     * @param adminDataDict
     * @return
     * @throws SysException 
     */
    @RequestMapping("/add")
    //@RequiresPermissions("adminDataDict:add")
    @ResponseBody
    public Result add(@RequestBody AdminDataDict adminDataDict) throws SysException {
    	adminDataDict.setCreateTime(new Date()); 
    	adminDataDict.setUpdateTime(new Date()); 
        if (null == service.addSelectiveMapper(adminDataDict)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 更改数据字典
     * @param adminDataDict
     * @return
     * @throws SysException 
     */
    @RequestMapping("/edit")
    //@RequiresPermissions("adminDataDict:edit")
    @ResponseBody
    public Result edit(@RequestBody AdminDataDict adminDataDict) throws SysException {
    	adminDataDict.setUpdateTime(new Date());
        if (service.update(adminDataDict)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }
    
    /**
     * 批量删除数据字典
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("adminDataDict:delete")
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

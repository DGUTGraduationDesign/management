package cn.management.controller.message;

import cn.management.controller.BaseController;
import cn.management.domain.admin.AdminUser;
import cn.management.enums.DeleteTypeEnum;
import cn.management.service.admin.AdminUserService;
import cn.management.util.DownloadUtil;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 通讯录控制器
 */
@Controller
@RequestMapping("message/information")
public class InformationController extends BaseController<AdminUserService, AdminUser> {

    /**
     * 条件查询通讯录列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("messageInformation:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        AdminUser user = JSON.parseObject((String)models.get("user"), AdminUser.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(AdminUser.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(user.getRealName())) {
            criteria.andLike("realName", "%" + user.getRealName() + "%");
        }
        if (StringUtils.isNotBlank(user.getNumber())) {
            criteria.andLike("number", "%" + user.getNumber() + "%");
        }
        if (null != user.getDeptId()) {
            criteria.andEqualTo("deptId", user.getDeptId());
        }
        if (null != user.getPostId()) {
            criteria.andEqualTo("postId", user.getPostId());
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 导出通讯录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/export")
    @RequiresPermissions("messageInformation:export")
    public String export(AdminUser condition, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //通用变量
        int rowNo = 0, cellNo = 1;
        Row nRow = null;
        Cell nCell = null;
        //1.读取工作簿
        String path = request.getSession().getServletContext().getRealPath("/") + "/xlsprint/tOUTPRODUCT.xls";
        InputStream is = new FileInputStream(path);
        Workbook wb = new HSSFWorkbook(is);
        //2.读取工作表
        Sheet sheet = wb.getSheetAt(0);
        //3.创建行对象
        //=========================================标题=============================
        //读取行对象
        nRow = sheet.getRow(rowNo++);
        nCell = nRow.getCell(cellNo);
        //设置单元格的内容
        nCell.setCellValue("通讯录");
        rowNo++;
        //=======================================数据输出=================================================
        nRow = sheet.getRow(rowNo);		//读取第三行
        CellStyle numberCellStyle = nRow.getCell(cellNo).getCellStyle();
        CellStyle nameCellStyle = nRow.getCell(cellNo++).getCellStyle();
        CellStyle sexCellStyle = nRow.getCell(cellNo++).getCellStyle();
        CellStyle deptCellStyle = nRow.getCell(cellNo++).getCellStyle();
        CellStyle postCellStyle = nRow.getCell(cellNo++).getCellStyle();
        CellStyle phoneCellStyle = nRow.getCell(cellNo++).getCellStyle();
        CellStyle mailCellStyle = nRow.getCell(cellNo++).getCellStyle();

        StringBuffer str = new StringBuffer();
        str.append("del_flag = 1");
        if (StringUtils.isNotBlank(condition.getRealName())) {
            str.append(" and real_name like '%" + condition.getRealName() + "%'");
        }
        if (null != condition.getNumber()) {
            str.append(" and number like '%" + condition.getNumber() + "%'");
        }
        if (null != condition.getDeptId()) {
            str.append(" and dept_id = "+ condition.getDeptId());
        }
        if (null != condition.getPostId()) {
            str.append(" and post_id = " + condition.getPostId());
        }
        List<AdminUser> userList = service.getByCondition(str.toString());
        for(AdminUser user :userList){
            nRow = sheet.createRow(rowNo++);	//产生数据行
            nRow.setHeightInPoints(24); 		//设置行高
            cellNo = 1;

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getNumber());	        //工号
            nCell.setCellStyle(numberCellStyle);		    //设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getRealName());	        //姓名
            nCell.setCellStyle(nameCellStyle);			    //设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getSex());	            //性别
            nCell.setCellStyle(sexCellStyle);		        //设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getDeptName());		    //部门
            nCell.setCellStyle(deptCellStyle);			    //设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getPostName());			//岗位
            nCell.setCellStyle(postCellStyle);				//设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getPhone());	        //手机号
            nCell.setCellStyle(phoneCellStyle);			    //设置文本样式

            nCell = nRow.createCell(cellNo++);				//产生单元格对象
            nCell.setCellValue(user.getMail());	        	//邮箱
            nCell.setCellStyle(mailCellStyle);			    //设置文本样式
        }
        //======================================输出到客户端（下载）========================================
        DownloadUtil downloadUtil = new DownloadUtil();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//内存中的缓存区
        wb.write(baos);//将excel表格中的内容输出到缓存
        baos.close();//刷新缓存
        downloadUtil.download(baos, request, response, "通讯录.xls");
        return null;
    }

}

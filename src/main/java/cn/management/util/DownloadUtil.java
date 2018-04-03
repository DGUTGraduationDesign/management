package cn.management.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * 下载工具类
 * @author ZhouJiaKai
 * @date 2018-02-21
 */
public class DownloadUtil {
    
    /**
     * 文件下载
     * @param byteArrayOutputStream
     * @param request
     * @param response
     * @param returnName
     * @throws IOException
     */
    public static void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletRequest request, HttpServletResponse response, String returnName) throws IOException{
        //通知浏览器以下载方式打开
        response.setContentType("application/octet-stream;charset=utf-8");
        String agent = request.getHeader("user-agent");
        returnName = encodeDownloadFilename(returnName, agent);
        response.addHeader("Content-Disposition", "attachment;filename=" + returnName);  
        response.setContentLength(byteArrayOutputStream.size());
        //取得输出流
        ServletOutputStream outputstream = response.getOutputStream();
        //写到输出流
        byteArrayOutputStream.writeTo(outputstream);
        //关闭
        byteArrayOutputStream.close();
        //刷数据
        outputstream.flush();
    }
    
    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     * @param fileName
     * @param agent
     * @return
     * @throws IOException
     */
    public static String encodeDownloadFilename(String fileName, String agent) throws IOException{
        if(agent.contains("Firefox")){
            //火狐浏览器
            fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?="; 
        }else{
            //IE及其他浏览器
            fileName = URLEncoder.encode(fileName, "utf-8");
        }
        return fileName;
    }
    
}

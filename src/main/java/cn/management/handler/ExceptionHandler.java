package cn.management.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.management.enums.ResultEnum;
import cn.management.util.Result;

/**
 * 全局异常处理器
 * @author ZhouJiaKai
 * @date 2018-02-23
 */
@ControllerAdvice
public class ExceptionHandler {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage(), e);
        return new Result(ResultEnum.UNKONW_ERROR);
    }
    
}

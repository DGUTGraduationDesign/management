package cn.management.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
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
    	if (e instanceof SysException) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), e.getMessage());
        }
    	if (e instanceof UnauthorizedException) {
            return new Result(ResultEnum.NO_AUTHORITY);
        }
        if (e instanceof UnauthenticatedException) {
    	    return new Result(ResultEnum.NO_LOGIN);
        }
        return new Result(ResultEnum.UNKONW_ERROR);
    }
    
}

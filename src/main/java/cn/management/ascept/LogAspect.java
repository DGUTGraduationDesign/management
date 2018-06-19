package cn.management.ascept;

import cn.management.annotation.OptLog;
import cn.management.domain.admin.AdminUser;
import cn.management.domain.log.OperationLog;
import cn.management.enums.ResultEnum;
import cn.management.service.admin.AdminUserService;
import cn.management.service.log.OperationLogService;
import cn.management.util.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 操作日志切面类
 * package: cn.management.ascept
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.0
 * <p>
 * date 2018/6/19 9:23
 */
@Aspect
@Component
public class LogAspect {

    @Resource
    private OperationLogService operationLogService;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //声明切入点
    @Pointcut(value = "execution(* cn.management.controller..*.*(..)) && @annotation(optLog)")
    public void logAspect(OptLog optLog) {}

    /**
     * 日志记录
     * value：指定切入点表达式或命名切入点；
     * argNames：指定命名切入点方法参数列表参数名字，可以有多个用“，”分隔，这些参数将传递给通知方法同名的参数。
     * @param joinPoint
     * @param optLog
     */
    @Around(value = "logAspect(optLog)", argNames = "joinPoint, optLog")
    public Object doLog(ProceedingJoinPoint joinPoint, OptLog optLog) throws Throwable {

        //调用目标方法
        Object result = joinPoint.proceed();
        //获取切面方法名称
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //获取类的简称
        String action = joinPoint.getTarget().getClass().getSimpleName();
        //获取切面方法中的所有参数
        Object[] args = joinPoint.getArgs();
        //获取切面方法所有的参数名称
        String[] parameterNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        //获取切面方法描述信息
        String msg = parserMethodMsg(parameterNames,args,optLog.msg());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AdminUser user =  (AdminUser) request.getSession().getAttribute(AdminUserService.LOGIN_USER_SESSION_KEY);

        // 前端独立的请求可能无法获取到session
        if(null == user){
            return result;
        }

        // 执行成功的方法才添加日志
        if(result instanceof Result){
            Result resultVo = (Result) result;
            if(Integer.valueOf(resultVo.getCode()).equals(ResultEnum.SUCCESS.getCode())){
                //实例化 操作日志对象
                OperationLog operationLog = new OperationLog();
                operationLog.setActionName(action);
                //operationLog.setIp(IPUtil.getIp(request));
                operationLog.setMethod(method.getName());
                operationLog.setUserId(user.getId());
                operationLog.setUserName(user.getRealName());
                operationLog.setCreateTime(new Date());
                operationLog.setUpdateTime(new Date());
                operationLog.setOperation(msg);
                try {
                    operationLogService.addSelectiveMapper(operationLog);
                }catch (Exception e){
                    logger.error("写入操作日志失败！",e);
                }
            }
        }
        return result;
    }

    /**
     * 解析注解的信息，example {company.id}
     *
     * @param parameterNames
     * @param args
     * @param msg
     * @return
     */
    private String parserMethodMsg(String[] parameterNames, Object[] args, String msg) {

        Map<String, Object> params = new HashMap<>();
        // 获取匹配的日志正则list
        List<String> msgExpressionList = getMsgExpressionList(msg);

        //验证参数名称及参数对象是否为空
        if(ArrayUtils.isNotEmpty(parameterNames) && ArrayUtils.isNotEmpty(args)) {
            Assert.isTrue(parameterNames.length == args.length, "参数名称跟参数对象不一致");
            for(int i = 0; i < parameterNames.length; i++) {
                params.put(parameterNames[i], args[i]);
            }
        }

        //验证表达式列表是否为空
        if(CollectionUtils.isNotEmpty(msgExpressionList)) {
            //循环解析表达式数据
            for(String expression : msgExpressionList) {
                String expressionValue = getExpressionValue(expression, params);
                if(expressionValue == null) {
                    expressionValue = "";
                }
                msg = msg.replaceAll("\\{" + expression + "\\}", expressionValue);
            }
        }

        return msg;
    }

    /**
     * 从方法描述中提取表达式列表 {expression}
     * @param desc
     * @return
     */
    private List<String> getMsgExpressionList(String desc) {
        List<String> expressionList = new ArrayList<>();
        //使用正则表达式提取方法描述中的表达式参数信息
        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(desc);
        while(m.find()){
            expressionList.add(m.group(1));
        }
        return expressionList;
    }

    /**
     * 通过表达式获取数据值
     * @param expression
     * @param params
     * @return
     */
    private String getExpressionValue(String expression, Map<String, Object> params) {
        //表达式参数名称
        String paramName = expression;
        String paramValue = StringUtils.EMPTY;
        //判断是否为复杂类型的参数名称
        if(expression.contains(".")) {
            String[] nameValue = expression.split("\\.");
            paramName = nameValue[0];
            paramValue = nameValue[1];
        }

        Assert.isTrue(StringUtils.isNotBlank(paramName), "表达式参数名称错误");
        //判断参数映射表中是否存在该参数对象
        if(params.containsKey(paramName)) {
            Object param = params.get(paramName);
            //判断是否为复杂类型数据
            if(StringUtils.isNotBlank(paramValue)) {
                if(param != null) {
                    try {
                        //通过BeanUtils获取表达式中的值
                        return org.apache.commons.beanutils.BeanUtils.getProperty(param ,paramValue);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return String.valueOf(param);
            }
        }
        return "";
    }

}

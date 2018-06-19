package cn.management.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Web上下文，保存所有request与response
 * 切面使用可以获取session相关的信息
 * package: cn.management.util
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.0
 * <p>
 * date 2018/6/19 10:26
 */
public class SysContextUtils {
    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestLocal.set(request);
    }

    public static HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseLocal.set(response);
    }

    public static HttpSession getSession(){
        return requestLocal.get().getSession();
    }

    /**
     * 清除配置相关变量
     */
    public static void clear() {
        requestLocal.remove();
        responseLocal.remove();
    }
}


package cn.management.domain.log;

import cn.management.domain.BaseEntity;

import javax.persistence.Table;

/**
 * <p>
 * 日志记录实体类
 * package: cn.management.domain.log
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.0
 * <p>
 * date 2018/6/19 9:29
 */
@Table(name = "operation_log")
public class OperationLog extends BaseEntity<Integer> {

    /** 用户Id */
    private Integer userId;

    /** 用户姓名 */
    private String userName;

    /** 操作控制器 */
    private String actionName;

    /** 操作方法 */
    private String method;

    /** 操作内容 */
    private String operation;

    /** ip */
    private String ip;

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getActionName() {
        return actionName;
    }

    public String getMethod() {
        return method;
    }

    public String getOperation() {
        return operation;
    }

    public String getIp() {
        return ip;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", actionName='" + actionName + '\'' +
                ", method='" + method + '\'' +
                ", operation='" + operation + '\'' +
                ", ip='" + ip + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }
}

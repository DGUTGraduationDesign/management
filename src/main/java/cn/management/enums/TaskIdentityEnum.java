package cn.management.enums;

/**
 * 任务列表页面 身份 权限 枚举
 * @author ZhouJiaKai
 * @since  2018/03/01
 */
public enum TaskIdentityEnum {

    /**
     * 我的任务
     */
    SELF("self","projectTask:self","我的任务"),
    /**
     * 发布的任务
     */
    PUBLISH("publish","projectTask:publish","发布的任务"),
    /**
     * 完成任务
     */
    COMPLETE("complete","projectTask:complete","完成任务"),
    /**
     * 取消任务
     */
    CANCEL("cancel","projectTask:cancel","取消任务");

    /**
     * 身份
     */
    private String identity;

    /**
     * 权限
     */
    private String permission;

    /**
     * 身份中文
     */
    private String identityCn;

    TaskIdentityEnum(String identity, String permission, String identityCn) {
        this.identity = identity;
        this.permission = permission;
        this.identityCn = identityCn;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPermission() {
        return permission;
    }

    public String getIdentityCn() {
        return identityCn;
    }

    /**
     * 根据 identity 获取 permission
     * @param identity
     * @return
     */
    public static String getPermission(String identity) {
        for (TaskIdentityEnum groupEnum : TaskIdentityEnum.values()) {
            if(groupEnum.getIdentity().equals(identity)) {
                return  groupEnum.getPermission();
            }
        }
        return null;
    }

}

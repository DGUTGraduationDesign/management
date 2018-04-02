package cn.management.enums;

/**
 * 考勤申请表页面 身份 权限 枚举
 * @author ZhouJiaKai
 * @since  2018/03/21
 */
public enum NoticeIdentityEnum {

    /**
     * 我的通知
     */
    SELF("self","projectNotice:self", "我的通知"),
    /**
     * 发布的通知
     */
    PUBLISH("publish", "projectNotice:publish", "发布的通知");

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

    NoticeIdentityEnum(String identity, String permission, String identityCn) {
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
        for (NoticeIdentityEnum groupEnum : NoticeIdentityEnum.values()) {
            if(groupEnum.getIdentity().equals(identity)) {
                return  groupEnum.getPermission();
            }
        }
        return null;
    }

}

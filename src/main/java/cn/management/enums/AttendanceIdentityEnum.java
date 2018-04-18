package cn.management.enums;

/**
 * 考勤申请表页面 身份 权限 枚举
 * @author ZhouJiaKai
 * @since  2018/03/01
 */
public enum AttendanceIdentityEnum {
	
    /**
     * 个人考勤申请
     */
    SELF("self","attendanceApplication:self","个人"),
    /**
     * 直接上级审核
     */
    LEAD("lead","attendanceApplication:leader","直接上级"),
    /**
     * 部门总监审核
     */
    HEAD("head","attendanceApplication:header","部门总监"),
    /**
     * 申请记录汇总
     */
    HR("all","attendanceApplication:all","申请汇总");

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

    AttendanceIdentityEnum(String identity, String permission, String identityCn) {
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
        for (AttendanceIdentityEnum groupEnum : AttendanceIdentityEnum.values()) {
            if(groupEnum.getIdentity().equals(identity)) {
                return  groupEnum.getPermission();
            }
        }
        return null;
    }

}

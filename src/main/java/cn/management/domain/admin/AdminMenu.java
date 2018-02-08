package cn.management.domain.admin;

import java.io.Serializable;

import javax.persistence.Table;

import cn.management.domain.BaseEntity;

/**
 * 菜单表实体类
 */
@Table(name = "admin_menu")
public class AdminMenu extends BaseEntity<Integer> implements Serializable {

    /**
     * 父级菜单ID
     */
    private Integer pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 菜单权限
     */
    private String permission;

    /**
     * 排序数字,越大越靠后
     */
    private Integer sort;
    
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "AdminMenu [pid=" + pid + ", name=" + name + ", url=" + url + ", permission=" + permission + ", sort="
                + sort + ", id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", delFlag="
                + delFlag + "]";
    }

}
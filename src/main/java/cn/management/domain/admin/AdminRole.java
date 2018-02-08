package cn.management.domain.admin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.management.domain.BaseEntity;

/**
 * 角色表实体类
 */
@Table(name = "admin_role")
public class AdminRole extends BaseEntity<Integer> implements Serializable {

    /**
     * json 处理工具
     */
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限，json字符串
     */
    private String permissions;

    /**
     * 获取权限map
     * @return
     */
    public Map<String, Object> getPermissionMap() {
        try {
            return OBJECT_MAPPER.readValue(this.permissions, Map.class);
        } catch (Exception e) {
            return new HashMap();
        }
    }

    /**
     * 设置权限，传入权限数组["user::add", "user:edit"]
     * @param permissions
     * @return
     */
    public void setPermissionMap(String[] permissions) throws JsonProcessingException {
        //将字符串数组转为 map
        HashMap<String, Integer> map = new HashMap<>();
        for (String key : permissions) {
            map.put(key, 1);
        }
        this.permissions = OBJECT_MAPPER.writeValueAsString(map);
    }
    
    public String getName() {
        return name;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "AdminRole [name=" + name + ", permissions=" + permissions + ", id=" + id + ", createTime=" + createTime
                + ", updateTime=" + updateTime + ", delFlag=" + delFlag + "]";
    }

}
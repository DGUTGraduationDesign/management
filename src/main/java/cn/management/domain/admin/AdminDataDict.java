package cn.management.domain.admin;

import javax.persistence.Table;

import cn.management.domain.BaseEntity;

import java.io.Serializable;

/**
 * 数据字典实体类
 */
@Table(name = "admin_data_dict")
public class AdminDataDict extends BaseEntity<Integer> implements Serializable {
    
	/** 
	 * 字典key 
	 */
    private String dictKey;
    
    /** 
     * 字典值
     */
    private String dictVal;
    
    /** 
     * 字典描述
     */
    private String dictDesc;
    
    /**
     * 字典类型
     */
    private String dictType;

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictVal() {
        return dictVal;
    }

    public void setDictVal(String dictVal) {
        this.dictVal = dictVal;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    @Override
    public String toString() {
        return "AdminDataDict{" +
                "dictKey='" + dictKey + '\'' +
                ", dictVal='" + dictVal + '\'' +
                ", dictDesc='" + dictDesc + '\'' +
                ", dictType='" + dictType + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag=" + delFlag +
                '}';
    }
}

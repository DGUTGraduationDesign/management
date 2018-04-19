package cn.management.domain.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流部署
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
public class WorkflowDeployment implements Serializable {

    /**
     * 流程ID，由“流程编号：流程版本号：自增长ID”组成
     */
    private String sid;

    /**
     * 部署名称（该字段是部署时手动置入的名称，对应表act_re_deployment的字段name）
     */
    private String name;

    /**
     * 流程编号（该编号就是流程文件process元素的id属性值）
     */
    private String key;

    /**
     * 流程名称（该字段就是流程文件process元素的name属性值）
     */
    private String pdName;

    /**
     * 流程定义的规则图片名称(用于查看流程图)
     */
    private String imageName;

    /**
     * 流程部署时间
     */
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss", timezone = "GMT+8")
    private Date srTime;

    /**
     * 流程版本号（由程序控制，新增即为1，修改后依次加1来完成的）
     */
    private String version;

    /**
     * 部署编号
     */
    private String deployment_id;

    /**
     * 流程部署文件(上传时用)
     */
    private MultipartFile file;

    public String getImageName() {
        return imageName;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public Date getSrTime() {
        return srTime;
    }

    public void setSrTime(Date srTime) {
        this.srTime = srTime;
    }

    public String getVersion() {
        return version;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeployment_id() {
        return deployment_id;
    }

    public void setDeployment_id(String deployment_id) {
        this.deployment_id = deployment_id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WorkflowDeployment{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", pdName='" + pdName + '\'' +
                ", imageName='" + imageName + '\'' +
                ", srTime='" + srTime + '\'' +
                ", version='" + version + '\'' +
                ", deployment_id='" + deployment_id + '\'' +
                ", file=" + file +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        WorkflowDeployment that = (WorkflowDeployment) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

}
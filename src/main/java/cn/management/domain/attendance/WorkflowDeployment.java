package cn.management.domain.attendance;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

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
     * 流程编号（该编号就是流程文件process元素的id属性值）
     */
    private String key;

    /**
     * 流程名称（该编号就是流程文件process元素的name属性值）
     */
    private String pdName;

    /**
     * 流程部署时间
     */
    private String srTime;

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

    public String getSrTime() {
        return srTime;
    }

    public void setSrTime(String srTime) {
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

    @Override
    public String toString() {
        return "WorkflowDeployment{" +
                "sid='" + sid + '\'' +
                ", key='" + key + '\'' +
                ", pdName='" + pdName + '\'' +
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
package cn.management.domain.project.dto;

public class ProjectMyTaskCountDto {

    private Integer myUnCompletetTask;

    private Integer myCompleteTask;

    private Integer myDelayTask;

    private Integer myCancelTask;

    public Integer getMyUnCompletetTask() {
        return myUnCompletetTask;
    }

    public Integer getMyCompleteTask() {
        return myCompleteTask;
    }

    public Integer getMyDelayTask() {
        return myDelayTask;
    }

    public Integer getMyCancelTask() {
        return myCancelTask;
    }

    public void setMyUnCompletetTask(Integer myUnCompletetTask) {
        this.myUnCompletetTask = myUnCompletetTask;
    }

    public void setMyCompleteTask(Integer myCompleteTask) {
        this.myCompleteTask = myCompleteTask;
    }

    public void setMyDelayTask(Integer myDelayTask) {
        this.myDelayTask = myDelayTask;
    }

    public void setMyCancelTask(Integer myCancelTask) {
        this.myCancelTask = myCancelTask;
    }

    @Override
    public String toString() {
        return "ProjectMyTaskInfo{" +
                "myUnCompletetTask=" + myUnCompletetTask +
                ", myCompleteTask=" + myCompleteTask +
                ", myDelayTask=" + myDelayTask +
                ", myCancelTask=" + myCancelTask +
                '}';
    }

}

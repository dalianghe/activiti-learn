package com.github.dalianghe.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessHistoryCustom {

    private String name; // 节点名称
    private String assignee; // 处理人
    private String startTime; // 接收时间
    private String endTime; // 完成时间
    private String duration; // 办理时长

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.startTime = sdf.format(startTime);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null!=endTime){
            this.endTime = sdf.format(endTime);
        }else{
            this.endTime = "";
        }
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

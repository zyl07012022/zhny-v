package com.alonginfo.project.appInspection.entities;

public class InspectionsRecord {

    /**
     * 巡检类型
     */
    private String patrolType;
    /**
     * 巡检计划派单时间
     */
    private String patrolSendData;
    /**
     * 异常巡检类型
     */
    private String patrolAbnormal;


    public String getPatrolAbnormal() {
        return patrolAbnormal;
    }

    public void setPatrolAbnormal(String patrolAbnormal) {
        this.patrolAbnormal = patrolAbnormal;
    }

    /**
     * 巡检对象
     */
    private String patrolObject;

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    public String getPatrolSendData() {
        return patrolSendData;
    }

    public void setPatrolSendData(String patrolSendData) {
        this.patrolSendData = patrolSendData;
    }

    public String getPatrolObject() {
        return patrolObject;
    }

    public void setPatrolObject(String patrolObject) {
        this.patrolObject = patrolObject;
    }
}

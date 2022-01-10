package com.alonginfo.project.component.entities;

public class Switch {
    /**
     * 主键
     */
    private String guid;
    /**
     * 开关编号
     */
    private String switchNumber;
    /**
     * 是否正常（0 否 1是）
     */
    private String switchType;
    /**
     * 巡检计划编号
     */
    private String patrolSchemeNumber;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(String switchNumber) {
        this.switchNumber = switchNumber;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    public String getPatrolSchemeNumber() {
        return patrolSchemeNumber;
    }

    public void setPatrolSchemeNumber(String patrolSchemeNumber) {
        this.patrolSchemeNumber = patrolSchemeNumber;
    }
}

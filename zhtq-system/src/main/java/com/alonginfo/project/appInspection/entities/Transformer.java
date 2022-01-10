package com.alonginfo.project.appInspection.entities;

/**
 * 变压器
 */
public class Transformer {
    /**
     * 主键
     */
    private String guid;
    /**
     * 变压器编号
     */
    private String transformerNumber;
    /**
     * 变压器温度
     */
    private String transformerTemperature;
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

    public String getTransformerNumber() {
        return transformerNumber;
    }

    public void setTransformerNumber(String transformerNumber) {
        this.transformerNumber = transformerNumber;
    }

    public String getTransformerTemperature() {
        return transformerTemperature;
    }

    public void setTransformerTemperature(String transformerTemperature) {
        this.transformerTemperature = transformerTemperature;
    }

    public String getPatrolSchemeNumber() {
        return patrolSchemeNumber;
    }

    public void setPatrolSchemeNumber(String patrolSchemeNumber) {
        this.patrolSchemeNumber = patrolSchemeNumber;
    }
}

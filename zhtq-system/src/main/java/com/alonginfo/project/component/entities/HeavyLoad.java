package com.alonginfo.project.component.entities;

import lombok.Data;

@Data
public class HeavyLoad {

    private String Guid;//主键

    private String patrolSchemeNumber;//巡检计划编号
    /**
     * 重载小时数
     */
    private String heavyLoad;
    /**
     * 过载小时数
     */
    private String overload;
    /**
     * 正常小时数
     */
    private String normal;
    /**
     * 连续最大负载率
     */
    private String maxImbalance;
    /**
     * 连续最大负载时刻
     */
    private String timeMaximumImbalance;
    /**
     * A项最大负载率
     */
    private String loadRatea;
    /**
     * B项最大负载率
     */
    private String loadRateb;
    /**
     * C项最大负载率
     */
    private String loadRatec;

    /**
     * 更新时间
     * @return
     */
    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    public String getHeavyLoad() {
        return heavyLoad;
    }

    public void setHeavyLoad(String heavyLoad) {
        this.heavyLoad = heavyLoad;
    }

    public String getOverload() {
        return overload;
    }

    public void setOverload(String overload) {
        this.overload = overload;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getMaxImbalance() {
        return maxImbalance;
    }

    public void setMaxImbalance(String maxImbalance) {
        this.maxImbalance = maxImbalance;
    }

    public String getTimeMaximumImbalance() {
        return timeMaximumImbalance;
    }

    public void setTimeMaximumImbalance(String timeMaximumImbalance) {
        this.timeMaximumImbalance = timeMaximumImbalance;
    }

    public String getLoadRatea() {
        return loadRatea;
    }

    public void setLoadRatea(String loadRatea) {
        this.loadRatea = loadRatea;
    }

    public String getLoadRateb() {
        return loadRateb;
    }

    public void setLoadRateb(String loadRateb) {
        this.loadRateb = loadRateb;
    }

    public String getLoadRatec() {
        return loadRatec;
    }

    public void setLoadRatec(String loadRatec) {
        this.loadRatec = loadRatec;
    }
}

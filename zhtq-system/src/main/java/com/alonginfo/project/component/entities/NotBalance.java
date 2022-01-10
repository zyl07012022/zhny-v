package com.alonginfo.project.component.entities;

import lombok.Data;

@Data
public class NotBalance {

    private String Guid;//主键

    private String patrolSchemeNumber;//巡检计划编号
    /**
     * 正常小时数
     */
    private String normal;
    /**
     * 三项不平衡小时数
     */
    private String trigonalImbalance;
    /**
     * 最大不平衡率
     */
    private String maxImbalance;
    /**
     * 最大不平衡率时刻
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

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getTrigonalImbalance() {
        return trigonalImbalance;
    }

    public void setTrigonalImbalance(String trigonalImbalance) {
        this.trigonalImbalance = trigonalImbalance;
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

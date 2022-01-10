package com.alonginfo.project.component.entities;

import lombok.Data;

@Data
public class LowVoltage {
    private String Guid;//主键

    private String patrolSchemeNumber;//巡检计划编号

    /**
     * A电压合格率占比（单位：%）
     */
    private String teama;
    /**
     * B电压合格率占比（单位：%）
     */
    private String teamb;
    /**
     * C电压合格率占比（单位：%）
     */
    private String teamc;
    /**
     * 最低电压值
     */
    private String minv;
    /**
     * 最低电压值时刻
     */
    private String timeMinimumImbalance;
    /**
     * A组电压合格率
     */
    private String loadRatea;
    /**
     * B组电压合格率
     */
    private String loadRateb;
    /**
     * C组电压合格率
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


    public String getTeama() {
        return teama;
    }

    public void setTeama(String teama) {
        this.teama = teama;
    }

    public String getTeamb() {
        return teamb;
    }

    public void setTeamb(String teamb) {
        this.teamb = teamb;
    }

    public String getTeamc() {
        return teamc;
    }

    public void setTeamc(String teamc) {
        this.teamc = teamc;
    }

    public String getMinv() {
        return minv;
    }

    public void setMinv(String minv) {
        this.minv = minv;
    }

    public String getTimeMinimumImbalance() {
        return timeMinimumImbalance;
    }

    public void setTimeMinimumImbalance(String timeMinimumImbalance) {
        this.timeMinimumImbalance = timeMinimumImbalance;
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

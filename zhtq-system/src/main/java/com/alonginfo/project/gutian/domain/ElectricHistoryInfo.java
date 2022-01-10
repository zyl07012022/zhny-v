package com.alonginfo.project.gutian.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 历史能耗数据实体类
 * @Author Yalon
 * @Date 2020-04-20 16:16
 * @Version mxdata_v
 */
@Data
public class ElectricHistoryInfo {

    /**
     * 今日用电量
     */
    private BigDecimal todayPower;

    /**
     * 上月同期用电量
     */
    private BigDecimal lastMonthDayPower;

    /**
     * 昨日用电量
     */
    private BigDecimal yesterdayPower;

    /**
     * 本月用电量
     */
    private BigDecimal thisMonthPower;

    /**
     * 去年同期用电量
     */
    private BigDecimal lastYearMonthPower;

    /**
     * 上月用电量
     */
    private BigDecimal lastMonthPower;

    public ElectricHistoryInfo() {
    }

    public ElectricHistoryInfo(BigDecimal todayPower, BigDecimal lastMonthDayPower, BigDecimal yesterdayPower, BigDecimal thisMonthPower, BigDecimal lastYearMonthPower, BigDecimal lastMonthPower) {
        this.todayPower = todayPower;
        this.lastMonthDayPower = lastMonthDayPower;
        this.yesterdayPower = yesterdayPower;
        this.thisMonthPower = thisMonthPower;
        this.lastYearMonthPower = lastYearMonthPower;
        this.lastMonthPower = lastMonthPower;
    }
}

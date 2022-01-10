package com.alonginfo.project.gutian.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 电实时信息实体类
 * @Author Yalon
 * @Date 2020-04-17 13:46
 * @Version mxdata_v
 */
@Data
public class ElectricRealTimeInfo {

    /**
     * A相电压
     */
    private BigDecimal voltageA;

    /**
     * B相电压
     */
    private BigDecimal voltageB;

    /**
     * C相电压
     */
    private BigDecimal voltageC;

    /**
     * A相电流
     */
    private BigDecimal ampereA;

    /**
     * B相电流
     */
    private BigDecimal ampereB;

    /**
     * C相电流
     */
    private BigDecimal ampereC;

    /**
     * A相有功功率
     */
    private BigDecimal wattActiveA;

    /**
     * B相有功功率
     */
    private BigDecimal wattActiveB;

    /**
     * C相有功功率
     */
    private BigDecimal wattActiveC;

    /**
     * A相无功功率
     */
    private BigDecimal wattReactiveA;

    /**
     * A相无功功率
     */
    private BigDecimal wattReactiveB;

    /**
     * A相无功功率
     */
    private BigDecimal wattReactiveC;
}

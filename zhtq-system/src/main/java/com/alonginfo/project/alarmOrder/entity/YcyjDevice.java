package com.alonginfo.project.alarmOrder.entity;

import lombok.Data;

/**
 * @author 崔亚魁
 */
@Data
public class YcyjDevice {

    private Long id;

    /** 变压器id */
    private String transf_id;

    private String transf_name;

    private String feeder_id;

    private String feeder_name;

    private String power_company;

    private String power_supply;

    /** A相功率 */
    private Double pa;

    /** A相功率 */
    private Double pb;

    /** A相功率 */
    private Double pc;

    /** 实际容量 */
    private Double rated_capacity;

    /** A相电压 */
    private Double ua;

    /** B相电压 */
    private Double ub;

    /** C相电压 */
    private Double uc;

    /** A相电流 */
    private Double ia;

    /** B相电流 */
    private Double ib;

    /** C相电流 */
    private Double ic;

    /** 三相有功功率 */
    Double p3;

    /** 三相无功功率 */
    Double q3;

    /** 持续时间 */
    private String continue_time;
}

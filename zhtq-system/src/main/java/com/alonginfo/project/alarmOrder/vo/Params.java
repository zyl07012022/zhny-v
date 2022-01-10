package com.alonginfo.project.alarmOrder.vo;

import lombok.Data;

/**
 * @author 崔亚魁
 */
@Data
public class Params {

    /** 变压器id */
    String transf_id;

    /** A相功率 */
    Double pa;

    /** A相功率 */
    Double pb;

    /** A相功率 */
    Double pc;

    /** 三相有功功率 */
    Double p3;

    /** 三相无功功率 */
    Double q3;

    /** 实际容量 */
    Double rated_capacity;

    /** 最大电流 */
    Double maxI;

    /** 最小电流 */
    Double minI;

    /** A相电压 */
    Double ua;

    /** B相电压 */
    Double ub;

    /** C相电压 */
    Double uc;

    /** A相电流 */
    Double ia;

    /** B相电流 */
    Double ib;

    /** C相电流 */
    Double ic;

    /** 持续时间 */
    int durTime;
}

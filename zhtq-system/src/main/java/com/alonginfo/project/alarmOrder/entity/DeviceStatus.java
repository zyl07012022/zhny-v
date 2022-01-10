package com.alonginfo.project.alarmOrder.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 崔亚魁
 */
@Data
public class DeviceStatus {

    private Long id;
    private String subordinate_substation;
    private String county_company;
    private String power_supply;
    private String feeder;
    private String transf_name;

    private String tg_id;
    private String transf_id;
    private String patrol_abnormal;
    private String areaPM_Name;
    private String user_num;
    private Timestamp updata_time;
    private String tg_name;
    private Integer error_type;
    private Double load_rate_a;
    private Double load_rate_b;
    private Double load_rate_c;
    private Double max_imbalance;
    private Timestamp time_maximum_imbalance;
    private Double min_v;
    private Timestamp min_volt_time;
    private Double volt_rate_a;
    private Double volt_rate_b;
    private Double volt_rate_c;

    /** 三项不平衡时间 */
    private Double trigonal_imbalance;

    /** 三项平衡时间 */
    private Double trigonal_nomal;

    private Double heavy_load;

    private Double overload;

    private Double normal;








}

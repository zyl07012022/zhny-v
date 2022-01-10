package com.alonginfo.project.alarmOrder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 任金义
 * @version 1.0
 * @date 2021/7/23 下午 02:14
 * @CreateTime: 2021-07-23 14:14
 */
@Data
public class ByqInfo {

    private Integer Id;

    //变压器id
    private String transfId;

    //变压器名称
    private String transfName;

    //异常状态类型
    private String errorType;

    //异常类型（39台区低压  40三相不平衡 41配变重过载）
    private Integer patrolAbnormal;

    //所属馈线id
    private String feederId;

    //所属馈线名称
    private String feederName;

    //所属供电公司
    private String powerCompany;

    //所属供电所
    private String powerSupply;

    //所属台区id
    private String tgId;

    //所属台区名称
    private String tgName;

    //台区经理
    private String areaPMName;

    //所属台区用户数
    private Integer userNum;

    //持续时间
    private String durTime;

    //数据更新时间
    private String updateTime;
}

 

package com.alonginfo.project.repairApp.entity;

import lombok.Data;

@Data
public class Orders {
    private String workNumber; //工单编号
    private String suborder; //抢修子单号
    private String teamId; //队伍编号
    private String table; //抢修 or 巡检

    private String workStatus; //抢修工单状态
    private String surveyTime;// 勘察时间
    private String repairNum;// 预计修复时长(小时)
    private String isEquipment;// 预是否为0.4kV下的低压设备(1是、0否)
    private String equipmentName;// 故障设备名称
    private String degreeUrgency;// 紧急程度
    private String sort;// 分类
    private String faultContent;// 故障内容
    private String repairContent;// 修复内容
    private String emergencyNumber;//抢修单号
    private String voltageClasses;//电压分级

    private String userId;//影响用户
    private String courtsId;//影响台区
    private String photoScene;//影响台区
}

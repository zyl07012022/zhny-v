package com.alonginfo.project.emergencyCommand.entity;

import com.alonginfo.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.List;

@Data
public class RepairWorkOrder {
    private String guid;

    @Excel(name = "序号")
    private String RowNum;

    @Excel(name = "工单编号")
    private String workNumber;  //工单编号

    @Excel(name = "工单名称")
    private String workName; //工单名称

    @Excel(name = "所属供电所")
    private String powerSupply; //所属供电所编号

    @Excel(name = "所属变电站")
    private String subordinateSubstation;//所属变电站

    @Excel(name = "所属馈线")
    private String feeder;//所属馈线

    @Excel(name = "报修时间")
    private String repairsTime; //报修时间

    private String voltageNode; //故障电压区间
    private String voltageClasses; //故障电压等级编号
    private String county; //所属县编号
    private String source; //工单来源编号
    private String classification; //现场分类
    private String faultPhenomenon; //故障现象编号
    private String workStatus;//工单状态（0 待派单 1 工单已派发 2 已到达 3 勘察 4 已修复 5 复电  11 被合并 ）
    private String pointName;//故障点名称
    private String pointSite;//故障点坐标
    private String teamId;//抢修队伍编号
    private String workPowerSupply; //抢修承接供电所编号
    private String emergencyNumber;//抢修单号
    private String parentNumber; //所属工单号

    @Excel(name = "派单时间")
    private String sendTime; //派单时间起

    @Excel(name = "复电时间")
    private String surveyTime;//勘察时间

    private String sendTimeEnd; //派单时间止
    private String faultAddress; //故障地址
    @Excel(name = "抢修人员")
    private String qxry;//抢修人员

    @Excel(name = "抢修描述")
    private String emergencyNote; //抢修描述

    private String powerSupplyName; //所属供电所名称
    private String voltageClassesName; //故障电压等级名称
    private String countyName; //所属县名称
    private String sourceName;  //工单来源名称
    private String faultPhenomenonName; //故障现象名称
    private String workPowerSupplyName; //抢修承接供电所名称

    private String suborderNumber; //查询子单标识
    private List<Suborder> suborderNums;//合并工单编号集合

    private List<String> userName;//用户名称
    private List<String> userId;//用户名称
    private List<String> courtsNumber;//台区编号

    private String effectUser;//影响用户
    private String courtsId;//影响台区

    private String faultType;//故障类型

    private  String photoScene;//抢修照片

}

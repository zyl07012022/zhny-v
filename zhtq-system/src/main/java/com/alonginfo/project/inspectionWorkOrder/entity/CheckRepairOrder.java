package com.alonginfo.project.inspectionWorkOrder.entity;

import com.alonginfo.framework.aspectj.lang.annotation.Excel;
import com.alonginfo.project.emergencyCommand.entity.Suborder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class CheckRepairOrder {
    private String guid;//主键

    @Excel(name = "异常巡检单号")
    private String patrolSchemeNumber;//巡检计划编号
    private String patrolSchemeName;//巡检计划名称
    private String patrolType;//巡检类型 （1 计划巡检 2 异常巡检）

    @Excel(name = "县公司")
    private String countyCompany;//县公司

    @Excel(name = "供电所")
    private String powerSupply;//所属供电所

    @Excel(name = "所属变电站")
    private String subordinateSubstation;//所属变电站

    @Excel(name = "所属馈线")
    private String feeder;//所属馈线

    @Excel(name = "巡检对象类型")
    private String patrolTypes;//巡检对象类型

    @Excel(name = "巡检对象")
    private String patrolObject;//巡检对象

    @Excel(name = "巡检状态")
    private String patrolStatus;//巡检状态（1 未巡检或未派发 2 已接巡检 3 已巡检 4 归档）
    private String patrolData;//巡检计划创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "巡检计划派单时间")
    private String patrolSendData;//巡检计划派单时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String patrolSendDataEnd;
    private String patrolNumber;//巡检单号
    private String teamId;//抢修队伍编号
    private String voltageClasses;//电压分级
    private  String source;//工单来源
    private String voltage;//电压等级

    @Excel(name = "异常类型")
    private String patrolAbnormal;//异常巡检类型
    private String note;//巡检描述
    private String parentNumber;//父类编号

    private String courtsId;//影响台区
    private String courtsName;//台区名称
    private String powerSupplyArea;//供电区域
    private String courtsManager;//台区经理
    private String courtsPhone;//台区联系电话
    private String county;//所属县

    private String userId;//用户编号、影响用户
    private String userName;//用户名称
    private String userAddress;//用户地址
    private String branchBox;//所属分支箱

    private String name;//人员姓名
    private String vocationalSkills;//职业技能
    private String phone;//联系电话
    private String technicalPost;//职称
    private String station;//岗位
    private String nameRepairCar;//巡检车辆
    private String powerSupplies;//供电所集合
    private String suborderNumber; //查询子单标识
    private List<Suborder> suborderNums;//合并工单编号集合
    private String pointSite;

    private List<Object> userIds;//用户名称
    private List<String> courtsNumber;//台区编号

    private Integer pageNum;//分页页码
    private Integer pageSize;//分页数据数

    private  String activeName;



}

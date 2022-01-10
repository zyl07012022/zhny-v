package com.alonginfo.project.inspectionWorkOrder.entity;

import com.alonginfo.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

@Data
public class Inspection {

    @Excel(name = "巡检计划编号")
    private String patrolSchemeNumber;

    @Excel(name = "巡检计划名称")
    private String patrolSchemeName;

    @Excel(name = "县公司")
    private String countyCompany;

    @Excel(name = "所属供电所")
    private String powerSupply;

    @Excel(name = "所属变电站")
    private String subordinateSubstation;

    @Excel(name = "所属馈线")
    private String feeder;

    @Excel(name = "巡检对象类型")
    private String patrolTypes;

    @Excel(name = "巡检对象")
    private String patrolObject;

    private String feedback;//巡检反馈

}

package com.alonginfo.project.emergencyCommand.entity;

import lombok.Data;

@Data
public class EmergencyCourts {
    private String guiid;//主键
    private String courtsNumber;//台区编号
    private String courtsName;//台区名称
    private String feeder;//所属馈线
    private String powerStation;//所属电站
    private String powerSupplyArea;//供电区域
    private Integer powerSupply;//所属供电所
    private String courtsManager;//台区经理
    private String courtsPhone;//台区联系电话
    private Integer county;//所属县

}

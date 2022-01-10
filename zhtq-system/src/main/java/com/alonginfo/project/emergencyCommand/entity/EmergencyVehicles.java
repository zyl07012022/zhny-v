package com.alonginfo.project.emergencyCommand.entity;

import lombok.Data;

@Data
public class EmergencyVehicles {
    //主键
    private String guid;
    //抢修车牌
    private String numbeRepairCar;
    //抢修车名称
    private String nameRepairCar;
    //抢修车类型
    private String typeRepairCar;
    //抢修队名称
    private String emergencyCrewName;
    //所属单位
    private String affiliatedUnit;
    //GPS编号
    private String gpsNumber;
    //备注
    private String note;
}

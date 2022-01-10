package com.alonginfo.project.alarmOrder.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceFaultInfo {

    private Integer id ;
    private String deviceId ;
    private String deviceName ;
    private String deviceType ;
    private Date tripTime;
    private String faultType ;
    private String content;
    private String accidentRange ;
    private String psrType ;
    private String mRid ;
    private String devName ;
    private String countyCompany ;
    private String powerSupply ;
    private String substation;
    private String feeder ;
    private String workNumber ;
    private String dealStatus ;
    private Date updateTime;
}

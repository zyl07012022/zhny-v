package com.alonginfo.project.emergencyCommand.entity;

import lombok.Data;

@Data
public class EmergencyUser {
    private String guid;//主键
    private String userId;//用户编号
    private String UserName;//用户名称
    private String courtsNumber;//所属台区编号
    private String branchBox;//所属分支箱
    private String userAddress;//用户地址
}

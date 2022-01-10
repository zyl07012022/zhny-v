package com.alonginfo.project.emergencyCommand.entity;

import lombok.Data;

@Data
public class EmergencyDictionary {
    //主键
    private String guid;
    //字典编码
    private String codeId;
    //字典名称
    private String codeName;
    //字典类型
    //1 所属单位 2 所属供电所 3 所属县
    // 4 故障电压等级 5 故障现象 6 现场分类
    // 7 抢修车类型 8 资源类别  9 资源类型
    // 10 职业技能 11 职称 12 岗位 13 驻点职能 14 资源状态 15 工单来源
    private Integer codeType;
}

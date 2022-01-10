package com.alonginfo.project.inspectionApp.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InspectionAppMapper {
    List<Map<String, Object>> selectInspection(@Param("patrolSendData") String patrolSendData);//查询巡检列表
}

package com.alonginfo.project.component.service;

import com.alonginfo.project.component.entities.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ComponentService {
    Map<String,Object> queryFormData( String patrolSchemeNumber);
    List<ReponseResult> queryKgbhData(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    List<ReponseResult> queryTransformerTemperature(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    NotBalance queryNotBalance(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    LowVoltage queryLowVoltage(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    HeavyLoad queryHeavyLoad(@Param("patrolSchemeNumber") String patrolSchemeNumber);

   List<Map<String, Object>> queryTableData(String patrolSchemeNumber);
}

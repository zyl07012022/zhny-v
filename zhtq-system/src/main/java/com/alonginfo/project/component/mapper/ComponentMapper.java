package com.alonginfo.project.component.mapper;


import com.alonginfo.project.component.entities.HeavyLoad;
import com.alonginfo.project.component.entities.LowVoltage;
import com.alonginfo.project.component.entities.NotBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description  mapper
 * @Author Yalon
 * @Date 2020-04-14 16:52
 * @Version mxdata_v
 */
@Mapper
public interface ComponentMapper {
   Map<String,Object> queryFormData(@Param("patrolSchemeNumber") String patrolSchemeNumber);
   List<Map<String,String>> queryKgbhData(@Param("patrolSchemeNumber") String patrolSchemeNumber);
   List<Map<String,String>> queryTransformerTemperature(@Param("patrolSchemeNumber") String patrolSchemeNumber);
   NotBalance queryNotBalance(@Param("patrolSchemeNumber") String patrolSchemeNumber);
   LowVoltage queryLowVoltage(@Param("patrolSchemeNumber") String patrolSchemeNumber);
   HeavyLoad queryHeavyLoad(@Param("patrolSchemeNumber") String patrolSchemeNumber);

   Map<String, Object> queryTableData(String patrolSchemeNumber);
}

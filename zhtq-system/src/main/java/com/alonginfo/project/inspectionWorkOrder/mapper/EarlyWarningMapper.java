package com.alonginfo.project.inspectionWorkOrder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EarlyWarningMapper {
    List<Map<String, Object>> selectCompany();//查询公司、供电所

    List<Map<String, Object>> selectPatrolNumber(@Param("orgName") String orgName);//查询巡检计划编号

    List<Map<String, Object>> selectAbnormal(@Param("orgName") String orgName);//查询异常数量

    List<Map<String, Object>> selectCourts(@Param("workNumbers") String workNumbers);//查询台区数量

    List<Map<String, Object>> selectManager(@Param("courtsNumbers") String courtsNumbers);//查询台区经理数量

    List<Map<String, Object>> selectUser(@Param("workNumbers") String workNumbers);//查询用户数量

    List<Map<String, Object>> calculationStatistics(@Param("str") String str, @Param("date") String date,@Param("orgName")String orgName);//查询异常率

    List<Map<String, Object>> selectWO(@Param("date") String date);//查询巡检单号最大值

    Integer createWO(@Param("patrolNumber") String patrolNumber,@Param("patrolSchemeNumber") String patrolSchemeNumber);//生成巡检单号

    List<Map<String, Object>> selectNum(@Param("orgName") String orgName);

    List<Map<String, String>> selectAbnormalList(@Param("orgName") String orgName,@Param("patrolAbnormal") String patrolAbnormal);

    Integer deleteOrder(@Param("transfId") String transfId);

    List<Map<String, Object>> selectNumList(@Param("orgName") String orgName);

    Integer upDeviceStatus(@Param("deviceType") String deviceType,@Param("ID") String ID);

    Integer selectNormalTr(@Param("str") String str,@Param("orgId") String orgId);

    List<Map<String, String>> selectMonthList(@Param("orgName") String orgName);

    List<Map<String, String>> selectDateList(@Param("orgName") String orgName);

    String queryOrgName(@Param("orgId") String orgId);
}

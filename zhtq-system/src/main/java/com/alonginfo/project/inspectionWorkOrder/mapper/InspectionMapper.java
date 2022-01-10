package com.alonginfo.project.inspectionWorkOrder.mapper;

import com.alonginfo.framework.aspectj.lang.annotation.DataSource;
import com.alonginfo.framework.aspectj.lang.enums.DataSourceType;
import com.alonginfo.project.component.entities.HeavyLoad;
import com.alonginfo.project.component.entities.LowVoltage;
import com.alonginfo.project.component.entities.NotBalance;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.entity.Inspection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InspectionMapper {
    /**
     * 巡检计划
     * @param map
     * @return
     */
    List<Map<String,Object>> getInspection(CheckRepairOrder map);

    /**
     * 影响用户
     * @param patrolSchemeNumber
     * @return
     */
    List<Map<String, String>> queryUser(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 影响台区
     * @param patrolSchemeNumber
     * @return
     */
    List<Map<String, String>> queryCourts(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 派工信息
     * @param --patrolSchemeNumber
     * @param --station
     * @return
     */
    List<Map<String, String>> queryDispatch(Map map);

    List<Map<String, String>> queryDispatchOther(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 巡检计划发起
     * @param checkRepairOrder
     */
    void planAdd(CheckRepairOrder checkRepairOrder);

    /**
     * 巡检计划修改
     * @param checkRepairOrder
     */
    void planChange(CheckRepairOrder checkRepairOrder);

    /**
     * 巡检计划删除
     * @param patrolSchemeNumber
     */
    void deleteBypatrolSchemeNumber(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 工单派发
     * @param patrolNumber
     * @param patrolSchemeNumber
     * @param patrolSendData
     */
    void operate(@Param("patrolNumber") String patrolNumber,
                 @Param("patrolSchemeNumber") String patrolSchemeNumber,
                 @Param("patrolSendData") String patrolSendData);

    /**
     * 工单归档
     * @param patrolSchemeNumber
     */
    void archived(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 列表导出
     * @param patrolStatus
     * @return
     */
    List<Inspection> export(@Param("patrolStatus") String patrolStatus);

    Integer addTrigonalImbalance(NotBalance notBalance);
    Integer addOverloadDetail(HeavyLoad heavyLoad);
    Integer addVoltageDetail(LowVoltage lowVoltage);

}

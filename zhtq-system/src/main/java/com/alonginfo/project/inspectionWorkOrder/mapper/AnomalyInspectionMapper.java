package com.alonginfo.project.inspectionWorkOrder.mapper;


import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnomalyInspectionMapper {

    List<Map<String, String>> selectAnomalyOrder( CheckRepairOrder checkRepairOrder);

    List<Map<String, String>> queryUser(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    List<Map<String, String>> queryCourts(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 查询队伍队长 || 队伍人员（抢修人员信息表）
     *
     * @param teamId  队伍编号
     * @param station 岗位编号
     * @return
     */
    List<Map<String, String>> queryTeamInfo(@Param("teamId") String teamId, @Param("station") String station);

    /**
     * 查询队伍车辆信息
     *
     * @param teamId 队伍编号
     * @return
     */
    List<Map<String, String>> queryVehicle(@Param("teamId") String teamId);


    /**
     * 操作工单状态
     *
     * @param checkRepairOrder
     * @return
     */
    Integer operation(CheckRepairOrder checkRepairOrder);

    /**
     * 合并多条工单
     *
     * @param list
     * @return
     */
    Integer suborder(List<CheckRepairOrder> list);

    List<CheckRepairOrder> export(@Param("patrolStatus") String patrolStatus);
}

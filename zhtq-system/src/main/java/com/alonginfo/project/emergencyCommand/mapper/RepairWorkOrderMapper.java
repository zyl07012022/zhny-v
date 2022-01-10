package com.alonginfo.project.emergencyCommand.mapper;

import com.alonginfo.project.emergencyCommand.entity.RelevanceCourts;
import com.alonginfo.project.emergencyCommand.entity.RelevanceUser;
import com.alonginfo.project.emergencyCommand.entity.RepairWorkOrder;
import com.alonginfo.project.emergencyCommand.entity.Suborder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RepairWorkOrderMapper {
    List<Map<String, String>> queryPage(RepairWorkOrder repairWorkOrder);

    List<Map<String, String>> queryUser(@Param("workNumber") String workNumber);

    List<Map<String, String>> queryCourts(@Param("workNumber") String workNumber);

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
     * @param repairWorkOrder
     * @return
     */
    Integer operation(RepairWorkOrder repairWorkOrder);

    /**
     * 合并多条工单
     *
     * @param list
     * @return
     */
    Integer suborder(List<Suborder> list);

    Integer addCourts(RelevanceCourts relevanceCourts);

    Integer addUser(RelevanceUser relevanceUser);

    void save(RepairWorkOrder repairWorkOrder);

    List<RepairWorkOrder> selectExcelData(Map map);
}

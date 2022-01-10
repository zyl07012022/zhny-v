package com.alonginfo.project.repairApp.mapper;

import com.alonginfo.project.repairApp.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface RepairMapper {
    List<Map<String, Object>> selectRepair(@Param("sendTime") String sendTime);//查询抢修列表

    @Select("SELECT guid, team_id teamId, name FROM t_yj_gzqx_qxdy WHERE station =#{station}")
    @ResultType(List.class)
    List<Map<String, Object>> selectCaptain(@Param("station") String station);//查询班长列表

    @Select("SELECT name FROM t_yj_gzqx_qxdy WHERE station !=24 and team_id=#{teamId}")
    @ResultType(List.class)
    List<Map<String, Object>> selectTeammate(@Param("teamId") String teamId);//查询队友列表

    @Select("SELECT numbe_repair_car carNumber FROM t_yj_gzqx_qxc WHERE team_id=#{teamId}")
    @ResultType(List.class)
    List<Map<String, Object>> selectVehicle(@Param("teamId") String teamId);//查询车辆列表

    @Select("UPDATE t_yj_qzqx_work SET team_id=#{teamId},work_status=6 WHERE work_number=#{workNumber}")
    @ResultType(Integer.class)
    Integer submit(@Param("workNumber") String workNumber, @Param("teamId") String teamId); //提交工单

    @Select("UPDATE t_yj_qzqx_work SET work_status = '11',parent_number = #{workNumber} WHERE work_number=#{suborder}")
    @ResultType(Integer.class)
    Integer suborder(@Param("workNumber") String workNumber, @Param("suborder") String suborder); //置成子单

    @Select("UPDATE t_yj_qzqx_patrol_scheme SET team_id=#{teamId},patrol_status=2 WHERE patrol_scheme_number=#{workNumber}")
    @ResultType(Integer.class)
    Integer submit2(@Param("workNumber") String workNumber, @Param("teamId") String teamId); //提交巡检工单

    @Select("UPDATE t_yj_qzqx_patrol_scheme SET patrol_status = '11',parent_number = #{workNumber} WHERE patrol_scheme_number=#{suborder}")
    @ResultType(Integer.class)
    Integer suborder2(@Param("workNumber") String workNumber, @Param("suborder") String suborder); //置成巡检子单

    Integer updateState(Orders orders); //动态修改工单状态

    @Select("SELECT equipment_name equipmentName,degree_Urgency degreeUrgency,sort,fault_content faultContent,userId,courtsId FROM t_yj_qzqx_work where work_number=#{workNumber}")
    @ResultType(List.class)
    List<Map<String, Object>> selectOrders(@Param("workNumber") String workNumber);//查询工单信息
}

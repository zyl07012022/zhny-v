package com.alonginfo.project.gutian.mapper;

import com.alonginfo.project.gutian.domain.ElectricRealTimeInfo;
import com.alonginfo.project.gutian.provider.GutianProvider;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description 古田三大业务场景Mapper
 * @Author Yalon
 * @Date 2020-04-15 13:40
 * @Version mxdata_v
 */
@Mapper
public interface GutianMapper {

    /**
     * 查找业务场景的基础信息（楼宇数量，建筑面积，供能线路数）
     * @param sceneName 场景名字
     * @return buildingNum 楼宇数量 sceneAcreage 建筑面积 wireNum 供能线路数
     */
    @Select(" SELECT building_num buildingNum, scene_acreage sceneAcreage, wire_num wireNum FROM cps_scene WHERE scene_name = #{sceneName}")
    @ResultType(Map.class)
    Map<String, Object> selectSceneBaseInfoByName(@Param("sceneName") String sceneName);

    /**
     * 查询场景年接待人数
     * @param sceneName 场景名字
     * @param year 年
     * @return
     */
    @Select(" SELECT SUM(cr.reception) FROM cps_reception cr LEFT JOIN cps_scene cs ON cr.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(cr.date, '%Y') = #{year}")
    @ResultType(Integer.class)
    Integer selectReceptionByNameAndYear(@Param("sceneName") String sceneName, @Param("year") String year);

    /**
     * 查询重大活动天数
     * @param sceneName 场景
     * @param year 年
     * @return
     */
    @Select(" SELECT COUNT(1) FROM cps_reception cr LEFT JOIN cps_scene cs ON cr.scene_id = cs.scene_id  " +
            " WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(cr.date, '%Y') = #{year} AND cr.is_event = 2 ")
    @ResultType(Integer.class)
    Integer selectEventCountByNameAndYear(@Param("sceneName") String sceneName,@Param("year") String year);

    /**
     * 查询接待天数
     * @param sceneName 场景
     * @param year 年
     * @return
     */
    @Select(" SELECT COUNT(1) FROM cps_reception cr LEFT JOIN cps_scene cs ON cr.scene_id = cs.scene_id  " +
            " WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(cr.date, '%Y') = #{year}")
    @ResultType(Integer.class)
    Integer selectReceptionCountByNameAndYear(@Param("sceneName") String sceneName,@Param("year") String year);

    /**
     * 查询年总用电量
     * @param sceneName 场景
     * @param year 年
     * @return
     */
    @Select(" SELECT SUM(t1.powerValue) FROM ( SELECT MAX(cue.ammeter_value)-MIN(cue.ammeter_value) powerValue " +
            " FROM cps_use_electricity cue JOIN cps_building_group cbg ON cue.ammeter_id = cbg.ammeter_id " +
            " JOIN cps_scene cs ON cbg.scene_id = cs.scene_id WHERE cs.scene_name = #{sceneName} " +
            " AND DATE_FORMAT(cue.time, '%Y') = #{year} GROUP BY cue.ammeter_id) t1")
    @ResultType(BigDecimal.class)
    BigDecimal selectPowerYearByNameAndYear(@Param("sceneName") String sceneName, @Param("year") String year);


    /**
     * 查询用电量 按日期或月分组
     * @param params
     *          sceneName 场景名称
     *          groupType 分组类型 day 按天分组 month 按月分组
     *          startDate  开始日期
     *          endDate 结束日期
     *          isPeak 是否为计算峰值
     *
     * @return
     */
    @SelectProvider(type = GutianProvider.class, method = "selectPowerUseGroupByType")
    List<Map<String, Object>> selectPowerUseGroupByType(Map<String, Object> params);

    /**
     * 查询用电功率
     * @param sceneName  场景名字
     * @param queryDate 查询日期
     * @return
     */
    @Select(" SELECT DATE_FORMAT(cp.time, '%H') groupTime,  ROUND(IFNULL(AVG(cp.active_power),0)*1000,2) watt " +
            " FROM cps_power cp LEFT JOIN cps_building_group cbg ON cp.ammeter_id = cbg.ammeter_id LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(cp.time, '%Y-%m-%d') = #{queryDate} GROUP BY DATE_FORMAT(cp.time, '%H') ORDER BY groupTime ")
    List<Map<String, Object>> selectWattByNameGroupByHour(@Param("sceneName") String sceneName, @Param("queryDate") String queryDate);

    /**
     * 查询人均耗电 每平米耗电  按月分组
     * @param sceneName 场景名称
     * @param year 查询年
     * @return
     */
    @Select("SELECT t1.queryMonth queryMonth, t1.power power, t2.reception reception FROM (" +
            "       SELECT subTable.queryMonth, SUM(subTable.power) power FROM (" +
            "           SELECT cue.ammeter_id ammeterId, DATE_FORMAT(cue.time, '%Y-%m') queryMonth, MAX(cue.ammeter_value)-MIN(cue.ammeter_value) power " +
            "           FROM cps_use_electricity cue JOIN cps_building_group cbg ON cue.ammeter_id = cbg.ammeter_id " +
            "           JOIN cps_scene cs ON cs.scene_id = cbg.scene_id " +
            "           WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(cue.time, '%Y') = #{year} " +
            "           GROUP BY ammeterId, queryMonth) subTable ) t1 " +
            " JOIN ( " +
            "       SELECT  DATE_FORMAT(date, '%Y-%m') queryMonth, SUM(reception) reception FROM cps_reception cr" +
            "       JOIN cps_scene cs ON cs.scene_id = cr.scene_id WHERE cs.scene_name = #{sceneName} AND DATE_FORMAT(date, '%Y') = #{year} " +
            "       GROUP BY queryMonth ) t2" +
            " ON t1.queryMonth = t2.queryMonth " +
            " ORDER BY queryMonth")
    List<Map<String, Object>> selectAvgPowerByNameGroupByMonth (@Param("sceneName") String sceneName, @Param("year") String year);

    /**
     * 根据设备ID查找设备的电表ID
     * @param unitId 设备Id
     * @return
     */
    @Select(" SELECT ammeter_id FROM cps_elec_unit WHERE elec_unit_id = #{unitId} ")
    @ResultType(String.class)
    String selectAmmeterIdByUnitId(@Param("unitId") String unitId);

    /**
     * 根据场景查询 电表ID
     * @param sceneName 场景
     * @return
     */
    @Select(" SELECT cbg.ammeter_id FROM cps_building_group cbg JOIN cps_scene cs ON cbg.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} ")
    @ResultType(String.class)
    List<String> selectAmmeterIdByScene(@Param("sceneName") String sceneName);

    /**
     * 根据电表ID 查找电的实时信息 电压 电流 功率
     * @param ammeterId 电表
     * @return
     */
    @Select(" SELECT t1.a_voltage voltageA, t1.b_voltage voltageB, t1.c_voltage voltageC, " +
            "        t2.a_elec_current ampereA, t2.b_elec_current ampereB, t2.c_elec_current ampereC, " +
            "        t3.a_active_power wattActiveA, t3.b_active_power wattActiveB, t3.c_active_power wattActiveC, " +
            "        t3.a_reactive_power wattReactiveA, t3.b_reactive_power wattReactiveB, t3.c_reactive_power wattReactiveC " +
            "   FROM  " +
            "       (SELECT * FROM cps_voltage WHERE ammeter_id = #{ammeterId} ORDER BY time DESC LIMIT 1) t1, " +
            "       (SELECT * FROM cps_elec_current WHERE ammeter_id = #{ammeterId} ORDER BY time DESC LIMIT 1) t2, " +
            "       (SELECT * FROM cps_power WHERE ammeter_id = #{ammeterId} ORDER BY time DESC LIMIT 1) t3 ")
    @ResultType(ElectricRealTimeInfo.class)
    ElectricRealTimeInfo selectElectricRealTimeByAmmeter(@Param("ammeterId") String ammeterId);

    /**
     * 根据场景名称获取 设备类型
     * @param sceneName 场景名称
     * @return
     */
    @Select(" SELECT DISTINCT elec_unit_type unitTypeKey " +
            " FROM cps_elec_unit ceu LEFT JOIN cps_building_group cbg ON ceu.building_group = cbg.building_id " +
            " LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id WHERE cs.scene_name = #{sceneName} ")
    List<Map<String, Object>> selectDistinctUnitTypeByScene(@Param("sceneName") String sceneName);

    /**
     * 根据场景和设备类型查询设备集合
     * @param type 类型
     * @param sceneName 场景
     * @return
     */
    @Select(" SELECT elec_unit_id unitId, elec_unit_name unitName " +
            " FROM cps_elec_unit ceu LEFT JOIN cps_building_group cbg ON ceu.building_group = cbg.building_id " +
            " LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} AND ceu.elec_unit_type = #{type} ")
    List<Map<String, Object>> selectUnitByTypeAndScene(@Param("sceneName") String sceneName, @Param("type") String type);

    /**
     * 查找场景的 楼宇群
     * @param sceneName
     * @return
     */
    @Select(" SELECT cbg.building_id buildingId, cbg.building_name buildingName FROM cps_building_group cbg " +
            " LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} ")
    List<Map<String, Object>> selectBuildingGroupsByScene(@Param("sceneName") String sceneName);

    /**
     * 查找场景的 设备
     * @param sceneName
     * @return
     */
    @Select(" SELECT ceu.elec_unit_id unitId, ceu.elec_unit_name unitName FROM cps_elec_unit ceu " +
            " LEFT JOIN cps_building_group cbg ON cbg.building_id = ceu.building_group " +
            " LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id " +
            " WHERE cs.scene_name = #{sceneName} ")
    List<Map<String, Object>> selectUnitByScene(@Param("sceneName") String sceneName);

    /**
     * 查询能耗分布
     * @param params
     *          year 年
     *          buildingId 楼宇群ID
     *          sceneName 场景
     *          type quarter-按季度 holiday-按节假日 season-按淡旺季 event-重大活动日
     * @return
     */
    @SelectProvider(type = GutianProvider.class, method = "selectPowerDistribute")
    List<Map<String, Object>> selectPowerDistribute(Map<String, Object> params);

    /**
     * 查询设备一天每小时能耗
     * @param unitId 设备ID
     * @param queryDate 天
     * @return
     */
    @Select(" SELECT DATE_FORMAT(cue.time, '%H') groupDate, MAX(cue.ammeter_value)-MIN(cue.ammeter_value) powerValue " +
            "  FROM  cps_use_electricity cue LEFT JOIN cps_elec_unit ceu ON cue.ammeter_id = ceu.ammeter_id " +
            " WHERE  ceu.elec_unit_id = #{unitId} AND DATE_FORMAT(cue.time, '%Y-%m-%d') = #{queryDate} " +
            "  GROUP BY groupDate ORDER BY groupDate")
    List<Map<String, Object>> selectPowerUseByUnit(@Param("unitId") String unitId, @Param("queryDate") String queryDate);

    /**
     * 查询时间范围内的用电量
     * @param sceneName 场景名称
     * @param startDateTime 开始时间
     * @param endDateTime 结束时间
     * @return
     */
    @Select(" SELECT SUM(t1.power) FROM (SELECT MAX(cue.ammeter_value)-MIN(cue.ammeter_value) power FROM cps_use_electricity cue " +
            "LEFT JOIN cps_building_group cbg ON cue.ammeter_id = cbg.ammeter_id " +
            " LEFT JOIN cps_scene cs ON cbg.scene_id = cs.scene_id WHERE cs.scene_name = #{sceneName} " +
            " AND cue.time >= STR_TO_DATE(#{startDateTime}, '%Y-%m-%d %H:%i') " +
            " AND cue.time <= STR_TO_DATE(#{endDateTime}, '%Y-%m-%d %H:%i') " +
            " GROUP BY cue.ammeter_id) t1")
    BigDecimal selectTotalPowerUseBySceneAndDate(@Param("sceneName") String sceneName,
                                                 @Param("startDateTime") String startDateTime,
                                                 @Param("endDateTime") String endDateTime);


    /**
     * 查询 楼宇群的 峰谷值
     * @param params
     *          sceneName 场景名称
     *          startDate 计算开始日期
     *          endDate 计算结束日期
     *          isPeak 是否为计算峰值
     * @return
     */
    @SelectProvider(type = GutianProvider.class, method = "selectPowerUseGroupByBuildings")
    List<Map<String, Object>> selectPowerUseGroupByBuildings(Map<String, Object> params);

    /**
     * 查询电功率 按楼宇分组
     * @param buildingId 所属楼宇群
     * @param queryDate 查询日期
     * @return
     */
    @Select(" SELECT cb.building_id buildingId, DATE_FORMAT(cp.time, '%H:%i') minTime, cb.building_name buildingName, " +
            "          ROUND(IFNULL(cp.active_power,0)*1000,2) watt" +
            "          FROM  cps_building cb  " +
            "      LEFT JOIN cps_power cp ON cb.ammeter_id = cp.ammeter_id WHERE cb.building_id = #{buildingId} " +
            "           AND  DATE_FORMAT(cp.time, '%Y-%m-%d') = #{queryDate} ")
    List<Map<String, Object>> selectWattGroupByBuilding(@Param("buildingId") String buildingId,
                                                        @Param("queryDate") String queryDate);

    /**
     * 查询 楼层级设备的 峰谷值
     * @param params
     *          sceneName 场景名称
     *          startDate 计算开始日期
     *          endDate 计算结束日期
     *          isPeak 是否为计算峰值
     * @return
     */
    @SelectProvider(type = GutianProvider.class, method = "selectPowerUseGroupByUnit")
    List<Map<String, Object>> selectPowerUseGroupByUnit(Map<String, Object> params);

    /**
     * 查询电功率 按设备分组
     * @param buildingId 所属楼宇群
     * @param queryDate 查询日期
     * @return
     */
    @Select(" SELECT ceu.elec_unit_id unitId, DATE_FORMAT(cp.time, '%H:%i') minTime, MAX(ceu.elec_unit_name) unitName, " +
            "          ROUND(IFNULL(AVG(cp.active_power),0)*1000,2) watt" +
            "          FROM  cps_elec_unit ceu  " +
            "      LEFT JOIN cps_power cp ON ceu.ammeter_id = cp.ammeter_id WHERE ceu.building_group = #{buildingId} " +
            "           AND  DATE_FORMAT(cp.time, '%Y-%m-%d') = #{queryDate} " +
            "    GROUP BY  unitId, minTime ")
    List<Map<String, Object>> selectWattGroupByUnit(@Param("buildingId") String buildingId,
                                                        @Param("queryDate") String queryDate);


    /**
     * 查询设备效率
     * @param unitId  设备ID
     * @param queryDate 查询日期
     * @return
     */
    @Select(" SELECT DATE_FORMAT(cp.time, '%H') groupTime,   " +
            " IF(IFNULL(AVG(cp.active_power),0)=0,null," +
            "  CONCAT(ROUND(0.00220/(IFNULL(AVG(cp.active_power),0))*100,2),'')) effic " +
            " FROM cps_power cp LEFT JOIN cps_elec_unit ceu ON cp.ammeter_id = ceu.ammeter_id " +
            " WHERE ceu.elec_unit_id = #{unitId} AND DATE_FORMAT(cp.time, '%Y-%m-%d') = #{queryDate} GROUP BY DATE_FORMAT(cp.time, '%H') ORDER BY groupTime ")
    List<Map<String, Object>> selectEfficByUnitGroupByHour(@Param("unitId") String unitId, @Param("queryDate") String queryDate);


    /**
     * 查询 电表的 一年用电量
     * @param ammeterIds 多个电表 逗号分隔
     * @param year 年
     * @return
     */
    @Select(" SELECT IFNULL(SUM(t1.powerValue),0) FROM ( SELECT MAX(cue.ammeter_value)-MIN(cue.ammeter_value) powerValue " +
            " FROM cps_use_electricity cue WHERE cue.ammeter_id IN (${ammeterIds})" +
            " AND DATE_FORMAT(cue.time, '%Y') = #{year} GROUP BY cue.ammeter_id) t1")
    BigDecimal selectPowerUseByAmmeterIds(@Param("ammeterIds") String ammeterIds, @Param("year") String year);

    /**
     * 获取数据库中当前年份之前得年份值
     * @param year 当前年份
     */
    @Select("select date_format(a.time, '%Y') years from cps_use_electricity a where DATE_FORMAT(a.time,'%Y') < #{year} GROUP BY DATE_FORMAT(a.time,'%Y')")
    List<Map<String, Object>> selectYearNum(@Param("year") String year);

    /**
     * 获取数据库中本年得当前月份之前得月份值
     * @param smonth 开始月份
     * @param emonth 结束月份
     */
    @Select("select date_format(a.time, '%m') months from cps_use_electricity a where DATE_FORMAT(a.time,'%Y-%m') >= #{smonth} AND DATE_FORMAT(a.time,'%Y-%m') < #{emonth} GROUP BY DATE_FORMAT(a.time,'%Y-%m')")
    List<Map<String, Object>> selectMonthNum(@Param("smonth") String smonth, @Param("emonth") String emonth);
    /**
     * 获取数据库中本月得当前天数之前得天数值
     * @param days 开始天数
     * @param daye 结束天数
     */
    @Select("select date_format(a.time, '%d') days from cps_use_electricity a where DATE_FORMAT(a.time, '%Y-%m-%d') >= #{sday} AND DATE_FORMAT(a.time, '%Y-%m-%d') < #{daye} GROUP BY DATE_FORMAT(a.time,'%Y-%m-%d')")
    List<Map<String, Object>> selectDayNum(@Param("sday") String days, @Param("daye") String daye);
    /**
     * 获取电能示值
     * @param params 开始天数
     */
    @SelectProvider(type = GutianProvider.class, method = "selectDnData")
    List<Map<String, Object>> selectDnData(Map<String, Object> params);
    /**
     * 根据场景id获取电表id
     * @param type 开始天数
     */
    @Select("SELECT a.ammeter_id ammeter_id from cps_building_group a WHERE a.scene_id = #{type}")
    List<Map<String, Object>> selectAmeterIdBysence(@Param("type") String type);

    /**
     * 获取数据库中当前年份之前得年份值
     * @param year 当前年份
     */
    @Select("select date_format(a.time, '%Y') years from cps_use_electricity_agriculture a where DATE_FORMAT(a.time,'%Y') < #{year} GROUP BY DATE_FORMAT(a.time,'%Y')")
    List<Map<String, Object>> selectNyYearNum(@Param("year") String year);

    /**
     * 获取数据库中本年得当前月份之前得月份值
     * @param smonth 开始月份
     * @param emonth 结束月份
     */
    @Select("select date_format(a.time, '%m') months from cps_use_electricity_agriculture a where DATE_FORMAT(a.time,'%Y-%m') >= #{smonth} AND DATE_FORMAT(a.time,'%Y-%m') < #{emonth} GROUP BY DATE_FORMAT(a.time,'%Y-%m')")
    List<Map<String, Object>> selectNyMonthNum(@Param("smonth") String smonth, @Param("emonth") String emonth);
    /**
     * 获取数据库中本月得当前天数之前得天数值
     * @param days 开始天数
     * @param daye 结束天数
     */
    @Select("select date_format(a.time, '%d') days from cps_use_electricity_agriculture a where DATE_FORMAT(a.time, '%Y-%m-%d') >= #{sday} AND DATE_FORMAT(a.time, '%Y-%m-%d') < #{daye} GROUP BY DATE_FORMAT(a.time,'%Y-%m-%d')")
    List<Map<String, Object>> selectNyDayNum(@Param("sday") String days, @Param("daye") String daye);
    /**
     * 获取电能示值
     * @param params 开始天数
     */
    @SelectProvider(type = GutianProvider.class, method = "selectDnData")
    List<Map<String, Object>> selectNyDnData(Map<String, Object> params);
    /**
     * 根据场景id获取电表id
     * @param type 开始天数
     */
    @Select("SELECT a.ammeter_id ammeter_id from cps_ammeter_agriculture a WHERE a.base_type = #{type}")
    List<Map<String, Object>> selectNyAmeterIdBysence(@Param("type") String type);

    /**
     * 获取重要事件得时间
     */
    @Select("SELECT\n" +
            "	DATE_FORMAT(a.date,'%Y-%m-%d') date\n" +
            "FROM\n" +
            "	cps_reception a\n" +
            "WHERE\n" +
            "	a.is_event = '2'\n" +
            "AND DATE_FORMAT(a.date,'%Y-%m')BETWEEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), interval 6 MONTH),'%Y-%m') AND  DATE_FORMAT(DATE_SUB(CURRENT_DATE(), interval 1 MONTH),'%Y-%m') GROUP BY date ORDER BY date;")
    List<Map<String, Object>> selectdateFromReception();
    /**
     * 重要事件得时间获取当天的峰值谷值
     */
    @Select("SELECT\n" +
            "	(IFNULL(SUM(IF((DATE_FORMAT(b.time,'%Y-%m-%d %H:%i:%s') = #{ktime}),b.ammeter_value,0)),0) - IFNULL(SUM(IF((DATE_FORMAT(b.time,'%Y-%m-%d %H:%i:%s') = #{etime}),b.ammeter_value,0)),0)) fz,\n" +
            "	(IFNULL(SUM(b.ammeter_value),0) - (IFNULL(SUM(IF((DATE_FORMAT(b.time,'%Y-%m-%d %H:%i:%s') = #{ktime}),b.ammeter_value,0)),0) - IFNULL(SUM(IF((DATE_FORMAT(b.time,'%Y-%m-%d %H:%i:%s') = #{etime}),b.ammeter_value,0)),0))) gz\n" +
            "FROM\n" +
            "	cps_use_electricity b\n" +
            "WHERE\n" +
            "	b.date = STR_TO_DATE(#{date},'%Y-%m-%d') AND b.ammeter_id in (SELECT a.ammeter_id from cps_building_group a WHERE a.scene_id = #{type})")
    List<Map<String, Object>> getLast6MotnData(Map<String, Object> map);

    /**
     * 查询楼宇群下面的楼宇
     * @param buildingGroupId
     * @return
     */
    @Select("SELECT building_id buildingId, building_name buildingName " +
            " FROM cps_building WHERE building_group = #{buildingGroupId}")
    List<Map<String, Object>> selectBuildingByBuildingGroup(@Param("buildingGroupId") String buildingGroupId);

}

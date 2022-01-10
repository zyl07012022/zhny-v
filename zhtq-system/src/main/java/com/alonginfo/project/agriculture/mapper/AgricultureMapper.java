package com.alonginfo.project.agriculture.mapper;

import com.alonginfo.project.agriculture.domain.Weather;
import com.alonginfo.project.agriculture.provider.AgricultureProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @Description 农业场景 mapper
 * @Author Yalon
 * @Date 2020-04-14 16:52
 * @Version mxdata_v
 */
@Mapper
public interface AgricultureMapper {

    /**
     * 查找 所有的 鸡舍 或者 大棚 用作页面上的下拉框
     * @param tableName 表名
     * @param houseKey 鸡舍或大棚主键
     * @param houseName 鸡舍或大棚名字
     * @return houseKey houseName
     */
    @Select(" SELECT DISTINCT ${houseKey} houseKey, ${houseName} houseName FROM ${tableName} " +
            "WHERE ( ${houseKey} != 10011 AND base_id = 1) OR ( ${houseKey} != 10031 AND base_id = 2) ")
    List<Map<String, Object>> selectHouseOptionsByTableInfo(@Param("tableName") String tableName,
                                                               @Param("houseKey") String houseKey,
                                                               @Param("houseName") String houseName);

    /**
     * 获取最新气象数据
     * @return
     */
    Weather selectWeather();

    /**
     * 贵妃鸡养殖场用能监视
     * @param type
     * @param houseKey
     * @return
     */
    List<Map<String,Object>> selectChickhouseWatch(@Param("type")String type, @Param("houseKey") String houseKey);

    /**
     * 石斛大棚用能监视
     * @param type
     * @param houseKey
     * @return
     */
    List<Map<String, Object>> selectGreenhouseWatch(@Param("type")String type, @Param("houseKey")String houseKey);

    /**
     * 查询鸡舍环境值与阈值
     * @return
     */
    List<Map<String, Object>> selectChickenWarn(@Param("baseType") String baseType);

    /**
     * 查询监测点值
     * @return
     */
    List<Map<String, Object>> selectWatch(@Param("baseType") String baseType);

    /**
     * 鸡舍/石斛 设备数
     * @return
     */
    Integer selectUnitChichenhouse();
    Integer selectUnitPlant();

    /**
     * 年度鸡蛋产出与鸡产出
     * @return
     */
    Map<String, Object> selectOutPut();
    Float selectPlantOutPut();
    /**
     * 年度累计用电
     * @return
     */
    Float selectelecData(@Param("baseType") String baseType);

    /**
     * 查询历史耗能
     * @param startTime
     * @param endTime
     * @return
     */
    Float selectHistoryEle(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("baseType") String baseType);

    /**
     * 能耗成本分析
     * @param baseType
     * @param type
     * @return
     */
    List<Map<String, Object>> getPowerDistribute(@Param("baseType") String baseType, @Param("type") String type);

    /**
     * 每月耗电
     * @param baseType
     * @return
     */
    List<Map<String, Object>> selectEleMonth(@Param("baseType") String baseType);

    /**
     * 查询产出数据
     * @return
     */
    List<Map<String, Object>> selectOutputData(@Param("baseType") String baseType);

    /**
     * 近一周水分饱和度 与 土壤温度预警
     * @return
     */
    List<Map<String, Object>> trChartWaring(@Param("houseKey") String houseKey);

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
    @SelectProvider(type = AgricultureProvider.class, method = "selectPowerUseGroupByType")
    List<Map<String, Object>> selectPowerUseGroupByType(Map<String, Object> params);

    /**
     * 查询用电功率
     * @param baseType  基地类型
     * @param queryDate 查询日期
     * @return
     */
    @Select(" SELECT DATE_FORMAT(cp.time, '%H') groupTime,  ROUND(IFNULL(AVG(cp.active_power),0)*1000,2) watt " +
            " FROM cps_power_agriculture cp LEFT JOIN cps_ammeter_agriculture caa ON cp.ammeter_id = caa.ammeter_id  " +
            " WHERE caa.base_type = #{baseType} AND DATE_FORMAT(cp.time, '%Y-%m-%d') = #{queryDate} GROUP BY DATE_FORMAT(cp.time, '%H') ORDER BY groupTime ")
    List<Map<String, Object>> selectWattByNameGroupByHour(@Param("baseType") String baseType, @Param("queryDate") String queryDate);

    /**
     * 查询设备能耗信息
     * @param baseType 1-鸡舍 2-大棚
     * @return
     */
    List<Map<String, Object>> selectUnitPowerUse(@Param("baseType") String baseType);

    /**
     * 查询风机
     * @param houseId
     * @return
     */
    Map<String, Object> selectWindMachine(@Param("houseId") String houseId, @Param("type") String type);

    /**
     * 查询 产耗指数
     * @param baseType
     * @return
     */
    List<Map<String, Object>> selectOPByBaseType(@Param("baseType") String baseType);

    /**
     * 查询能耗指数
     * @param baseType
     * @return
     */
    List<Map<String, Object>> selectPRByBaseType(@Param("baseType") String baseType);
}

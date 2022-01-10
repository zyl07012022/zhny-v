package com.alonginfo.project.agriculture.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * app农业场景数据接口层
 * @author shidj
 * @date 2020年5月8日11:49:38
 */
public interface AppAgricultureMapper {

    /**
     * 石斛场景
     * @return
     */
    List<Map<String, Object>> selectPlantWatch();

    /**
     * 石斛
     * @return
     */
    List<Map<String, Object>> selectChickenWatch();

    /**
     * 根据监测点id获取数据详情
     * 石斛场景
     * @param watchId
     * @return
     */
    Map<String, Object> getPlantWatchForId(@Param("watchId") String watchId);

    /**
     * 根据监测点id获取监测点数据详情
     * 养殖场景
     * @param watchId
     * @return
     */
    Map<String, Object> getChickenWatchForId(@Param("watchId") String watchId);

    /**
     * 养殖场检测点数据获取
     * @return
     */
    List<Map<String, Object>> selectPlantWaringWatch();

    /**
     * 养殖场景检测点数据获取
     * @return
     */
    List<Map<String, Object>> selectChickenWaringWatch();

    /**
     * 查询一个月设备能耗
     * @param baseType
     * @param rows
     * @return
     */
    List<Map<String, Object>> selectUnitPower(@Param("baseType") String baseType, @Param("rows") Integer rows);

    /**
     * 查询house 近况
     * @param baseType
     * @param rows
     * @return
     */
    List<Map<String, Object>> selectHouseSurvey(@Param("baseType") String baseType, @Param("rows") Integer rows);


    /**
     * 删除鸡舍产出
     * @param outputList
     * @return
     */
    Integer deleteChickenhouseOutput(List<Map<String, Object>> outputList);

    /**
     * 删除大棚产出
     * @param outputList
     * @return
     */
    Integer deleteGreenhouseOutput(List<Map<String, Object>> outputList);
    /**
     * 插入鸡舍产出
     * @param outputList
     * @return
     */
    Integer insertChickenhouseOutput(List<Map<String, Object>> outputList);

    /**
     * 插入鸡舍产出
     * @param outputList
     * @return
     */
    Integer insertGreenhouseOutput(List<Map<String, Object>> outputList);
}

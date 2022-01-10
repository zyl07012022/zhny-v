package com.alonginfo.project.agriculture.service;

import com.alonginfo.project.agriculture.domain.Weather;

import java.util.List;
import java.util.Map;

/**
 * @Description 农业场景 服务层接口
 * @Author Yalon
 * @Date 2020-04-14 17:19
 * @Version mxdata_v
 */
public interface IAgricultureService {

    /**
     * 查找鸡舍或大棚的 下拉框选项
     * @param houseType chickenHouse 鸡舍   greenHouse 大棚
     * @return houseKey houseName
     */
    List<Map<String, Object>> queryHouseOptionsByType(String houseType);

    /**
     * 获取数据库中最新一条数据
     * @return
     */
    Weather selectNew();

    /**
     * 查询用能监测值
     * @param sceneName
     * @param type
     * @return
     */
    Map<String,List<String>> queryWatchValue(String sceneName, String type, String houseKey);

    /**
     * 鸡舍环境预警
     * @param sceneName
     * @return
     */
    Map<String,Object> chickenWarn(String sceneName);

    /**
     * 鸡舍用能设备、产出情况、累计用电、不间断用能、累计用电费用
     * @return
     */
    Map<String,Object> outputData();

    /**
     * 石斛产出查询
     * @return
     */
    Map<String,Object> outputDataPlant();

    /**
     * 历史耗能
     * @return
     */
    Map<String,Object> SelectEleHistoryByScene(String baseType);

    /**
     * 能耗成本分析
     * @param sceneName
     * @param type
     * @return
     */
    List<Map<String, Object>> getPowerDistribute(String sceneName,String type);

    /**
     * 能耗指标
     * @param baseType
     * @return
     */
    Map<String,List<String>> SelectEleIndex(String baseType);

    /**
     * 近一周水分饱和度 与 土壤温度预警曲线
     * @return
     */
    Map<String,List<String>> trChartWaring(String houseKey);

    /**
     * 查询峰谷值
     * @param baseType
     * @param type
     * @return
     */
    Map<String, Object> queryPeakValleyByNameAndType(String baseType, String type);

    /**
     * 查询
     * @param baseType
     * @return
     */
    Map<String, Object> queryWattByName(String baseType);

    /**
     * 查询设备能耗信息
     * @param baseType 1-鸡舍 2-大棚
     * @return
     */
    List<Map<String, Object>> queryUnitPowerUse(String baseType);

    /**
     * 查询风机状态
     * @param houseId
     * @param type
     * @return
     */
    Map<String, Object> queryWindMachine(String houseId, String type);


    /**
     * 查询产耗指数
     * @param baseType
     * @return
     */
    List<Map<String, Object>> queryOpByBaseType(String baseType);

    /**
     * 查询能耗指数
     * @param baseType
     * @return
     */
    Map<String, Object> queryPrByBaseType(String baseType);
}

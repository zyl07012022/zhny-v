package com.alonginfo.project.agriculture.service;

import java.util.List;
import java.util.Map;

/**
 * app农业场景接口层
 * @author shidj
 * @date 2020年5月8日11:48:21
 */
public interface IAppAgricultureService {

    /**
     * 查询所以监测点及最近一条数据
     * @param sceneName
     * @return
     */
    List<Map<String,Object>> selectWatch(String sceneName);

    /**
     * 根据id获取监测点详情及最近一条数据
     * @param watchId
     * @return
     */
    Map<String,Object> getWatchForId(String watchId, String sceneName);

    /**
     * 获取异常检测点
     * @param sceneName
     * @return
     */
    List<Map<String,Object>> getWaringWatch(String sceneName);

    /**
     * 查询一个月设备能耗
     * @param baseType
     * @param rows 条数
     * @return
     */
    List<Map<String, Object>> getUnitPower(String baseType, Integer rows);

    /**
     * 查询house近况 --首页
     * @param baseType
     * @param rows
     * @return
     */
    List<Map<String, Object>> getHouseSurvey(String baseType, Integer rows);

    /**
     * 增加产出
     * @param baseType
     * @return
     */
    Integer addOutput(String baseType, List<Map<String, Object>> outputList);
}

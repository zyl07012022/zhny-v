package com.alonginfo.project.gutian.service;

import java.util.List;
import java.util.Map;

/**
 * @author LM
 * @create 2020-05-25 9:53
 */
public interface IGutianAppService {
    /**
     * 当前园区用电功率
     * @return
     */
    String getDqyqgl(String sceneId);

    /**
     * 当前园区累积能耗
     * @return
     */
    String getDqyqljnh(String sceneId);

    /**
     * 能耗指标
     * @param date
     * @return
     */
    Map<String,String> getNhzb(String date,String sceneId);

    /**
     * 古田干部学院-楼宇群用能监测
     * @return
     */
    List<Map<String,String>> getLyqynjc(String sceneId);

    /**
     * 楼层级电气设备用能监测
     * @param sceneId
     * @return
     */
    List<Map<String,String>> getLcjdqsbynjc(String sceneId);

    /**
     * 楼宇区域监测
     * @param buildingId
     * @return
     */
    Map<String,Object> getLyqyjc(String buildingId);

    /**
     * 楼层级电气设备用能监测明细
     * @param elecUnitId
     * @param buildingGroup
     * @return
     */
    Map<String,Object> getLyqyjcmx(String elecUnitId, String buildingGroup);

    /**
     * 用能-设备能耗
     * @param sceneId
     * @return
     */
    List<Map<String,Object>> getSbnh(String sceneId);

    /**
     *保存接待信息
     * @param sceneId
     * @param list
     * @return
     */
    Map<String, Object> insertGTSZData(String sceneId, List<Map<String, Object>> list);
}

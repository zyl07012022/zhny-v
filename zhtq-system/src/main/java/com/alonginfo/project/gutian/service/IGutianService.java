package com.alonginfo.project.gutian.service;

import com.alonginfo.project.gutian.domain.ElectricHistoryInfo;
import com.alonginfo.project.gutian.domain.ElectricRealTimeInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description 古田三大场景业务层接口
 * @Author Yalon
 * @Date 2020-04-15 15:38
 * @Version mxdata_v
 */
public interface IGutianService {

    /**
     * 查询场景的 楼宇数量 建筑面积 年度累计用电 接待人数 供能线路 累计用电费用
     *
     * @param sceneName 场景名字
     * @return buildingNum 楼宇数量 sceneAcreage 建筑面积 wireNum 供能线路数 reception 接待人数
     *                     powerYear 年度累计用电 powerCost 累计用电费用  eventCount 重大活动天数
     */
    Map<String, Object> querySceneInfoByName(String sceneName);

    /**
     * 查询场景的峰谷值
     * @param sceneName 场景名字
     * @param type 查询类型 DAY,WEEK,MONTH,YEAR
     * @return
     */
    Map<String, Object> queryPeakValleyByNameAndType(String sceneName, String type);

    /**
     * 查询场景的用电功率
     * @param sceneName 场景名称
     * @return
     */
    Map<String, Object> queryWattByName(String sceneName);

    /**
     * 查询人均耗电 每平米耗电曲线
     * @param sceneName 场景名称
     * @return
     */
    Map<String, Object> queryAvgPowerByName(String sceneName);

    /**
     * 根据设备ID查找电力实时信息 电压电流功率
     *
     * @param unitId 设备ID 若为空 则查找 古田会议纪念馆的电表的值
     * @return
     */
    ElectricRealTimeInfo queryElectricRealTime(String unitId);

    /**
     * 根据场景 查询设备类型集合 用作下拉框
     * @param sceneName 场景
     * @return
     */
    List<Map<String, Object>> queryUnitTypeOptions(String sceneName);

    /**
     * 根据场景和设备类型 查询设备集合 用作下拉框
     * @param sceneName 场景
     * @param unitType 设备类型
     * @return
     */
    List<Map<String, Object>> queryUnitOptions(String sceneName, String unitType);

    /**
     * 根据场景 获取楼宇群信息
     * @param sceneName 场景
     * @return
     */
    List<Map<String, Object>> queryBuildingGroupsByScene(String sceneName);

    /**
     * 查询能耗成本分布饼状图
     * @param sceneName 场景名称
     * @param buildingId 楼宇群ID  查全部则为空
     * @param type 查询类型 按季度 按淡旺季 按节假日 quarter-按季度 holiday-按节假日 season-按淡旺季
     * @return
     */
    List<Map<String, Object>> queryPowerDistribute(String sceneName, String buildingId, String type);

    /**
     * 查询历史能耗
     * @param sceneName 场景名称
     * @return
     */
    ElectricHistoryInfo queryElectricHistoryByScene(String sceneName);

    /**
     * 查询峰谷值用电量 按楼宇群分组
     * @param sceneName 场景名
     * @param type DAY WEEK MONTH YEAR
     * @return
     */
    Map<String, Object> queryPowerUseGroupByBuildings(String sceneName, String type);

    /**
     * 查询电功率 按楼宇分组
     * @param buildingId 楼宇群
     * @return
     */
    Map<String, Object> queryWattGroupByBuilding(String buildingId);

    /**
     * 查询峰谷值用电量 按楼层级设备分组
     * @param sceneName 场景名
     * @param type DAY WEEK MONTH YEAR
     * @return
     */
    Map<String, Object> queryPowerUseGroupByUnit(String sceneName, String type);

    /**
     * 查询电功率 按设备分组
     * @param buildingId 楼宇群
     * @return
     */
    Map<String, Object> queryWattGroupByUnit(String buildingId);

    /**
     * 查询设备效率
     * @param unitId 设备ID
     * @return
     */
    Map<String, Object> queryEfficByUnit(String unitId);

    /**
     * 查询古田山庄一年 耗能 业务分布
     * @return
     */
    Map<String, Object> querySzBusiUse();

    /**
     * 查询干部学院一年 耗能 业务分布
     * @return
     */
    Map<String, Object> queryGbxyBusiUse();

    Map<String, Object> getYnjcEchartData(String sceneName);


}

package com.alonginfo.project.gutian.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.project.gutian.constant.UnitType;
import com.alonginfo.project.gutian.domain.ElectricHistoryInfo;
import com.alonginfo.project.gutian.domain.ElectricRealTimeInfo;
import com.alonginfo.project.gutian.mapper.GutianMapper;
import com.alonginfo.project.gutian.service.IGutianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alonginfo.project.gutian.constant.SceneConstant.*;

/**
 * @Description 三大业务场景 业务层实现
 * @Author Yalon
 * @Date 2020-04-15 15:48
 * @Version mxdata_v
 */
@Service
public class GutianServiceImpl implements IGutianService {

    @Autowired
    private GutianMapper gutianMapper;

    /**
     * 查询场景的 楼宇数量 建筑面积 年度累计用电 接待人数 供能线路 累计用电费用
     *
     * @param sceneName 场景名字
     * @return buildingNum 楼宇数量 sceneAcreage 建筑面积 wireNum 供能线路数 reception 接待人数
     * powerYear 年度累计用电 powerCost 累计用电费用  eventCount 重大活动天数
     */
    @Override
    public Map<String, Object> querySceneInfoByName(String sceneName) {
        //当前年
        String year = String.valueOf(LocalDate.now().getYear());
        //查找 楼宇数 建筑面积 供能线路
        Map<String, Object> sceneInfo = gutianMapper.selectSceneBaseInfoByName(sceneName);
        //查找 年度累计用电
        BigDecimal powerYear = Optional.ofNullable(gutianMapper.selectPowerYearByNameAndYear(sceneName, year))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);

        sceneInfo.put("reception", Optional.ofNullable(gutianMapper.selectReceptionByNameAndYear(sceneName, year)).orElse(0));
        sceneInfo.put("powerYear", powerYear);
        sceneInfo.put("eventCount", gutianMapper.selectEventCountByNameAndYear(sceneName, year));
        sceneInfo.put("days", gutianMapper.selectReceptionCountByNameAndYear(sceneName, year));
        //TODO 计算累计用电费用 计算规则暂不知
        BigDecimal eleCharge = BigDecimal.valueOf(0.6206);
        sceneInfo.put("powerCost", eleCharge.multiply(powerYear).setScale(2, BigDecimal.ROUND_HALF_UP));
        return sceneInfo;
    }

    /**
     * 查询场景的峰谷值
     *
     * @param sceneName 场景名字
     * @param type      查询类型 DAY,WEEK,MONTH,YEAR
     * @return
     */
    @Override
    public Map<String, Object> queryPeakValleyByNameAndType(String sceneName, String type) {
        //计算横坐标的值
        List<String> dateList = calcDateByType(type);
        //查询开始结束日期
        String[] startAndEndDate = calcStartAndEndTime(type);
        //查询全部用电量
        Map<String, BigDecimal> totalPowerUse = handleQueryPeakValleyResult(sceneName, startAndEndDate[0],
                startAndEndDate[1], startAndEndDate[2], false);
        //查询峰值用电量
        Map<String, BigDecimal> peakPowerUse = handleQueryPeakValleyResult(sceneName, startAndEndDate[0],
                startAndEndDate[1], startAndEndDate[2], true);

        //构造返回map
        List<String> totalPowerList = new ArrayList<>();
        List<String> peakPowerList = new ArrayList<>();
        List<String> valleyPowerList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();

        //循环数据
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            BigDecimal total = Optional.ofNullable(totalPowerUse.get(key)).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal peak = Optional.ofNullable(peakPowerUse.get(key)).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal valley = total.subtract(peak).setScale(2, BigDecimal.ROUND_HALF_UP);
            abscissaList.add(filterAbscissa(key, type, i));
            if (!TYPE_DAY.equals(type)) {
                peakPowerList.add(peak.toString());
                valleyPowerList.add(valley.toString());
            } else if (i < Integer.parseInt(PEAK_HOUR_START) || i >= Integer.parseInt(PEAK_HOUR_END)) {
                peakPowerList.add("");
                valleyPowerList.add(valley.toString());
            } else {
                valleyPowerList.add("");
                peakPowerList.add(peak.toString());
            }
        });

        //返回
        Map<String, Object> resultMap = new HashMap<>(6);
        resultMap.put("totalPower", totalPowerList);
        resultMap.put("peakPower", peakPowerList);
        resultMap.put("valleyPower", valleyPowerList);
        resultMap.put("abscissa", abscissaList);
        return resultMap;
    }

    /**
     * 查询场景的用电功率
     *
     * @param sceneName 场景名称
     * @return
     */
    @Override
    public Map<String, Object> queryWattByName(String sceneName) {
        //计算横坐标
        List<String> dateList = calcDateByType(TYPE_DAY);
        //查询电功率
        Map<String, Double> watts = gutianMapper.selectWattByNameGroupByHour(sceneName, LocalDate.now().format(FORMAT_DATE))
                .stream().collect(Collectors.toMap(map -> (String) map.get("groupTime"),
                        map -> (Double) map.get("watt"), (k1, k2) -> k1));
        //构造返回参数
        List<String> wattList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理数据
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            abscissaList.add((Integer.parseInt(key)) + ":00");
            wattList.add(Optional.ofNullable(watts.get(key)).orElse(0.0).toString());
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("wattList", wattList);
        resultMap.put("abscissaList", abscissaList);
        return resultMap;
    }

    /**
     * 查询人均耗电 每平米耗电曲线
     *
     * @param sceneName 场景名称
     * @return
     */
    @Override
    public Map<String, Object> queryAvgPowerByName(String sceneName) {
        //计算横坐标
        List<String> dateList = calcDateByType(TYPE_YEAR);
        //查询平均电量所需要的数据
        Map<String, Map<String, Object>> avgData = gutianMapper.selectAvgPowerByNameGroupByMonth(sceneName, String.valueOf(LocalDate.now().getYear()))
                .stream().collect(Collectors.toMap(map -> (String) map.get("queryMonth"), a -> a, (k1, k2) -> k1));
        //查找场景面积
        BigDecimal acreage = BigDecimal.valueOf(Optional.ofNullable((Float) gutianMapper.selectSceneBaseInfoByName(sceneName)
                .get("sceneAcreage")).orElse(Float.parseFloat("0")));
        //构造返回参数
        List<BigDecimal> avgPersonList = new ArrayList<>();
        List<BigDecimal> avgAcreageList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            Map<String, Object> avg = Optional.ofNullable(avgData.get(key)).orElse(new HashMap<>(2));
            BigDecimal avgPerson = BigDecimal.ZERO;
            BigDecimal avgAcreage = BigDecimal.ZERO;
            BigDecimal power = BigDecimal.valueOf(Optional.ofNullable((Double) avg.get("power")).orElse(0.0));
            BigDecimal person = (BigDecimal) avg.get("reception");
            if (person != null && BigDecimal.ZERO.compareTo(person) != 0) {
                //人数不为空可以计算人均用电
                avgPerson = power.divide(person, 2, BigDecimal.ROUND_HALF_UP);
            }
            if (BigDecimal.ZERO.compareTo(acreage) != 0) {
                //面积不为空可以计算每平米用电
                avgAcreage = power.divide(acreage, 2, BigDecimal.ROUND_HALF_UP);
            }
            abscissaList.add(key);
            avgPersonList.add(avgPerson);
            avgAcreageList.add(avgAcreage);
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(5);
        resultMap.put("avgPersonList", avgPersonList);
        resultMap.put("avgAcreageList", avgAcreageList);
        resultMap.put("abscissaList", abscissaList);
        return resultMap;
    }

    /**
     * 根据设备ID查找电力实时信息 电压电流功率
     *
     * @param unitId 设备ID 若为空 则查找 古田会议纪念馆的电表的值
     * @return
     */
    @Override
    public ElectricRealTimeInfo queryElectricRealTime(String unitId) {
        String ammeterId = null;
        if (StringUtils.isEmpty(unitId)) {
            //没有传设备ID 说明 查询的是古田会议纪念馆的实时信息 先查询古田会议纪念馆的电表
            List<String> ammeterIds = gutianMapper.selectAmmeterIdByScene(GUTIAN_MEMORIAL);
            if (StringUtils.isNotEmpty(ammeterIds)) {
                ammeterId = ammeterIds.get(0);
            }
        } else {
            //有设备ID 根据设备ID查询设备所属的电表
            ammeterId = gutianMapper.selectAmmeterIdByUnitId(unitId);
        }
        //根据电表查实时信息
        return gutianMapper.selectElectricRealTimeByAmmeter(ammeterId);
    }

    /**
     * 根据场景 查询设备类型集合 用作下拉框
     *
     * @param sceneName 场景
     * @return
     */
    @Override
    public List<Map<String, Object>> queryUnitTypeOptions(String sceneName) {
        List<Map<String, Object>> unitTypeList = gutianMapper.selectDistinctUnitTypeByScene(sceneName);
        unitTypeList.stream().forEach(map ->  map.put("unitTypeName", UnitType
                .getEnum(String.valueOf(map.get("unitTypeKey") == null ? "null" : map.get("unitTypeKey"))).getName())
        );
        return unitTypeList;
    }

    /**
     * 根据场景和设备类型 查询设备集合 用作下拉框
     *
     * @param sceneName 场景
     * @param unitType  设备类型
     * @return
     */
    @Override
    public List<Map<String, Object>> queryUnitOptions(String sceneName, String unitType) {
        return gutianMapper.selectUnitByTypeAndScene(sceneName, unitType);
    }

    /**
     * 根据场景 获取楼宇群信息
     *
     * @param sceneName 场景
     * @return
     */
    @Override
    public List<Map<String, Object>> queryBuildingGroupsByScene(String sceneName) {
        return gutianMapper.selectBuildingGroupsByScene(sceneName);
    }

    /**
     * 查询能耗成本分布饼状图
     *
     * @param sceneName  场景名称
     * @param buildingId 楼宇群ID  查全部则为空
     * @param type       查询类型 按季度 按淡旺季 按节假日 quarter-按季度 holiday-按节假日 season-按淡旺季
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPowerDistribute(String sceneName, String buildingId, String type) {
        Map<String, Object> params = new HashMap<>(5);
        params.put("sceneName", sceneName);
        params.put("buildingId", buildingId);
        params.put("type", type);
        params.put("year", String.valueOf(LocalDate.now().getYear()));
        return gutianMapper.selectPowerDistribute(params);
    }


    /**
     * 查询历史能耗
     *
     * @param sceneName 场景名称
     * @return
     */
    @Override
    public ElectricHistoryInfo queryElectricHistoryByScene(String sceneName) {
        String zeroClock = " 00:00";
        //当前时分
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(" HH:mm"));
        //当前日期
        LocalDate today = LocalDate.now();
        //本月第一天
        LocalDate month = today.with(TemporalAdjusters.firstDayOfMonth());

        ElectricHistoryInfo historyInfo = new ElectricHistoryInfo();
        //今日用电量
        historyInfo.setTodayPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                today.format(FORMAT_DATE) + zeroClock, today.format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        //上月同期用电量
        historyInfo.setLastMonthDayPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                today.plusMonths(-1).format(FORMAT_DATE) + zeroClock, today.plusMonths(-1).format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        //昨日用电量
        historyInfo.setYesterdayPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                today.plusDays(-1).format(FORMAT_DATE) + zeroClock, today.plusDays(-1).format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        //本月用电量
        historyInfo.setThisMonthPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                month.format(FORMAT_DATE) + zeroClock, today.format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        //去年同期用电量
        historyInfo.setLastYearMonthPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                month.plusYears(-1).format(FORMAT_DATE) + zeroClock, today.plusYears(-1).format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        //上月用电量
        historyInfo.setLastMonthPower(Optional.ofNullable(gutianMapper.selectTotalPowerUseBySceneAndDate(sceneName,
                month.plusMonths(-1).format(FORMAT_DATE) + zeroClock, today.plusMonths(-1).format(FORMAT_DATE) + time))
                .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
        return historyInfo;
    }

    /**
     * 查询峰谷值用电量 按楼宇群分组
     *
     * @param sceneName 场景名
     * @param type      DAY WEEK MONTH YEAR
     * @return
     */
    @Override
    public Map<String, Object> queryPowerUseGroupByBuildings(String sceneName, String type) {
        //获取计算开始结束日期
        String[] startAndEnd = calcStartAndEndTime(type);
        //查找所有楼宇群
        List<String> buildingList = gutianMapper.selectBuildingGroupsByScene(sceneName).stream()
                .map(map -> (String) map.get("buildingName")).collect(Collectors.toList());
        //计算总电量
        Map<String, BigDecimal> totalPowerUse = handleQueryBuildingPeakValley(sceneName, startAndEnd[1], startAndEnd[2], false);
        //计算峰值
        Map<String, BigDecimal> peakPowerUse = handleQueryBuildingPeakValley(sceneName, startAndEnd[1], startAndEnd[2], true);
        //构造返回
        List<String> totalPowerList = new ArrayList<>();
        List<String> peakPowerList = new ArrayList<>();
        List<String> valleyPowerList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理
        IntStream.range(0, buildingList.size()).forEach(i -> {
            String key = buildingList.get(i);
            BigDecimal total = Optional.ofNullable(totalPowerUse.get(key))
                    .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal peak = Optional.ofNullable(peakPowerUse.get(key))
                    .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal valley = total.subtract(peak).setScale(2, BigDecimal.ROUND_HALF_UP);

            abscissaList.add(key);
            totalPowerList.add(total.toString());
            peakPowerList.add(peak.toString());
            valleyPowerList.add(valley.toString());
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(6);
        resultMap.put("totalPower", totalPowerList);
        resultMap.put("peakPower", peakPowerList);
        resultMap.put("valleyPower", valleyPowerList);
        resultMap.put("abscissa", abscissaList);
        return resultMap;
    }

    /**
     * 查询电功率 按楼宇分组
     *
     * @param buildingId 楼宇群
     * @return
     */
    @Override
    public Map<String, Object> queryWattGroupByBuilding(String buildingId) {
        //计算横坐标
        List<String> dateList = calcDateByType(TYPE_HOUR);
        //查询楼宇群的楼宇
        List<Map<String, Object>> buildings = gutianMapper.selectBuildingByBuildingGroup(buildingId);
        //构造返回参数
        Map<String, List<String>> wattBuildingMap = new HashMap<>(16);

        //循环楼宇 查询楼宇的值
        buildings.forEach(buildingMap -> {
            //查询
            Map<String, Double> watts = gutianMapper.selectWattGroupByBuilding(buildingMap.get("buildingId").toString(),
                    LocalDate.now().format(FORMAT_DATE)).stream().collect(Collectors.toMap(
                    map -> (String) map.get("minTime"), map -> (Double) map.get("watt"), (k1, k2) -> k1
            ));
            //构造

            List<String> list = new ArrayList<>();
            IntStream.range(0, dateList.size()).forEach(i ->
                    list.add(Optional.ofNullable(watts.get(dateList.get(i))).orElse(0.0000).toString())
            );
            wattBuildingMap.put((String) buildingMap.get("buildingName"), list);
        });

        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("abscissa", dateList);
        resultMap.put("wattBuildingMap", wattBuildingMap);
        return resultMap;
    }

    /**
     * 查询峰谷值用电量 按楼层级设备分组
     *
     * @param sceneName 场景名
     * @param type      DAY WEEK MONTH YEAR
     * @return
     */
    @Override
    public Map<String, Object> queryPowerUseGroupByUnit(String sceneName, String type) {
        //获取计算开始结束日期
        String[] startAndEnd = calcStartAndEndTime(type);
        //查找所有设备
        List<String> buildingList = gutianMapper.selectUnitByScene(sceneName).stream()
                .map(map -> (String) map.get("unitName")).collect(Collectors.toList());
        //计算总电量
        Map<String, BigDecimal> totalPowerUse = handleQueryUnitPeakValley(sceneName, startAndEnd[1], startAndEnd[2], false);
        //计算峰值
        Map<String, BigDecimal> peakPowerUse = handleQueryUnitPeakValley(sceneName, startAndEnd[1], startAndEnd[2], true);
        //构造返回
        List<String> totalPowerList = new ArrayList<>();
        List<String> peakPowerList = new ArrayList<>();
        List<String> valleyPowerList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理
        IntStream.range(0, buildingList.size()).forEach(i -> {
            String key = buildingList.get(i);
            BigDecimal total = Optional.ofNullable(totalPowerUse.get(key))
                    .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal peak = Optional.ofNullable(peakPowerUse.get(key))
                    .orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal valley = total.subtract(peak).setScale(2, BigDecimal.ROUND_HALF_UP);

            abscissaList.add(key);
            totalPowerList.add(total.toString());
            peakPowerList.add(peak.toString());
            valleyPowerList.add(valley.toString());
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(6);
        resultMap.put("totalPower", totalPowerList);
        resultMap.put("peakPower", peakPowerList);
        resultMap.put("valleyPower", valleyPowerList);
        resultMap.put("abscissa", abscissaList);
        return resultMap;
    }

    /**
     * 查询电功率 按设备分组
     *
     * @param buildingId 楼宇群
     * @return
     */
    @Override
    public Map<String, Object> queryWattGroupByUnit(String buildingId) {
        //计算横坐标
        List<String> dateList = calcDateByType(TYPE_HOUR);
        //查询
        Map<String, Map<String, Double>> watts = gutianMapper.selectWattGroupByUnit(buildingId,
                LocalDate.now().format(FORMAT_DATE)).stream().collect(Collectors.toMap(
                map -> (String) map.get("unitName"), map -> {
                    HashMap resultMap = new HashMap();
                    resultMap.put((String) map.get("minTime"),
                            (Double) map.get("watt"));
                    return resultMap;
                }, (k1, k2) -> {
                    k1.putAll(k2);
                    return k1;
                }));
        //构造返回参数
        Map<String, List<String>> wattBuildingMap = new HashMap<>(16);
        List<String> abscissa = new ArrayList<>();


        watts.entrySet().stream().forEach(entry -> {
            List<String> list = new ArrayList<>();
            IntStream.range(0, dateList.size()).forEach(i ->
                    list.add(Optional.ofNullable(entry.getValue().get(dateList.get(i))).orElse(0.0000).toString())
            );
            wattBuildingMap.put(entry.getKey(), list);
        });
        IntStream.range(0, dateList.size()).forEach(i -> abscissa.add(dateList.get(i)));

        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("abscissa", abscissa);
        resultMap.put("wattBuildingMap", wattBuildingMap);
        return resultMap;
    }

    /**
     * 查询设备效率
     *
     * @param unitId 设备ID
     * @return
     */
    @Override
    public Map<String, Object> queryEfficByUnit(String unitId) {
        //计算横坐标
        List<String> dateList = calcDateByType(TYPE_DAY);
        //查询电功率
        Map<String, String> effics = gutianMapper.selectEfficByUnitGroupByHour(unitId, LocalDate.now().format(FORMAT_DATE))
                .stream().collect(Collectors.toMap(map -> (String) map.get("groupTime"),
                        map -> Optional.ofNullable(map.get("effic")).orElse("").toString(), (k1, k2) -> k1));
        //构造返回参数
        List<String> efficList = new ArrayList<>();
        List<String> textList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理数据
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            String effic = Optional.ofNullable(effics.get(key)).orElse("0");
            String text = "";
            if (StringUtils.isEmpty(effic) || "0".equals(effic) ) {
                text = "无";
            } else if (Double.valueOf(effic) < 87) {
                text = "一般";
            } else if (Double.valueOf(effic) > 92) {
                text = "优";
            } else {
                text = "良";
            }
            abscissaList.add((Integer.parseInt(key)) + ":00");
            efficList.add(effic);
            textList.add(text);
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("efficList", efficList);
        resultMap.put("textList", textList);
        resultMap.put("abscissaList", abscissaList);
        return resultMap;
    }

    /**
     * 查询古田山庄一年 耗能 业务分布
     *
     * @return
     */
    @Override
    public Map<String, Object> querySzBusiUse() {
        //当前年
        String year = String.valueOf(LocalDate.now().getYear());
        //会议商务
        BigDecimal hyswPower = gutianMapper.selectPowerUseByAmmeterIds("100010", year).setScale(2, BigDecimal.ROUND_HALF_UP);
        //住宿
        BigDecimal zsPower = gutianMapper.selectPowerUseByAmmeterIds("100013,100009,100005,100004,100003,100006,100007,100008", year).setScale(2, BigDecimal.ROUND_HALF_UP);
        //餐饮接待
        BigDecimal cyjdPower = gutianMapper.selectPowerUseByAmmeterIds("100001,100002", year).subtract(zsPower).setScale(2, BigDecimal.ROUND_HALF_UP);
        //返回参数
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("会议商务", hyswPower);
        resultMap.put("住宿", zsPower);
        resultMap.put("餐饮接待", cyjdPower.compareTo(BigDecimal.ZERO) < 0 ? 0 : cyjdPower);
        return resultMap;
    }

    /**
     * 查询干部学院一年 耗能 业务分布
     *
     * @return
     */
    @Override
    public Map<String, Object> queryGbxyBusiUse() {

        //TODO 古田干部学院电表ID还没拿到
        //返回参数
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("会议培训", 0);
        resultMap.put("餐饮接待", 0);
        resultMap.put("住宿", 0);
        return resultMap;
    }


    /**
     * 根据 类型查询横坐标的日期
     *
     * @param type 查询类型 DAY,WEEK,MONTH,YEAR
     * @return
     */
    public static List<String> calcDateByType(String type) {
        if (TYPE_DAY.equals(type)) {
            //今日
            return IntStream.range(0, LocalDateTime.now().getHour() + 1).mapToObj(GutianServiceImpl::changeNumToHour).collect(Collectors.toList());
        } else if (TYPE_WEEK.equals(type)) {
            //本周
            return IntStream.range(0, 7).mapToObj(i -> LocalDate.now().with(DayOfWeek.MONDAY).plusDays(i)
                    .format(FORMAT_DATE)).collect(Collectors.toList());
        } else if (TYPE_MONTH.equals(type)) {
            //本月
            return IntStream.range(0, LocalDate.now().lengthOfMonth())
                    .mapToObj(i -> LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusDays(i)
                            .format(FORMAT_DATE)).collect(Collectors.toList());
        } else if (TYPE_YEAR.equals(type)) {
            //本年
            return IntStream.range(0, LocalDate.now().getMonthValue())
                    .mapToObj(i -> LocalDate.now().withDayOfYear(1).plusMonths(i)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM"))).collect(Collectors.toList());
        } else if (TYPE_HOUR.equals(type)) {
            return IntStream.range(0, LocalDateTime.now().getHour()*4 + 1 + LocalDateTime.now().getMinute() / 15)
                    .mapToObj(GutianServiceImpl::changeNumToQua).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * 查找需要查询的起始日期
     *
     * @param type 查询类型 DAY,WEEK,MONTH,YEAR
     * @return
     */
    public static String[] calcStartAndEndTime(String type) {
        if (TYPE_DAY.equals(type)) {
            //今日
            LocalDate now = LocalDate.now();
            return new String[]{GROUP_TYPE_HOUR, now.format(FORMAT_DATE),
                    now.plusDays(1).format(FORMAT_DATE)};
        } else if (TYPE_WEEK.equals(type)) {
            //本周
            LocalDate firstOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
            return new String[]{GROUP_TYPE_DAY, firstOfWeek.format(FORMAT_DATE),
                    firstOfWeek.plusDays(7).format(FORMAT_DATE)};
        } else if (TYPE_MONTH.equals(type)) {
            //本月
            return new String[]{GROUP_TYPE_DAY, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).format(FORMAT_DATE),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).format(FORMAT_DATE)};
        } else if (TYPE_YEAR.equals(type)) {
            //本年
            return new String[]{GROUP_TYPE_MONTH, LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).format(FORMAT_DATE),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextYear()).format(FORMAT_DATE)};
        }

        return new String[]{null, null, null};
    }

    /**
     * 过滤一下横坐标 本日的横坐标为 0点-1点，1点-2点...  本周的横坐标为10-09周一,10-10周二..
     *
     * @param abscissa 横坐标
     * @param type     查询类型
     * @param i        循环次数
     * @return
     */
    public static String filterAbscissa(String abscissa, String type, Integer i) {
        if (TYPE_DAY.equals(type)) {
            //今日 横坐标改为 0点-1点
            int abscissaInt = Integer.parseInt(abscissa);
            return abscissaInt + "点-" + (abscissaInt + 1) + "点";
        } else if (TYPE_WEEK.equals(type)) {
            //本周 横坐标改为 10-09周六
            String subAbscissa = abscissa.substring(5);
            switch (i) {
                case 0:
                    return subAbscissa + "周一";
                case 1:
                    return subAbscissa + "周二";
                case 2:
                    return subAbscissa + "周三";
                case 3:
                    return subAbscissa + "周四";
                case 4:
                    return subAbscissa + "周五";
                case 5:
                    return subAbscissa + "周六";
                case 6:
                    return subAbscissa + "周日";
                default:
                    return subAbscissa;
            }
        } else {
            return abscissa;
        }
    }

    /**
     * 查询峰谷值 并处理返回值
     *
     * @param sceneName 场景名
     * @param groupType 分组类型 day 按天分组 month 按月分组
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isPeak    是否为计算峰值
     * @return
     */
    private Map<String, BigDecimal> handleQueryPeakValleyResult(String sceneName, String groupType,
                                                                String startDate, String endDate,
                                                                Boolean isPeak) {
        Map<String, Object> params = new HashMap<>(7);
        params.put("sceneName", sceneName);
        params.put("groupType", groupType);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isPeak", isPeak);
        return gutianMapper.selectPowerUseGroupByType(params).stream()
                .collect(Collectors.toMap(map -> (String) map.get("groupDate"),
                        map -> BigDecimal.valueOf((Double) map.get("powerValue"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP),
                        (k1, k2) -> k1));
    }

    private Map<String, BigDecimal> handleQueryBuildingPeakValley(String sceneName, String startDate,
                                                                  String endDate, Boolean isPeak) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("sceneName", sceneName);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isPeak", isPeak);
        return gutianMapper.selectPowerUseGroupByBuildings(params).stream()
                .collect(Collectors.toMap(map -> (String) map.get("buildingName"),
                        map -> BigDecimal.valueOf((Double) map.get("powerValue"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP),
                        (k1, k2) -> k1));
    }

    private Map<String, BigDecimal> handleQueryUnitPeakValley(String sceneName, String startDate,
                                                                  String endDate, Boolean isPeak) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("sceneName", sceneName);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isPeak", isPeak);
        return gutianMapper.selectPowerUseGroupByUnit(params).stream()
                .collect(Collectors.toMap(map -> (String) map.get("unitName"),
                        map -> BigDecimal.valueOf((Double) map.get("powerValue"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP),
                        (k1, k2) -> k1));
    }

    /**
     * 不满10补0
     *
     * @param i 参数
     * @return
     */
    private static String changeNumToHour(Integer i) {
        return i < 10 ? ("0" + i) : ("" + i);
    }

    /**
     * 未来6小时用能预测数据
     * A,过去每年同月同日同时段的用能数统计，即2019年5月16日13:00-19:00的总用能、2018年5月16日13:00-19:00的总用能、2017年5月16日13:00-19:00的总用能……
     * B.本年度，截至在本月之前每月份的16日13:00-19:00的用能统计，例如，本年度1月16日13:00-19:00的总用能、本年度2月13:00-19:00的总用能、本年度3月13:00-19:00的总用能、本年度4月13:00-19:00的总用能样本
     * C、本月度截至5月16日前，每天13:00-19:00的总用能统计。即5月1日起至5月15日的每天13:00-19:00的总用能样本
     * @param sceneName
     */
    @Override
    public Map<String, Object> getYnjcEchartData(String sceneName) {
        String type = "";
        Map<String, Object> params = null;
        switch (sceneName){
            case "古田会议纪念馆":
                type = "1";
                params = calculate6hours(type);
                break;
            case "古田山庄":
                type = "2";
                params = calculate6hours(type);
                break;
            case "古田干部学院":
                type = "3";
                params = calculateZDSJHN(type);
                break;
            case "1":
                type = "1";
                params = calculateNY6hours(type);
                break;
            case "2":
                type = "2";
                params = calculateNY6hours(type);
                break;
            default:
                params = new HashMap<>(6);
                break;
        }
        return params;
    }
    //农业场景下详情里的未来6小时预测
    private Map<String, Object> calculateNY6hours(String type) {
        //获取该场景下得电表id
        List<Map<String, Object>> ameters = gutianMapper.selectNyAmeterIdBysence(type);
        Boolean falg = false;
        Object[] idarr = new Object[ameters.size()];
        for (int i = 0 ; i<ameters.size();i++) {
            idarr[i] = ameters.get(i).get("ammeter_id");
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        Map<String, Object> params = new HashMap<>(6);
        //获取当前时间
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Integer year = time.getYear();//当前年份
        Integer month = time.getMonthValue();//当前月份
        Integer day = time.getDayOfMonth();//本月天数
        Double[] result = {0.0,0.0,0.0,0.0,0.0,0.0};//返回结果
        //年统计总电能
        List<Map<String, Object>> yeardata = gutianMapper.selectNyYearNum(year.toString());
        if (yeardata!=null){
            for (int i = 0 ; i<yeardata.size();i++){
                Map<String, Object> data = yeardata.get(i);
                LocalDateTime time1 = time.withYear(Integer.valueOf(data.get("years").toString()));
                time1 = time1.withMinute(0);
                calculateNyData(time1,result,idarr);
            }
        }
        List<Map<String, Object>> monthdata = gutianMapper.selectNyMonthNum(year.toString()+"-01",year.toString()+"-"+String.format("%02d",month));
        //月统计总电能
        for (Map<String, Object> data2 :monthdata){
            LocalDateTime time1 = time.withMonth(Integer.valueOf(data2.get("months").toString()));
            time1 = time1.withMinute(0);
            calculateNyData(time1,result, idarr);
        }
        List<Map<String, Object>> daydata = gutianMapper.selectNyDayNum(year.toString()+"-"+String.format("%02d",month)+"-01",year.toString()+"-"+String.format("%02d",month)+"-"+String.format("%02d",day));
        if (daydata!=null){
            Integer a = Integer.valueOf(daydata.get(0).get("days").toString());
            if(time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).plusHours(7).getDayOfMonth()!=a){
                Map<String, Object> map = new HashMap<>();
                if(time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getMonthValue()!=month){
                    map.put("days",time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getDayOfMonth()+".3");
                }else {
                    map.put("days",time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getDayOfMonth()+"");
                }

                daydata.add(map);
                falg = true;
            };
        }
        //日统计总电能
        for (Map<String, Object> data3 :daydata) {
            String[] a = data3.get("days").toString().split("\\.");
            LocalDateTime time1 = null;
            if(a.length==2){
                time1 = time.minusMonths(1);
            }else{
                time1 = time;
            }
            time1 = time1.withDayOfMonth(Integer.valueOf(a[0]));
            time1 = time1.withMinute(0);
            calculateData(time1,result, idarr);
        }
        int sum = yeardata.size()+monthdata.size()+daydata.size();
        //取平均值
        for (int i = 0 ; i<result.length;i++) {
            System.out.println("初始数据:"+result[i]+","+sum);
            result[i] = Math.abs(Double.valueOf(nf.format(result[i] / sum)));
            System.out.println(result[i]);
        }
        params.put("charData", result);
        LocalDateTime time2 = LocalDateTime.now();
        System.out.println(time2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return params;
    }

    /**
     * 计算能值
     * @param time 当前时间
     * @param result 最终结果数组
     * @param idarr 电表id集合
     */
    private void calculateNyData(LocalDateTime time, Double[] result, Object[] idarr){
        Map<String, Object> params = new HashMap<>(6);

        for (int i = 1 ; i<=7;i++){
            params.put("time"+i,time.plusHours(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println(time.plusHours(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        params.put("idarr",idarr);
        List<Map<String, Object>> dndata = gutianMapper.selectNyDnData(params);
        Double value1 = Double.valueOf(dndata.get(0).get("value2").toString())-Double.valueOf(dndata.get(0).get("value1").toString());
        Double value2 = Double.valueOf(dndata.get(0).get("value3").toString())-Double.valueOf(dndata.get(0).get("value2").toString());
        Double value3 = Double.valueOf(dndata.get(0).get("value4").toString())-Double.valueOf(dndata.get(0).get("value3").toString());
        Double value4 = Double.valueOf(dndata.get(0).get("value5").toString())-Double.valueOf(dndata.get(0).get("value4").toString());
        Double value5 = Double.valueOf(dndata.get(0).get("value6").toString())-Double.valueOf(dndata.get(0).get("value5").toString());
        Double value6 =  Double.valueOf(dndata.get(0).get("value7").toString())-Double.valueOf(dndata.get(0).get("value6").toString());
        result[0] = result[0]+value1;
        result[1] = result[1]+value2;
        result[2] = result[2]+value3;
        result[3] = result[3]+value4;
        result[4] = result[4]+value5;
        result[5] = result[5]+value6;
    }

    private Map<String, Object> calculate6hours(String type){
        //获取该场景下得电表id
        List<Map<String, Object>> ameters = gutianMapper.selectAmeterIdBysence(type);
        Boolean falg = false;
        Object[] idarr = new Object[ameters.size()];
        for (int i = 0 ; i<ameters.size();i++) {
            idarr[i] = ameters.get(i).get("ammeter_id");
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        Map<String, Object> params = new HashMap<>(6);
        //获取当前时间
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Integer year = time.getYear();//当前年份
        Integer month = time.getMonthValue();//当前月份
        Integer day = time.getDayOfMonth();//本月天数
        Double[] result = {0.0,0.0,0.0,0.0,0.0,0.0};//返回结果
        //年统计总电能
        List<Map<String, Object>> yeardata = gutianMapper.selectYearNum(year.toString());
        if (yeardata!=null){
            for (int i = 0 ; i<yeardata.size();i++){
                Map<String, Object> data = yeardata.get(i);
                LocalDateTime time1 = time.withYear(Integer.valueOf(data.get("years").toString()));
                time1 = time1.withMinute(0);
                calculateData(time1,result,idarr);
            }
        }
        List<Map<String, Object>> monthdata = gutianMapper.selectMonthNum(year.toString()+"-01",year.toString()+"-"+String.format("%02d",month));
        //月统计总电能
        for (Map<String, Object> data2 :monthdata){
            LocalDateTime time1 = time.withMonth(Integer.valueOf(data2.get("months").toString()));
            time1 = time1.withMinute(0);
            calculateData(time1,result, idarr);
        }
        List<Map<String, Object>> daydata = gutianMapper.selectDayNum(year.toString()+"-"+String.format("%02d",month)+"-01",year.toString()+"-"+String.format("%02d",month)+"-"+String.format("%02d",day));
        if (daydata!=null){
            Integer a = Integer.valueOf(daydata.get(0).get("days").toString());
            if(time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).plusHours(7).getDayOfMonth()!=a){
                Map<String, Object> map = new HashMap<>();
                if(time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getMonthValue()!=month){
                    map.put("days",time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getDayOfMonth()+".3");
                }else {
                    map.put("days",time.withDayOfMonth(Integer.valueOf(daydata.get(0).get("days").toString())).minusDays(1).getDayOfMonth()+"");
                }

                daydata.add(map);
                falg = true;
            };
        }
        //日统计总电能
        for (Map<String, Object> data3 :daydata) {
            String[] a = data3.get("days").toString().split("\\.");
            LocalDateTime time1 = null;
            if(a.length==2){
                time1 = time.minusMonths(1);
            }else{
                time1 = time;
            }
            time1 = time1.withDayOfMonth(Integer.valueOf(a[0]));
            time1 = time1.withMinute(0);
            calculateData(time1,result, idarr);
        }
        int sum = yeardata.size()+monthdata.size()+daydata.size();
        if (falg){
            sum+=1;
        }
        //取平均值
        for (int i = 0 ; i<result.length;i++) {
            System.out.println("初始数据:"+result[i]+","+sum);
            result[i] = Math.abs(Double.valueOf(nf.format(result[i] / sum)));
            System.out.println(result[i]);
        }
        params.put("charData", result);
        LocalDateTime time2 = LocalDateTime.now();
        System.out.println(time2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return params;
    }
    /**
     * 计算能值
     * @param time 当前时间
     * @param result 最终结果数组
     * @param idarr 电表id集合
     */
    private void calculateData(LocalDateTime time, Double[] result, Object[] idarr){
        Map<String, Object> params = new HashMap<>(6);

        for (int i = 1 ; i<=7;i++){
            params.put("time"+i,time.plusHours(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println(time.plusHours(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        params.put("idarr",idarr);
        List<Map<String, Object>> dndata = gutianMapper.selectDnData(params);
        Double value1 = Double.valueOf(dndata.get(0).get("value2").toString())-Double.valueOf(dndata.get(0).get("value1").toString());
        Double value2 = Double.valueOf(dndata.get(0).get("value3").toString())-Double.valueOf(dndata.get(0).get("value2").toString());
        Double value3 = Double.valueOf(dndata.get(0).get("value4").toString())-Double.valueOf(dndata.get(0).get("value3").toString());
        Double value4 = Double.valueOf(dndata.get(0).get("value5").toString())-Double.valueOf(dndata.get(0).get("value4").toString());
        Double value5 = Double.valueOf(dndata.get(0).get("value6").toString())-Double.valueOf(dndata.get(0).get("value5").toString());
        Double value6 =  Double.valueOf(dndata.get(0).get("value7").toString())-Double.valueOf(dndata.get(0).get("value6").toString());
        result[0] = result[0]+value1;
        result[1] = result[1]+value2;
        result[2] = result[2]+value3;
        result[3] = result[3]+value4;
        result[4] = result[4]+value5;
        result[5] = result[5]+value6;
    }
    /**
     * 计算重大事件耗能
     */
    private Map<String, Object> calculateZDSJHN(String type){
        Map<String, Object> result = new HashMap<>(6);
        List<Object[]> data = new ArrayList<>(6);
        Map<String, Object> param = null;

        NumberFormat nf = NumberFormat.getNumberInstance();
        List<Map<String, Object>> dates = gutianMapper.selectdateFromReception();
        Object[] timearr = new Object[dates.size()];
        Object[] fzarr = new Object[dates.size()];
        Object[] gzarr = new Object[dates.size()];
        int i = 0;
        for (Map<String, Object> map:dates) {
            String time = map.get("date").toString();
            String ktime = time+" "+PEAK_HOUR_END+":00:00";
            String etime = time+" "+PEAK_HOUR_START+":00:00";
            param = new HashMap<>(6);
            param.put("date",time);
            param.put("ktime",ktime);
            param.put("etime",etime);
            param.put("type",type);
            List<Map<String, Object>> datas = gutianMapper.getLast6MotnData(param);
            Object[] dataarr = {time,datas.get(0).get("fz"),datas.get(0).get("gz")};
            timearr[i]=time;
            fzarr[i]=datas.get(0).get("fz");
            gzarr[i]=datas.get(0).get("gz");
            i++;
        }
        data.add(timearr);
        data.add(fzarr);
        data.add(gzarr);
        result.put("charData", data);
        return result;
    }


    public static String changeNumToQua(Integer num) {
        Integer hour = num / 4;
        Integer min = num % 4 * 15;
        return changeNumToHour(hour) + ":" + changeNumToHour(min);
    }
}

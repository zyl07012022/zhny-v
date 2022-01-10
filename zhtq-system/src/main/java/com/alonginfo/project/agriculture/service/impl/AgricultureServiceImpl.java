package com.alonginfo.project.agriculture.service.impl;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.project.agriculture.constant.AgricultureConstant;
import com.alonginfo.project.agriculture.domain.Weather;
import com.alonginfo.project.agriculture.mapper.AgricultureMapper;
import com.alonginfo.project.agriculture.service.IAgricultureService;
import com.alonginfo.project.gutian.service.impl.GutianServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
 * @Description 农业场景 服务层 实现类
 * @Author Yalon
 * @Date 2020-04-14 17:20
 * @Version mxdata_v
 */
@Service
public class AgricultureServiceImpl implements IAgricultureService {

    @Autowired
    private AgricultureMapper agricultureMapper;

    DecimalFormat df = new DecimalFormat("0.00");//格式化小数

    /**
     * 查找鸡舍或大棚的 下拉框选项
     *
     * @param houseType chickenHouse 鸡舍   greenHouse 大棚
     * @return houseKey houseName
     */
    @Override
    public List<Map<String, Object>> queryHouseOptionsByType(String houseType) {
        if (StringUtils.isEmpty(houseType)) {
            return new ArrayList<>();
        }
        //参数处理
        String tableName = "";
        String houseKey = "";
        String houseName = "";
        if (AgricultureConstant.HOUSE_TYPE_CHICKEN.equals(houseType)) {
            tableName = AgricultureConstant.TABLE_NAME_CHICKEN;
            houseKey = AgricultureConstant.HOUSE_KEY_CHICKEN;
            houseName = AgricultureConstant.HUSE_NAME_CHICKEN;
        } else if (AgricultureConstant.HOUSE_TYPE_GREEN.equals(houseType)) {
            tableName = AgricultureConstant.TABLE_NAME_GREEN;
            houseKey = AgricultureConstant.HOUSE_KEY_GREEN;
            houseName = AgricultureConstant.HUSE_NAME_GREEN;
        }
        return agricultureMapper.selectHouseOptionsByTableInfo(tableName, houseKey, houseName);
    }

    /**
     * 获取最新气象数据
     *
     * @return
     */
    @Override
    public Weather selectNew() {
        return agricultureMapper.selectWeather();
    }

    /**
     * 查询用能监测值
     *
     * @param sceneName chicken鸡舍  plant大棚
     * @param type
     * @return
     */
    @Override
    public Map<String, List<String>> queryWatchValue(String sceneName, String type, String houseKey) {
        //返回结果值
        Map<String, List<String>> resMap = new HashMap<String, List<String>>();
        //计算横坐标的值
        List<String> dateList = calcDateByType(type);

        // 查询数据库返回结果值
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        if ("chicken".equals(sceneName)) {// 鸡舍
            dataList = agricultureMapper.selectChickhouseWatch(type, houseKey);
        } else { //石斛大棚
            dataList = agricultureMapper.selectGreenhouseWatch(type, houseKey);
        }

        List<String> ynArr = handleArr(dateList, dataList, "carbon");

        List<String> xAxis = dateList.stream().map(item -> {
            if (AgricultureConstant.TYPE_DAY.equals(type)) {
                return item = item + "时";
            } else if (AgricultureConstant.TYPE_WEEK.equals(type) || AgricultureConstant.TYPE_MONTH.equals(type)) {
                return item = item + "日";
            } else if (AgricultureConstant.TYPE_YEAR.equals(type)) {
                return item = item + "月";
            }
            return item;
        }).collect(Collectors.toList());
        resMap.put("seriesData", ynArr);
        resMap.put("xAxis", xAxis);
        return resMap;
    }

    /**
     * 鸡舍环境预警处理
     *
     * @param sceneName
     * @return
     */
    @Override
    public Map<String, Object> chickenWarn(String sceneName) {
        String baseType = "chicken".equals(sceneName) ? "1" : "2";

        // 返回结果值
        Map<String, Object> map = new HashMap<String, Object>();

        // 监测点list
        List<Map<String, Object>> watchList = new ArrayList<Map<String, Object>>();

        // 查询数据库co2与温度最新一条值与阈值
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        // 能耗与监视故障
        List<Map<String, Object>> nhList = new ArrayList<Map<String, Object>>();
        watchList = agricultureMapper.selectWatch(baseType);
        dataList = agricultureMapper.selectChickenWarn(baseType);
//            nhList = agricultureMapper.selectNhChicken();

        // 组装co2与温度预警数据
        if ("1".equals(baseType))
            assembleData(watchList, dataList, map);
        else if ("2".equals(baseType))
            assembleData_sh(watchList, dataList, map);
        return map;
    }


    /**
     * 鸡舍用能设备、产出情况、累计用电、不间断用能、累计用电费用
     *
     * @return
     */
    @Override
    public Map<String, Object> outputData() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        // 鸡舍设备数
        Integer unit = agricultureMapper.selectUnitChichenhouse();
        resMap.put("sbs", unit);
        // 年度 鸡与鸡蛋产出
        Map<String, Object> ecMap = agricultureMapper.selectOutPut();
        resMap.put("jcc", ecMap.get("chickenNum"));
        resMap.put("dcc", ecMap.get("eggNum"));
        // 年度累计用电
        Float elecData = agricultureMapper.selectelecData("1");
        //电费
        Float dfData = elecData * 1.38f;
        resMap.put("elecData", elecData);
        resMap.put("dfData", dfData);
        return resMap;
    }

    /**
     * 石斛用能设备、产出情况、累计用电、不间断用能、累计用电费用
     *
     * @return
     */
    @Override
    public Map<String, Object> outputDataPlant() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        // 鸡舍设备数
        Integer unit = agricultureMapper.selectUnitPlant();
        resMap.put("sbs", unit);
        // 年度石斛
        Float num = agricultureMapper.selectPlantOutPut();
        resMap.put("num", num);
        // 年度累计用电
        Float elecData = agricultureMapper.selectelecData("2");
        //电费
        Float dfData = elecData * 1.38f;
        resMap.put("elecData", elecData);
        resMap.put("dfData", dfData);
        return resMap;
    }

    /**
     * 查询能耗成本分析
     *
     * @param sceneName chicken鸡舍  plant大棚
     * @param type      1按季度 2 按淡旺季
     * @return
     */
    @Override
    public List<Map<String, Object>> getPowerDistribute(String sceneName, String type) {
        // 返回值
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        String baseType = "chicken".equals(sceneName) ? "1" : "2";
        if ("2".equals(type)) {// 按淡旺季
            List<Map<String, Object>> ss = agricultureMapper.getPowerDistribute(baseType, type);
            resList = Optional.ofNullable(agricultureMapper.getPowerDistribute(baseType, type)).orElse(new ArrayList<>()).stream().filter(smap -> StringUtils.isNotNull(smap.get("dstype"))).map(a -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", "1".equals(a.get("dstype").toString()) ? "旺季" : "淡季");
                map.put("value", a.get("powerValue"));
                return map;
            }).collect(Collectors.toList());
        } else if ("1".equals(type)) {
            resList = Optional.ofNullable(agricultureMapper.getPowerDistribute(baseType, type)).orElse(new ArrayList<>()).stream().map(a -> {
                Map<String, Object> map = new HashMap<String, Object>();
                if ("1".equals(a.get("datet").toString())) {
                    map.put("name", "第一季度");
                } else if ("2".equals(a.get("datet").toString())) {
                    map.put("name", "第二季度");
                } else if ("3".equals(a.get("datet").toString())) {
                    map.put("name", "第三季度");
                } else if ("4".equals(a.get("datet").toString())) {
                    map.put("name", "第四季度");
                }
                map.put("value", a.get("powerValue"));
                return map;
            }).collect(Collectors.toList());
        }
        return resList;
    }

    /**
     * 能耗指标
     *
     * @param sceneName
     * @return
     */
    @Override
    public Map<String, List<String>> SelectEleIndex(String sceneName) {
        String baseType = "chicken".equals(sceneName) ? "1" : "2";

        // 返回结果值
        Map<String, List<String>> resMap = new HashMap<String, List<String>>();
        // 计算横坐标的值
        List<String> dateList = calcDateByType("YEAR");

        // 查询每月耗电
        List<Map<String, Object>> eleList = agricultureMapper.selectEleMonth(baseType);

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        // 查询养鸡场或石斛种植场每月产出
        if ("1".equals(baseType)) {// 养鸡场
            dataList = agricultureMapper.selectOutputData(baseType);
            combinData_yj(dateList, eleList, dataList, resMap);
        } else if ("2".equals(baseType)) {// 石斛种植大棚
            dataList = agricultureMapper.selectOutputData(baseType);
            combinData_sh(dateList, eleList, dataList, resMap);
        }
        return resMap;
    }

    /**
     * 近一周水分饱和度 与 土壤温度预警
     *
     * @return
     */
    @Override
    public Map<String, List<String>> trChartWaring(String houseKey) {
        //返回结果值
        Map<String, List<String>> resMap = new HashMap<String, List<String>>();
        //计算横坐标的值
        List<String> dateList = calcDateByType("WEEK");
        // 获取数据
        List<Map<String, Object>> dataList = agricultureMapper.trChartWaring(houseKey);
        // 获取值
        List<String> humidity = handleArr(dateList, dataList, "humidity");
        List<String> temperature = handleArr(dateList, dataList, "temperature");
        List<String> xAxis = dateList.stream().map(item -> {
            return item = item + "日";
        }).collect(Collectors.toList());

        resMap.put("humidity", humidity);
        resMap.put("temperature", temperature);
        resMap.put("xAxis", xAxis);
        return resMap;
    }

    /**
     * 查询峰谷值
     *
     * @param baseType
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> queryPeakValleyByNameAndType(String baseType, String type) {
        //计算横坐标的值
        List<String> dateList = GutianServiceImpl.calcDateByType(type);
        //查询开始结束日期
        String[] startAndEndDate = GutianServiceImpl.calcStartAndEndTime(type);
        //查询全部用电量
        Map<String, BigDecimal> totalPowerUse = handleQueryPeakValleyResult(baseType, startAndEndDate[0],
                startAndEndDate[1], startAndEndDate[2], false);
        //查询峰值用电量
        Map<String, BigDecimal> peakPowerUse = handleQueryPeakValleyResult(baseType, startAndEndDate[0],
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
            abscissaList.add(GutianServiceImpl.filterAbscissa(key, type, i));
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
     * 查询
     *
     * @param baseType
     * @return
     */
    @Override
    public Map<String, Object> queryWattByName(String baseType) {
        //计算横坐标
        List<String> dateList = GutianServiceImpl.calcDateByType(TYPE_DAY);
        //查询电功率
        Map<String, Double> watts = agricultureMapper.selectWattByNameGroupByHour(baseType, LocalDate.now().format(FORMAT_DATE))
                .stream().collect(Collectors.toMap(map -> (String) map.get("groupTime"),
                        map -> (Double) map.get("watt"), (k1, k2) -> k1));
        //构造返回参数
        List<String> wattList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理数据
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            abscissaList.add((Integer.parseInt(key) + 1) + ":00");
            wattList.add(Optional.ofNullable(watts.get(key)).orElse(0.0).toString());
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("wattList", wattList);
        resultMap.put("abscissaList", abscissaList);
        return resultMap;
    }

    /**
     * 查询设备能耗信息
     *
     * @param baseType 1-鸡舍 2-大棚
     * @return
     */
    @Override
    public List<Map<String, Object>> queryUnitPowerUse(String baseType) {
        return agricultureMapper.selectUnitPowerUse(baseType);
    }

    /**
     * 查询风机状态
     *
     * @param houseId
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> queryWindMachine(String houseId, String type) {
        return agricultureMapper.selectWindMachine(houseId, type);
    }

    /**
     * 查询产耗指数
     *
     * @param baseType
     * @return
     */
    @Override
    public List<Map<String, Object>> queryOpByBaseType(String baseType) {
        return agricultureMapper.selectOPByBaseType(baseType);
    }

    /**
     * 查询能耗指数
     *
     * @param baseType
     * @return
     */
    @Override
    public Map<String, Object> queryPrByBaseType(String baseType) {
        //计算横坐标的值
        List<String> dateList = GutianServiceImpl.calcDateByType(TYPE_DAY);
        //查询能耗指数
        Map<String, String> prs = agricultureMapper.selectPRByBaseType(baseType)
                .stream().collect(Collectors.toMap(map -> (String) map.get("groupTime"),
                        map -> (String) map.get("pr"), (k1, k2) -> k1));
        //构造返回参数
        List<String> prList = new ArrayList<>();
        List<String> abscissaList = new ArrayList<>();
        //循环处理数据
        IntStream.range(0, dateList.size()).forEach(i -> {
            String key = dateList.get(i);
            abscissaList.add((Integer.parseInt(key) + 1) + ":00");
            prList.add(Optional.ofNullable(prs.get(key)).orElse(""));
        });
        //返回
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("prList", prList);
        resultMap.put("abscissaList", abscissaList);
        return resultMap;
    }

    /**
     * 组装数据
     *
     * @param dateList
     * @param eleList
     * @param dataList
     * @param resMap
     */
    private void combinData_sh(List<String> dateList, List<Map<String, Object>> eleList, List<Map<String, Object>> dataList, Map<String, List<String>> resMap) {
        // 每月用电转为Map
        Map<String, String> eleMap = Optional.ofNullable(eleList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String) map.get("datime"),
                map -> String.format("%.2f", map.get("pvalue")), (k1, k2) -> k1));

        // 每月产出转为Map
        Map<String, String> outMap = Optional.ofNullable(dataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String) map.get("datetime"),
                map -> new BigDecimal(String.valueOf(map.get("num")))
                        .divide(new BigDecimal(eleMap.get(map.get("datetime"))), 2, BigDecimal.ROUND_HALF_UP).toString(), (k1, k2) -> k1));
        List<String> yAxis = new ArrayList<String>();
        for (int i = 0; i < dateList.size(); i++) {
            yAxis.add(Optional.ofNullable(outMap.get(dateList.get(i))).orElse("0"));
        }
        resMap.put("yAxis", yAxis);
    }

    /**
     * 组装数据
     *
     * @param dateList xAxis
     * @param eleList  每月用电
     * @param dataList 每月产出
     * @param resMap   返回值
     */
    private void combinData_yj(List<String> dateList, List<Map<String, Object>> eleList, List<Map<String, Object>> dataList, Map<String, List<String>> resMap) {
        // 每月用电转为Map
        Map<String, String> eleMap = Optional.ofNullable(eleList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String) map.get("datime"),
                map -> String.format("%.2f", map.get("pvalue")), (k1, k2) -> k1));

        // 转换不同的结果 每羽鸡 与 ，每枚蛋
        Map<String, String> outMap_chk = Optional.ofNullable(dataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String) map.get("datetime"),
                map -> {

                    if (map.get("chickenNum") == null || new BigDecimal(String.valueOf(map.get("chickenNum"))).compareTo(BigDecimal.ZERO) <= 0)
                        return "--";
                    else
                        return new BigDecimal(eleMap.get(map.get("datetime")))
                                .divide(new BigDecimal(String.valueOf(map.get("chickenNum"))), 2, BigDecimal.ROUND_HALF_UP).toString();
                }, (k1, k2) -> k1));

        Map<String, String> outMap_egg = Optional.ofNullable(dataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String) map.get("datetime"),
                map -> {
                    if (map.get("eggNum") == null || new BigDecimal(String.valueOf(map.get("eggNum"))).compareTo(BigDecimal.ZERO) <= 0)
                        return "--";
                    else
                        return new BigDecimal(eleMap.get(map.get("datetime")))
                                .divide(new BigDecimal(String.valueOf(map.get("eggNum"))), 2, BigDecimal.ROUND_HALF_UP).toString();
                }, (k1, k2) -> k1));
        /*Map<String,String> outMap_chk = Optional.ofNullable(dataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String)map.get("datetime"),
                map ->  new BigDecimal(String.valueOf(map.get("chickenNum"))).divide(AgricultureConstant.converIndex,2,BigDecimal.ROUND_HALF_UP)
                        .add(new BigDecimal(String.valueOf(map.get("eggNum"))))
                        .divide(new BigDecimal(eleMap.get(map.get("datetime"))),2,BigDecimal.ROUND_HALF_UP).toString(),(k1, k2) -> k1));

        Map<String,String> outMap_egg = Optional.ofNullable(dataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(map -> (String)map.get("datetime"),
                map ->  new BigDecimal(String.valueOf(map.get("eggNum"))).multiply(AgricultureConstant.converIndex)
                        .add(new BigDecimal(String.valueOf(map.get("chickenNum"))))
                        .divide(new BigDecimal(eleMap.get(map.get("datetime"))),2,BigDecimal.ROUND_HALF_UP).toString(),(k1, k2) -> k1));*/

        // 处理值
        List<String> yAxis_chk = new ArrayList<String>();
        List<String> yAxis_egg = new ArrayList<String>();
        for (int i = 0; i < dateList.size(); i++) {
            yAxis_chk.add(Optional.ofNullable(outMap_chk.get(dateList.get(i))).orElse("0"));
            yAxis_egg.add(Optional.ofNullable(outMap_egg.get(dateList.get(i))).orElse("0"));
        }
        resMap.put("yAxis_chk", yAxis_chk);
        resMap.put("yAxis_egg", yAxis_egg);
    }

    /**
     * 历史耗能查询
     *
     * @return
     */
    @Override
    public Map<String, Object> SelectEleHistoryByScene(String baseType) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        String zeroClock = "00:00";
        //当前时分
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        //当前日期
        LocalDate today = LocalDate.now();
        //本月第一天
        LocalDate month = today.with(TemporalAdjusters.firstDayOfMonth());
        // 今日用电量
        resMap.put("todayPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                today.format(AgricultureConstant.df) + " " + zeroClock, today.format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        // 上月同期用电量
        resMap.put("lastMohtnDayPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                today.plusMonths(-1).format(AgricultureConstant.df) + " " + zeroClock, today.plusMonths(-1).format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        // 昨日用电量
        resMap.put("yesterDayPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                today.plusDays(-1).format(AgricultureConstant.df) + " " + zeroClock, today.plusDays(-1).format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        // 本月用电量
        resMap.put("currentMonthPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                month.format(AgricultureConstant.df) + " " + zeroClock, today.format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        // 去年同期用电量
        resMap.put("lastYearPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                month.plusYears(-1).format(AgricultureConstant.df) + " " + zeroClock, today.plusYears(-1).format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        // 上月用电量
        resMap.put("lastMonthPower", Optional.ofNullable(agricultureMapper.selectHistoryEle(
                month.plusMonths(-1).format(AgricultureConstant.df) + " " + zeroClock, today.plusMonths(-1).format(AgricultureConstant.df) + " " + time, baseType)).orElse(0f));
        return resMap;
    }

    /**
     * 组装大棚数据
     *
     * @param watchList 监视点list
     * @param dataList  最新一条件监视数据list
     * @param map       返回结果值
     */
    private void assembleData_sh(List<Map<String, Object>> watchList, List<Map<String, Object>> dataList, Map<String, Object> map) {
        // 水分饱和度预警
        List<Map<String, Object>> humidityList = dataList.stream().filter(a ->
                Float.parseFloat(String.valueOf(a.get("humidity"))) > Float.parseFloat(String.valueOf(a.get("humidityMax")))).collect(Collectors.toList());
        // co2告警
        List<Map<String, Object>> coTwoList = dataList.stream().filter(a ->
                Float.parseFloat(String.valueOf(a.get("carbon"))) > Float.parseFloat(String.valueOf(a.get("carbonMax")))).collect(Collectors.toList());
        // 土壤温度告警
        List<Map<String, Object>> temperatureList = dataList.stream().filter(a ->
                Float.parseFloat(String.valueOf(a.get("temperature"))) > Float.parseFloat(String.valueOf(a.get("temperatureMax")))).collect(Collectors.toList());
        map.put("humidity", humidityList);
        map.put("humidity_percent", watchList.size() == 0 ? 0 : df.format(Float.valueOf(humidityList.size()) / watchList.size() * 100));
        map.put("coTwo", coTwoList);
        map.put("coTwo_percent", watchList.size() == 0 ? 0 : df.format(Float.valueOf(coTwoList.size()) / watchList.size() * 100));
        map.put("temperature", temperatureList);
        map.put("temperature_percent", watchList.size() == 0 ? 0 : df.format(Float.valueOf(temperatureList.size()) / watchList.size() * 100));
    }

    /**
     * 组装co2与温度预警数据
     *
     * @param watchList
     * @param dataList
     */
    private void assembleData(List<Map<String, Object>> watchList, List<Map<String, Object>> dataList, Map<String, Object> dataMap) {
        Map<String, String> watchMap = watchList.stream().collect(Collectors.toMap(map -> String.valueOf(map.get("watchId")),
                map -> (String) map.get("watchName"), (k1, k2) -> k1));
        // co2告警list
        List<Map<String, Object>> coWarnList = dataList.stream().filter(map ->
                Float.parseFloat(String.valueOf(map.get("carbon"))) > Float.parseFloat(String.valueOf(map.get("carbonMax")))).collect(Collectors.toList());
        //温度告警list
        List<Map<String, Object>> temperatureList = dataList.stream().filter(map ->
                Float.parseFloat(String.valueOf(map.get("temperature"))) > Float.parseFloat(String.valueOf(map.get("temperatureMax")))).collect(Collectors.toList());
        // 置换鸡舍名称
        coWarnList.forEach(e -> {
            e.put("name", watchMap.get(String.valueOf(e.get("chichenhouse_watch_id"))));
        });
        temperatureList.forEach(e -> {
            e.put("name", watchMap.get(String.valueOf(e.get("chichenhouse_watch_id"))));
        });
        dataMap.put("co2", coWarnList);
        dataMap.put("co2_percent", watchList.size() == 0 ? 0 : df.format(Float.valueOf(coWarnList.size()) / watchList.size() * 100));
        dataMap.put("temperature", temperatureList);
        dataMap.put("temperature_percent", watchList.size() == 0 ? 0 : df.format(Float.valueOf(temperatureList.size()) / watchList.size() * 100));
    }

    /**
     * 处理数值空值
     *
     * @param dateList
     * @param dataList
     * @param name
     */
    private List<String> handleArr(List<String> dateList, List<Map<String, Object>> dataList, String name) {
        List<String> ynArr = new ArrayList<String>();
        Map<String, String> dataMap = dataList.stream().collect(Collectors.toMap(map -> (String) map.get("datatime"), map -> {
            if (map.get(name) == null) {
                return "";
            } else {
                return String.format("%.2f", map.get(name));
            }
        }, (k1, k2) -> k1));
        // 处理值
        for (int i = 0; i < dateList.size(); i++) {
            ynArr.add(Optional.ofNullable(dataMap.get(dateList.get(i))).orElse(""));
        }
        return ynArr;
    }


    /**
     * 计算横坐标值
     *
     * @param type
     * @return
     */
    private static List<String> calcDateByType(String type) {
        if (AgricultureConstant.TYPE_DAY.equals(type)) {
            //今日
            return IntStream.range(0, LocalDateTime.now().getHour() + 1).mapToObj(AgricultureServiceImpl::changeNumToHour).collect(Collectors.toList());
        } else if (AgricultureConstant.TYPE_WEEK.equals(type)) {
            //本周
            return IntStream.range(0, 7).mapToObj(i -> LocalDate.now().with(DayOfWeek.MONDAY).plusDays(i)
                    .format(DateTimeFormatter.ofPattern(AgricultureConstant.FORMAT_DATE_DAY))).collect(Collectors.toList());
        } else if (AgricultureConstant.TYPE_MONTH.equals(type)) {
            //本月
            return IntStream.range(0, LocalDate.now().lengthOfMonth())
                    .mapToObj(i -> LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).plusDays(i)
                            .format(DateTimeFormatter.ofPattern(AgricultureConstant.FORMAT_DATE_DAY))).collect(Collectors.toList());
        } else if (AgricultureConstant.TYPE_YEAR.equals(type)) {
            //本年
            return IntStream.range(0, LocalDate.now().getMonthValue())
                    .mapToObj(i -> LocalDate.now().withDayOfYear(1).plusMonths(i)
                            .format(DateTimeFormatter.ofPattern("MM"))).collect(Collectors.toList());
        }
        return new ArrayList<>();
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

    private Map<String, BigDecimal> handleQueryPeakValleyResult(String baseType, String groupType,
                                                                String startDate, String endDate,
                                                                Boolean isPeak) {
        Map<String, Object> params = new HashMap<>();
        params.put("baseType", baseType);
        params.put("groupType", groupType);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isPeak", isPeak);
        return agricultureMapper.selectPowerUseGroupByType(params).stream()
                .collect(Collectors.toMap(map -> (String) map.get("groupDate"),
                        map -> BigDecimal.valueOf((Double) map.get("powerValue"))
                                .setScale(2, BigDecimal.ROUND_HALF_UP),
                        (k1, k2) -> k1));
    }
}

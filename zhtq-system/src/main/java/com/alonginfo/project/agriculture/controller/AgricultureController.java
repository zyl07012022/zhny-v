package com.alonginfo.project.agriculture.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.agriculture.domain.Weather;
import com.alonginfo.project.agriculture.service.IAgricultureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 智慧农业用能场景
 * @author 师登举
 * @date 2020年4月17日
 */
@Api(tags = "古田农业场景控制层")
@RestController
@RequestMapping("/smartAgriculture")
public class AgricultureController extends BaseController {

    @Autowired
    private IAgricultureService service;

    /**
     * 查询微气象值
     * @return
     */
    @ApiOperation("获取微气象 天气")
    @PostMapping("/wqx")
    public AjaxResult queryWqx() {
        Weather wt = service.selectNew();
        return AjaxResult.success(wt);
    }

    /**
     * 根据类型获取鸡舍或者大棚下拉状态
     */
    @GetMapping(value = "/getSelectType/{type}")
    public AjaxResult getConfigKey(@PathVariable String type) {
        return AjaxResult.success(service.queryHouseOptionsByType(type));
    }

    /**
     * 用能监测值
     * @param sceneName
     * @param type
     * @return
     */
    @ApiOperation("获取用能监测值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "贵妃鸡养殖场,石斛大棚种植大棚",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getWatchValue")
    public AjaxResult getWatchValue (String sceneName, String type, String houseKey) {
        return AjaxResult.success(service.queryWatchValue(sceneName, type, houseKey));
    }

    /**
     * 鸡舍告警值查询
     * @return
     */
    @ApiOperation("获取鸡舍告警值")
    @PostMapping("/chickenWarn")
    public AjaxResult chickenWarn(String sceneName) {
        return AjaxResult.success(service.chickenWarn(sceneName));
    }

    /**
     * 获取能耗成本分析
     * @param sceneName
     * @param type
     * @return
     */
    @ApiOperation("能耗成本分析")
    @PostMapping("/getPowerDistribute")
    public AjaxResult getPowerDistribute(String sceneName,String type) {
        return AjaxResult.success(service.getPowerDistribute(sceneName,type));
    }

    /**
     * 鸡舍用能设备、产出情况、累计用电、不间断用能、累计用电费用
     * @return
     */
    @GetMapping("/outputData")
    public AjaxResult outputData() {
        return AjaxResult.success(service.outputData());
    }

    /**
     * 石斛用能设备、产出情况、累计用电、不间断用能、累计用电费用
     * @return
     */
    @GetMapping("/outputData_plant")
    public AjaxResult outputDataPlant() {
        return AjaxResult.success(service.outputDataPlant());
    }

    /**
     * 历史耗能
     * @return
     */
    @PostMapping("/SelectEleHistoryByScene")
    public AjaxResult SelectEleHistoryByScene(String baseType) {
        return AjaxResult.success(service.SelectEleHistoryByScene(baseType));
    }

    /**
     * 能耗指标
     * @return
     */
    @PostMapping("/SelectEleIndex")
    public AjaxResult SelectEleIndex(String sceneName) {
        return AjaxResult.success(service.SelectEleIndex(sceneName));
    }

    /**
     * 近一周水饱和度与土壤温度
     * @return
     */
    @PostMapping("/trChartWaring")
    public AjaxResult trChartWaring(String houseKey) {
        return AjaxResult.success(service.trChartWaring(houseKey));
    }

    @ApiOperation("获取场景的 峰谷曲线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseType", value = "场景名称", allowableValues = "1,2",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getPeakValleyByNameAndType")
    public AjaxResult getPeakValleyByNameAndType (String baseType, String type) {
        return AjaxResult.success(service.queryPeakValleyByNameAndType(baseType, type));
    }

    @ApiOperation("获取场景的 用电功率曲线")
    @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
            paramType = "query", required = true)
    @PostMapping("/getWattByName")
    public AjaxResult getWattByName (String baseType) {
        return AjaxResult.success(service.queryWattByName(baseType));
    }

    @ApiOperation("获取场景的 设备能耗")
    @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
            paramType = "query", required = true)
    @PostMapping("/getUnitPowerUse")
    public AjaxResult getUnitPowerUse (String baseType) {
        return AjaxResult.success(service.queryUnitPowerUse(baseType));
    }

    @ApiOperation("获取风机情况信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", value = "鸡舍Id", paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getWindMachine")
    public AjaxResult getWindMachine (String houseId, String type) {
        return AjaxResult.success(service.queryWindMachine(houseId, type));
    }

    @ApiOperation("获取场景的 产耗指数")
    @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
            paramType = "query", required = true)
    @PostMapping("/getOpByBaseType")
    public AjaxResult getOpByBaseType (String baseType) {
        return AjaxResult.success(service.queryOpByBaseType(baseType));
    }

    @ApiOperation("获取场景的 能耗指数")
    @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
            paramType = "query", required = true)
    @PostMapping("/getPrByBaseType")
    public AjaxResult getPrByBaseType (String baseType) {
        return AjaxResult.success(service.queryPrByBaseType(baseType));
    }
}

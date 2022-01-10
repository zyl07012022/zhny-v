package com.alonginfo.project.agriculture.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.agriculture.service.IAppAgricultureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 智慧农业app请求后台控制层
 */
@Api(tags = "古田APP农业控制层")
@RestController
@RequestMapping("/smartAgricultureApp")
public class AppAgricultureController extends BaseController {

    @Autowired
    private IAppAgricultureService service;

    /**
     * 获取全部监测点及当前数据
     * @return
     */
    @ApiOperation("获取全部监测点及当前数据")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "plant,chicken",
            paramType = "query", required = true)
    @PostMapping("/getWatch")
    public AjaxResult getWatch(String sceneName) {
        return AjaxResult.success(service.selectWatch(sceneName));
    }

    /**
     * 获取全部监测点及当前数据
     * @return
     */
    @ApiOperation("获取全部监测点及当前数据")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "plant,chicken",
            paramType = "query", required = true)
    @PostMapping("/getWaringWatch")
    public AjaxResult getWaringWatch(String sceneName) {
        return AjaxResult.success(service.getWaringWatch(sceneName));
    }

    /**
     * 根据id获取监测点详情
     * @param watchId
     * @return
     */
    @ApiOperation("根据id获取监测点详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "plant,chicken",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "watchId", value = "监视点ID", paramType = "query", required = true)
    })
    @PostMapping("/getWatchForId")
    public AjaxResult getWatchForId(String watchId, String sceneName) {
        return AjaxResult.success(service.getWatchForId(watchId,sceneName));
    }

    @ApiOperation("获取一个月的设备 能耗")
    @ApiImplicitParams({
            @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "rows", value = "条数", paramType = "query")
    })

    @PostMapping("/getUnitPower")
    public AjaxResult getUnitPower (String baseType, Integer rows) {
        return AjaxResult.success(service.getUnitPower(baseType, rows));
    }

    @ApiOperation("获取首页的 鸡舍的二氧化碳的含量或者大棚的土壤水分含量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "rows", value = "条数", paramType = "query")
    })

    @PostMapping("/getHouseSurvey")
    public AjaxResult getHouseSurvey (String baseType, Integer rows) {
        return AjaxResult.success(service.getHouseSurvey(baseType, rows));
    }

    @ApiOperation("增加鸡舍大棚的产出")
    @ApiImplicitParams({
            @ApiImplicitParam(name="baseType", value = "场景类型", allowableValues = "1,2",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "outputList", value = "条数", paramType = "body", dataType = "application/json")
    })

    @PostMapping("/addOutput")
    public AjaxResult addOutput (String baseType, @RequestBody List<Map<String, Object>> outputList) {
        return AjaxResult.success(service.addOutput(baseType, outputList));
    }


}

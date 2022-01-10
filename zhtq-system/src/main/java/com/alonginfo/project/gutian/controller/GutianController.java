package com.alonginfo.project.gutian.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.gutian.service.IGutianService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.alonginfo.project.gutian.constant.SceneConstant.GUTIAN_COLLEGE;
import static com.alonginfo.project.gutian.constant.SceneConstant.GUTIAN_VILLA;

/**
 * @Description 古田三大业务场景 控制层
 * @Author Yalon
 * @Date 2020-04-15 16:21
 * @Version mxdata_v
 */
@Api(tags = "古田三大业务场景控制层")
@RestController
@RequestMapping("/gutian")
public class GutianController extends BaseController {

    @Autowired
    private IGutianService gutianService;

    @ApiOperation("获取场景的 楼宇数量 建筑面积 年度累计用电 接待人数 供能线路 累计用电费用 重大活动天数")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getSceneInfoByName")
    public AjaxResult getSceneInfoByName(String sceneName) {
        if (StringUtils.isEmpty(sceneName)) {
            return AjaxResult.error("场景名称不可为空！");
        }
        return AjaxResult.success(gutianService.querySceneInfoByName(sceneName));
    }

    @ApiOperation("获取场景的 峰谷曲线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getPeakValleyByNameAndType")
    public AjaxResult getPeakValleyByNameAndType (String sceneName, String type) {
        return AjaxResult.success(gutianService.queryPeakValleyByNameAndType(sceneName, type));
    }

    @ApiOperation("获取场景的 用电功率曲线")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getWattByName")
    public AjaxResult getWattByName (String sceneName) {
        return AjaxResult.success(gutianService.queryWattByName(sceneName));
    }

    @ApiOperation("获取场景的 人均用电和每平米用电曲线")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getAvgByName")
    public AjaxResult getAvgByName (String sceneName) {
        return AjaxResult.success(gutianService.queryAvgPowerByName(sceneName));
    }

    @ApiOperation("获取设备 或 古田会议纪念馆的 电压电流功率值 ")
    @ApiImplicitParam(name="unitId", value = "电气设备ID", paramType = "query")
    @PostMapping("/getElectricRealTimeInfoByUnit")
    public AjaxResult getElectricRealTimeInfoByUnit (String unitId) {
        return AjaxResult.success(gutianService.queryElectricRealTime(unitId));
    }

    @ApiOperation("获取场景的 设备类别下拉框")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getUnitTypeOptions")
    public AjaxResult getUnitTypeOptions(String sceneName) {
        return AjaxResult.success(gutianService.queryUnitTypeOptions(sceneName));
    }

    @ApiOperation("获取场景的该类别的 设备下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "unitType", value = "设备类别ID", paramType = "query", required = true)
    })
    @PostMapping("/getUnitOptions")
    public AjaxResult getUnitOptions(String sceneName, String unitType) {
        return AjaxResult.success(gutianService.queryUnitOptions(sceneName, unitType));
    }

    @ApiOperation("获取场景的 楼宇群")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getBuildingGroupsByScene")
    public AjaxResult getBuildingGroupsByScene (String sceneName) {
        return AjaxResult.success(gutianService.queryBuildingGroupsByScene(sceneName));
    }

    @ApiOperation("获取场景的 耗能分布 饼状图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "buildingId", value = "楼宇群ID", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "quarter,holiday,season",
                    paramType = "query", required = true)
    })
    @PostMapping("/getPowerDistribute")
    public AjaxResult getPowerDistribute (String sceneName, String buildingId, String type) {
        return AjaxResult.success(gutianService.queryPowerDistribute(sceneName, buildingId, type));
    }

    @ApiOperation("获取场景的历史能耗")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getElectricHistoryInfoByScene")
    public AjaxResult getElectricHistoryInfoByScene(String sceneName) {
        return AjaxResult.success(gutianService.queryElectricHistoryByScene(sceneName));
    }

    @ApiOperation("获取场景的 按大楼 峰谷曲线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getPowerUseGroupByBuildings")
    public AjaxResult getPowerUseGroupByBuildings(String sceneName, String type) {
        return AjaxResult.success(gutianService.queryPowerUseGroupByBuildings(sceneName, type));
    }

    @ApiOperation("查询电功率 按楼宇分组")
    @ApiImplicitParam(name = "buildingId", value = "楼宇群ID", paramType = "query")
    @PostMapping("/getWattGroupByBuilding")
    public AjaxResult getWattGroupByBuilding (String buildingId) {
        return AjaxResult.success(gutianService.queryWattGroupByBuilding(buildingId));
    }

    @ApiOperation("未来6小时用能预测")
    @PostMapping("/getYnjcEchartData")
    public AjaxResult getYnjcEchartData(String sceneName) {
        return AjaxResult.success(gutianService.getYnjcEchartData(sceneName));
    }

    @ApiOperation("获取场景的 按设备 峰谷曲线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "type", value = "查询类型", allowableValues = "DAY,WEEK,MONTH,YEAR",
                    paramType = "query", required = true)
    })
    @PostMapping("/getPowerUseGroupByUnit")
    public AjaxResult getPowerUseGroupByUnit(String sceneName, String type) {
        return AjaxResult.success(gutianService.queryPowerUseGroupByUnit(sceneName, type));
    }

    @ApiOperation("查询电功率 按设备分组")
    @ApiImplicitParam(name = "buildingId", value = "楼宇群ID", paramType = "query")
    @PostMapping("/getWattGroupByUnit")
    public AjaxResult getWattGroupByUnit (String buildingId) {
        return AjaxResult.success(gutianService.queryWattGroupByUnit(buildingId));
    }

    @ApiOperation("获取设备效率")
    @ApiImplicitParam(name="unitId", value = "设备ID", paramType = "query", required = true)
    @PostMapping("/getEfficByUnit")
    public AjaxResult getEfficByUnit (String unitId) {
        return AjaxResult.success(gutianService.queryEfficByUnit(unitId));
    }

    @ApiOperation("获取场景的 用电业务分布 ")
    @ApiImplicitParam(name="sceneName", value = "场景名称", allowableValues = "古田会议纪念馆,古田山庄,古田干部学院",
            paramType = "query", required = true)
    @PostMapping("/getBusiPowerUse")
    public AjaxResult getBusiPowerUse (String sceneName) {
        if (GUTIAN_VILLA.equals(sceneName)) {
            return AjaxResult.success(gutianService.querySzBusiUse());
        } else if (GUTIAN_COLLEGE.equals(sceneName)) {
            return AjaxResult.success(gutianService.queryGbxyBusiUse());
        }
        return null;
    }



}

package com.alonginfo.project.gutian.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.gutian.service.IGutianAppService;
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
 * @Description 古田三大业务场景 控制层 -app
 * @Author LM
 * @Date 2020-04-15 16:21
 * @Version mxdata_v
 */
@Api(tags = "古田三大业务场景控制层-app")
@RestController
@RequestMapping("/gutianapp")
public class GutianAppController extends BaseController {


    @Autowired
    private IGutianAppService gutianAppService;



    @ApiOperation("当前园区用电功率")
    @ApiImplicitParam(name = "sceneId", value = "场景ID", paramType = "query")
    @PostMapping("/getdqyqgl")
    public AjaxResult getDqyqgl(String sceneId) {
        return AjaxResult.success("成功",gutianAppService.getDqyqgl(sceneId));
    }



    @ApiOperation("当前园区累积能耗")
    @ApiImplicitParam(name = "sceneId", value = "场景ID", paramType = "query")
    @PostMapping("/getdqyqljnh")
    public AjaxResult getDqyqljnh(String sceneId) {
        return AjaxResult.success("成功",gutianAppService.getDqyqljnh(sceneId));
    }


    @ApiOperation("能耗指标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "年月 YYYY-MM",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "sceneId", value = "场景ID",
                    paramType = "query", required = true)
    })
    @PostMapping("/getnhzb")
    public AjaxResult getNhzb(String date,String sceneId) {
        return AjaxResult.success(gutianAppService.getNhzb(date,sceneId));
    }


    @ApiOperation("楼宇群用能监测")
    @ApiImplicitParam(name = "sceneId", value = "场景ID",
            paramType = "query", required = true)
    @PostMapping("/getlyqynjc")
    public AjaxResult getLyqynjc(String sceneId) {
        return AjaxResult.success(gutianAppService.getLyqynjc(sceneId));
    }

    @ApiOperation("楼宇区域监测明细")
    @ApiImplicitParam(name = "buildingId", value = "楼宇ID",
            paramType = "query", required = true)
    @PostMapping("/getlyqyjc")
    public AjaxResult getLyqyjc(String buildingId) {
        return AjaxResult.success(gutianAppService.getLyqyjc(buildingId));
    }



    @ApiOperation("楼层级电气设备用能监测")
    @ApiImplicitParam(name = "sceneId", value = "场景ID",
            paramType = "query", required = true)
    @PostMapping("/getlcjdqsbynjc")
    public AjaxResult getLcjdqsbynjc(String sceneId) {
        return AjaxResult.success(gutianAppService.getLcjdqsbynjc(sceneId));
    }


    @ApiOperation("楼层级电气设备用能监测明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elecUnitId", value = "设备ID",
                    paramType = "query", required = true),
            @ApiImplicitParam(name = "buildingGroup", value = "楼宇ID",
                    paramType = "query", required = true)
    })
    @PostMapping("/getlyqyjcmx")
    public AjaxResult getLyqyjcmx(String elecUnitId,String buildingGroup) {
        return AjaxResult.success(gutianAppService.getLyqyjcmx(elecUnitId,buildingGroup));
    }



    @ApiOperation("用能-设备能耗")
    @ApiImplicitParam(name = "sceneId", value = "场景ID",
            paramType = "query", required = true)
    @PostMapping("/getsbnh")
    public AjaxResult getSbnh(String sceneId) {
        return AjaxResult.success(gutianAppService.getSbnh(sceneId));
    }


    @ApiOperation("保存接待信息")
    @PostMapping("/insertReceptionData")
    public AjaxResult insertReceptionData (@RequestParam("sceneId") String sceneId, @RequestBody List<Map<String,Object>> list) {
        return AjaxResult.success(gutianAppService.insertGTSZData(sceneId,list));
    }





}

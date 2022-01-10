package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RepairInfo;
import com.alonginfo.project.emergencyCommand.service.RepairStationaryPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 获取抢修驻点信息
 * @author 袁坤
 * @date 2020年9月17日
 */
@Api(tags = "获取抢修驻点信息")
@RestController
@RequestMapping("/RepairStationaryPoint")
public class RepairStationaryPointController extends BaseController {

    @Resource
    private RepairStationaryPointService repairStationaryPointService;

    /**
     * 获取获取抢修驻点信息
     * @return
     */
    @ApiOperation("获取抢修驻点信息")
    @PostMapping("/queryPage")
    public AjaxResult queryPage(String pageNum, String pageSize, RepairInfo repairInfo) {
        AjaxResult wt = repairStationaryPointService.queryPage(pageNum,pageSize,repairInfo);
        return wt;
    }

}

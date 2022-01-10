package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.service.RescueTeamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 抢修队伍信息
 *
 * @author PDM
 * @date 2020年9月17日
 */
@Api(tags = "抢修队伍信息")
@RestController
@RequestMapping("/rescueTeams")
public class RescueTeamsController extends BaseController {
    @Autowired
    RescueTeamsService rescueTeamsService;

    /**
     * 获取抢修队伍人员信息
     *
     * @return
     */
    @ApiOperation("获取抢修队伍信息")
    @PostMapping("/queryPage")
    public AjaxResult queryPage(String pageNum, String pageSize, @RequestParam(value = "affiliated_unit") String affiliated_unit, @RequestParam(value = "team_name")String team_name,  @RequestParam(value = "resource_categories")String resource_categories, @RequestParam(value = "resource_type") String resource_type) {
        AjaxResult ajaxResult = rescueTeamsService.queryPage(pageNum, pageSize, affiliated_unit, team_name, resource_categories, resource_type);
        return ajaxResult;
    }

}

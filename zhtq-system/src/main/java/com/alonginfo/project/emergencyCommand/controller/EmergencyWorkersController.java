package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.common.utils.ServletUtils;
import com.alonginfo.framework.security.LoginUser;
import com.alonginfo.framework.security.service.TokenService;
import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.agriculture.domain.Weather;
import com.alonginfo.project.emergencyCommand.service.EmergencyWorkersService;
import com.alonginfo.project.system.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 抢修队伍人员信息
 * @author 师登举
 * @date 2020年4月17日
 */
@Api(tags = "抢修队伍人员信息")
@RestController
@CrossOrigin
@RequestMapping("/emergencyWorkers")
public class EmergencyWorkersController extends BaseController {

    @Autowired
    private EmergencyWorkersService emergencyWorkersService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取抢修队伍人员信息
     * @return
     */
    @ApiOperation("获取抢修队伍人员信息")
    @PostMapping("/queryPage")
    public AjaxResult queryPage(String pageNum,String pageSize,String team_name,String name) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult wt = emergencyWorkersService.queryPage(pageNum,pageSize,team_name,name);
        return wt;
    }

    /**
     * 获取抢修队伍人员信息
     * @return
     */
    @ApiOperation("测试App调用")
    @PostMapping("/testApp")
    public Object testApp() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return user;
    }


}

package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.project.emergencyCommand.service.IAppEmergencyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App使用后台数据接口
 */
@Api(tags = "App使用后台数据接口")
@RestController
@CrossOrigin
@RequestMapping("/appEmergency")
public class AppEmergencyController {

    @Autowired
    private IAppEmergencyService appEmergencyService;


}

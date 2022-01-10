package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.EmergencyVehicles;
import com.alonginfo.project.emergencyCommand.service.IEmergencyVehicles;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emergencyVehicles")
public class EmergencyVehiclesController extends BaseController {

    @Autowired
    private IEmergencyVehicles emergencyVehicles;

    @PostMapping("/queryPage")
    public AjaxResult queryPage(String pageNum, String pageSize, String affiliatedUnit, String nameRepairCar, String numbeRepairCar, Integer typeRepairCar){
        AjaxResult  list = emergencyVehicles.queryPage(pageNum,pageSize,affiliatedUnit,nameRepairCar,numbeRepairCar,typeRepairCar);
        return list;
    }
}

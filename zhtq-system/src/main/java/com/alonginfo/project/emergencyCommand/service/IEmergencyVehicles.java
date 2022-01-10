package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.EmergencyVehicles;

import java.util.List;

public interface IEmergencyVehicles {
    AjaxResult queryPage(String pageNum, String pageSize, String affiliatedUnit, String nameRepairCar, String numbeRepairCar, Integer typeRepairCar);
}

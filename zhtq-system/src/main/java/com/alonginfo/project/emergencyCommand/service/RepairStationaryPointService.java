package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RepairInfo;



public interface RepairStationaryPointService {
    AjaxResult queryPage(String pageNum, String pageSize, RepairInfo repairInfo);
}

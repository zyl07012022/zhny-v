package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.EmergencyVehicles;
import com.alonginfo.project.emergencyCommand.mapper.EmergencyVehiclesMapper;
import com.alonginfo.project.emergencyCommand.service.IEmergencyVehicles;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyVehiclesImpl implements IEmergencyVehicles {

    @Autowired
    private EmergencyVehiclesMapper emergencyVehiclesMapper;

//    @Override
//    public List<EmergencyVehicles> queryPage(PageInfo<EmergencyVehicles> pageInfo) {
//        return null;
////        return emergencyVehiclesMapper.queryPage(pageInfo);
//    }

    @Override
    public AjaxResult queryPage(String pageNum, String pageSize, String affiliatedUnit, String nameRepairCar, String numbeRepairCar, Integer typeRepairCar) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<EmergencyVehicles> list = emergencyVehiclesMapper.queryPage(affiliatedUnit,nameRepairCar,numbeRepairCar,typeRepairCar);
        return AjaxResult.success(new PageInfo(list));
    }
}

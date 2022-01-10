package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RepairInfo;
import com.alonginfo.project.emergencyCommand.mapper.RepairStationaryPointMapper;
import com.alonginfo.project.emergencyCommand.service.RepairStationaryPointService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RepairStationaryPointServiceImpl implements RepairStationaryPointService {

    @Resource
    private RepairStationaryPointMapper repairStationaryPointMapper;

    @Override
    public AjaxResult queryPage(String pageNum, String pageSize, RepairInfo repairInfo) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String,String>> list = repairStationaryPointMapper.queryPage(repairInfo);
        return AjaxResult.success(new PageInfo(list));
    }


}

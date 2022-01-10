package com.alonginfo.project.inspectionApp.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionApp.mapper.InspectionAppMapper;
import com.alonginfo.project.inspectionApp.service.InspectionAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InspectionAppServiceImpl implements InspectionAppService {
    @Resource
    InspectionAppMapper inspectionAppMapper;

    @Override
    public AjaxResult selectInspection(String patrolSendData) {
        return AjaxResult.success(inspectionAppMapper.selectInspection(patrolSendData));
    }
}

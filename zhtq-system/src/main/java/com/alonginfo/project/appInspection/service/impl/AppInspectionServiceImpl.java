package com.alonginfo.project.appInspection.service.impl;

import com.alonginfo.common.utils.IdUtils;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.appInspection.entities.InspectionsRecord;
import com.alonginfo.project.appInspection.entities.Transformer;
import com.alonginfo.project.appInspection.mapper.AppInspectionMapper;
import com.alonginfo.project.appInspection.service.IAppInspectionService;
import com.alonginfo.project.component.entities.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AppInspectionServiceImpl implements IAppInspectionService {

    @Resource
    private AppInspectionMapper appInspectionMapper;

    @Override
    public InspectionsRecord queryRestTime(String patrolSchemeNumber) {
        return appInspectionMapper.queryRestTime(patrolSchemeNumber);
    }

    @Override
    public InspectionsRecord queryBaseInfo(String patrolSchemeNumber) {
        return appInspectionMapper.queryBaseInfo(patrolSchemeNumber);
    }

    @Override
    public int addInspectionDetails(InspectionDetails inspectionDetails) {
        inspectionDetails.setGuid(IdUtils.fastSimpleUUID());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        inspectionDetails.setPatrolData(sdf.format(new Date()));
        String username = appInspectionMapper.queryUsername(inspectionDetails.getPatrolSchemeNumber());
        inspectionDetails.setPatrolPerson(username);
        return appInspectionMapper.addInspectionDetails(inspectionDetails);
    }

    @Override
    public int addHighVolSwitchInfo(List<Switch> list) {
        list.forEach(s->{
            s.setGuid(IdUtils.fastSimpleUUID());
        });
        return appInspectionMapper.addHighVolSwitchInfo(list);
    }

    @Override
    public int addTransformerInfo(List<Transformer> list) {
        list.forEach(s->{
            s.setGuid(IdUtils.fastSimpleUUID());
        });
        return appInspectionMapper.addTransformerInfo(list);
    }

    @Override
    public AjaxResult updateState(Map map) {
        try {
            appInspectionMapper.updateState(map);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateVoltageDetail(LowVoltage lowVoltage) {
        try {
            appInspectionMapper.updateVoltageDetail(lowVoltage);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateTrigonalImbalance(NotBalance notBalance) {
        try {
            appInspectionMapper.updateTrigonalImbalance(notBalance);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateOverloadDetail(HeavyLoad heavyLoad) {
        try {
            appInspectionMapper.updateOverloadDetail(heavyLoad);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }
}

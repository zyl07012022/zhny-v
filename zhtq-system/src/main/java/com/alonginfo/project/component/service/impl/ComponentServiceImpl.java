package com.alonginfo.project.component.service.impl;


import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.project.component.entities.HeavyLoad;
import com.alonginfo.project.component.entities.LowVoltage;
import com.alonginfo.project.component.entities.NotBalance;
import com.alonginfo.project.component.entities.ReponseResult;
import com.alonginfo.project.component.mapper.ComponentMapper;
import com.alonginfo.project.component.service.ComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ComponentServiceImpl implements ComponentService {

    @Resource
    private ComponentMapper componentMapper;

    @Override
    public Map<String, Object> queryFormData(String patrolSchemeNumber) {
        Map<String, Object> inspectionDetails = componentMapper.queryFormData(patrolSchemeNumber);
        inspectionDetails.put("userId",StringUtils.valueOf(inspectionDetails.get("userId")).replace("//",";"));
        inspectionDetails.put("courtsId",StringUtils.valueOf(inspectionDetails.get("courtsId")).replace("//",";"));
        return inspectionDetails;
    }

    @Override
    public List<ReponseResult> queryKgbhData(String patrolSchemeNumber) {
        List<Map<String, String>> list = componentMapper.queryKgbhData(patrolSchemeNumber);
        List<ReponseResult> results = new ArrayList<>();
        List<String> switchNumList = new ArrayList<>();
        List<String> switchTypeList = new ArrayList<>();
        switchNumList.add("开关编号");
        switchTypeList.add("开关运行状态");
        list.stream().forEach(s -> {
            switchNumList.add(s.get("switchNumber"));
        });
        list.stream().forEach(s -> {
            switchTypeList.add(s.get("switchType"));
        });
        ReponseResult reponseResult = new ReponseResult();
        reponseResult.setForwardList(switchNumList);
        reponseResult.setUnderList(switchTypeList);
        results.add(reponseResult);
        return results;
    }

    @Override
    public List<ReponseResult> queryTransformerTemperature(String patrolSchemeNumber) {
        List<Map<String, String>> list = componentMapper.queryTransformerTemperature(patrolSchemeNumber);
        List<ReponseResult> results = new ArrayList<>();
        List<String> transformerNumList = new ArrayList<>();
        List<String> transformerTempList = new ArrayList<>();
        if (list != null) {
            if (list.size() % 2 != 0) {
                for (int i = 0; i < list.size() / 2 + 1; i++) {
                    Map<String, String> map = list.get(i);
                    transformerNumList.add(map.get("transformerNumber"));
                    transformerNumList.add(map.get("transformerTemperature"));
                }
                for (int i = list.size() / 2 + 1; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    transformerTempList.add(map.get("transformerNumber"));
                    transformerTempList.add(map.get("transformerTemperature"));
                }
                transformerTempList.add("");
                transformerTempList.add("");
            } else {
                for (int i = 0; i < (list.size() + 1) / 2; i++) {
                    Map<String, String> map = list.get(i);
                    transformerNumList.add(map.get("transformerNumber"));
                    transformerNumList.add(map.get("transformerTemperature"));
                }
                for (int i = (list.size() + 1) / 2; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    transformerTempList.add(map.get("transformerNumber"));
                    transformerTempList.add(map.get("transformerTemperature"));
                }
            }
        }
        ReponseResult reponseResult = new ReponseResult();
        reponseResult.setForwardList(transformerNumList);
        reponseResult.setUnderList(transformerTempList);
        results.add(reponseResult);
        return results;
    }

    @Override
    public NotBalance queryNotBalance(String patrolSchemeNumber) {
        NotBalance notBalance = componentMapper.queryNotBalance(patrolSchemeNumber);
        return notBalance;
    }

    @Override
    public LowVoltage queryLowVoltage(String patrolSchemeNumber) {
        LowVoltage lowVoltage = componentMapper.queryLowVoltage(patrolSchemeNumber);
        return lowVoltage;
    }

    @Override
    public HeavyLoad queryHeavyLoad(String patrolSchemeNumber) {
        HeavyLoad heavyLoad = componentMapper.queryHeavyLoad(patrolSchemeNumber);
        return heavyLoad;
    }

    @Override
    public List<Map<String, Object>> queryTableData(String patrolSchemeNumber) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = componentMapper.queryTableData(patrolSchemeNumber);
        String[] patrolObject = StringUtils.valueOf( map.get("patrolObject")).split("//");
        String[] patrolTypes = StringUtils.valueOf( map.get("patrolTypes")).split("//");
        String[] patrolAbnormal = StringUtils.valueOf( map.get("patrolAbnormal")).split("//");
        String[] note = StringUtils.valueOf( map.get("feedback")).split("//");
        for (int i = 0; i <patrolObject.length ; i++) {
            Map<String, Object> res = new LinkedHashMap<String, Object>();
            if(patrolObject.length>i) {
                res.put("patrolObject", StringUtils.isEmpty(patrolObject) ? "" : patrolObject[i]);
            }else {
                res.put("patrolObject","");
            }
            if(patrolTypes.length>i) {
                res.put("patrolTypes", patrolTypes[i]);
            }else{
                res.put("patrolTypes","");
            }
            if(patrolAbnormal.length>i) {
                res.put("patrolAbnormal", patrolAbnormal[i]);
            }else{
                res.put("patrolAbnormal","");
            }
            if(note.length>i) {
                res.put("note", note[i]);
            }else{
                res.put("note","");
            }
            list.add(res);
        }
        return list;
    }

}

package com.alonginfo.project.inspectionWorkOrder.service;


import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.entity.Inspection;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface InspectionService {
    List<Map<String,Object>> getInspection(CheckRepairOrder map);
    List<Map<String, String>> queryUser(String patrolSchemeNumber);
    List<Map<String, String>> queryCourts(String patrolSchemeNumber);
    List<Map<String, String>> queryDispatch(Map map);
    List<Map<String, String>> queryDispatchOther(String patrolSchemeNumber);
    void planAdd(CheckRepairOrder checkRepairOrder);
    void planChange(CheckRepairOrder checkRepairOrder);
    void deleteBypatrolSchemeNumber(String patrolSchemeNumber);
    void operate(String patrolSchemeNumber);
    void archived(String patrolSchemeNumber);
    Map<String, Object> export(String patrolStatus, HttpServletRequest request);
}

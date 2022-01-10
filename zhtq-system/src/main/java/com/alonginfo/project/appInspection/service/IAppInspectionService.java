package com.alonginfo.project.appInspection.service;


import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.appInspection.entities.InspectionsRecord;
import com.alonginfo.project.appInspection.entities.Transformer;
import com.alonginfo.project.component.entities.*;

import java.util.List;
import java.util.Map;

public interface IAppInspectionService {
    /**
     * 查询剩余时间
     * @param patrolSchemeNumber
     * @return
     */
    InspectionsRecord queryRestTime(String patrolSchemeNumber);

    /**
     * 查询基本信息
     * @param patrolSchemeNumber
     * @return
     */
    InspectionsRecord queryBaseInfo(String patrolSchemeNumber);

    /**
     * 插入巡检计划数据
     * @param inspectionDetails
     * @return
     */
    int addInspectionDetails(InspectionDetails inspectionDetails);

    /**
     * 插入高压开关信息
     * @param list
     * @return
     */
    int addHighVolSwitchInfo(List<Switch> list);

    /**
     * 插入变压器开关信息
     * @param list
     * @return
     */
    int addTransformerInfo(List<Transformer> list);

    /**
     * 更改工单状态
     * @param map
     * @return
     */
    AjaxResult updateState(Map map);

    AjaxResult updateVoltageDetail(LowVoltage lowVoltage);

    AjaxResult updateTrigonalImbalance(NotBalance notBalance);

    AjaxResult updateOverloadDetail(HeavyLoad heavyLoad);
}

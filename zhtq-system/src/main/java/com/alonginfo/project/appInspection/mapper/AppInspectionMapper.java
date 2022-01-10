package com.alonginfo.project.appInspection.mapper;

import com.alonginfo.project.appInspection.entities.InspectionsRecord;
import com.alonginfo.project.appInspection.entities.Transformer;
import com.alonginfo.project.component.entities.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppInspectionMapper {
    InspectionsRecord queryRestTime(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    InspectionsRecord queryBaseInfo(@Param("patrolSchemeNumber") String patrolSchemeNumber);

    /**
     * 查询巡检人员名称
     * @param patrolSchemeNumber
     * @return
     */
    String queryUsername(@Param("patrolSchemeNumber") String patrolSchemeNumber);
    int addInspectionDetails(InspectionDetails inspectionDetails);
    int addHighVolSwitchInfo(List<Switch> list);
    int addTransformerInfo(List<Transformer> list);
    Integer updateState(Map map);

    Integer updateVoltageDetail(LowVoltage lowVoltage);
    Integer updateTrigonalImbalance(NotBalance notBalance);
    Integer updateOverloadDetail(HeavyLoad heavyLoad);
}

package com.alonginfo.project.alarmOrder.mapper;

import com.alonginfo.project.alarmOrder.entity.YcyjDevice;
import com.alonginfo.project.alarmOrder.vo.Params;

import java.util.List;
import java.util.Map;

/**
 * @Author ljg
 * @Date 2021/7/15 10:03
 */
public interface TrWaringDataMapper {
    List<Map<String,Object>> queryTrList();
}

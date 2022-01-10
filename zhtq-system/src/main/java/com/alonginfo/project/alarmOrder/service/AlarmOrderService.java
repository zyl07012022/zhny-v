package com.alonginfo.project.alarmOrder.service;

import com.alonginfo.project.alarmOrder.vo.Params;

import java.util.Map;

/**
 * @author 崔亚魁
 */
public interface AlarmOrderService {

    Map<String,Object> calculate(Params params);
}

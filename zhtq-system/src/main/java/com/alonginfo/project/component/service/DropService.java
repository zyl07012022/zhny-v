package com.alonginfo.project.component.service;

import com.alonginfo.project.component.entities.DropParams;

import java.util.List;
import java.util.Map;

/**
 * @Author ljg
 * @Date 2021/7/15 13:43
 */
public interface DropService {
    List<DropParams> queryStList(String query);

    List<DropParams> queryFeederList(DropParams dropParams);

    List<DropParams> queryDeviceList(Map map);

    List<Map<String, Object>> queryOrgId();
}

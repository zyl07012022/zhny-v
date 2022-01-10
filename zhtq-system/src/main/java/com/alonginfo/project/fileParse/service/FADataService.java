package com.alonginfo.project.fileParse.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.DeviceFaultInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ljg
 * @date 2021/2/8
 */

public interface FADataService {

    void addDeviceInfo(Map<String, String> map2);

    void addFileInfo(Map<String, String> map);

    List<Map<String, Object>> getDMSData(String status,String deviceId);

    void addGisOrder(Map map);

    AjaxResult importExcelData(List<DeviceFaultInfo> faultInfoList) throws IOException;
}

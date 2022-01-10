package com.alonginfo.project.emergencyCommand.common.service;

import java.util.List;
import java.util.Map;

public interface DropListService {
    List<Map<String,Object>> selectDropList(String codeType);

    List<Map<String,Object>> selectUserName();

    List<Map<String,Object>> selectCourtsName();
}

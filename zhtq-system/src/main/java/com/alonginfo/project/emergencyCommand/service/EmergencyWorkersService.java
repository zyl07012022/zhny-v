package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.agriculture.domain.Weather;

import java.util.List;
import java.util.Map;

public interface EmergencyWorkersService {
    AjaxResult queryPage(String pageNum, String pageSize, String team_name, String name);
}

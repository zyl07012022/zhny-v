package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.framework.web.domain.AjaxResult;

public interface RescueTeamsService {
    AjaxResult queryPage(String pageNum, String pageSize, String affiliated_unit, String team_name, String resource_categories, String resource_type);
}

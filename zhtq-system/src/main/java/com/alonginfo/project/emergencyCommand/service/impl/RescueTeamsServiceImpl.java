package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.mapper.RescueTeamsMapper;
import com.alonginfo.project.emergencyCommand.service.RescueTeamsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RescueTeamsServiceImpl implements RescueTeamsService {
    @Resource
    RescueTeamsMapper rescueTeamsMapper;

    @Override
    public AjaxResult queryPage(String pageNum, String pageSize, String affiliated_unit, String team_name, String resource_categories, String resource_type) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = rescueTeamsMapper.queryPage(affiliated_unit, team_name, resource_categories, resource_type);
        return AjaxResult.success(new PageInfo(list));
    }
}

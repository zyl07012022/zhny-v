package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.mapper.EmergencyWorkersMapper;
import com.alonginfo.project.emergencyCommand.service.EmergencyWorkersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmergencyWorkersServiceImpl implements EmergencyWorkersService {

    @Autowired
    private EmergencyWorkersMapper emergencyWorkersMapper;

    @Override
    public AjaxResult queryPage(String pageNum, String pageSize, String team_name, String name) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String,String>> list = emergencyWorkersMapper.queryPage(team_name,name);
        return AjaxResult.success(new PageInfo(list));
    }
}

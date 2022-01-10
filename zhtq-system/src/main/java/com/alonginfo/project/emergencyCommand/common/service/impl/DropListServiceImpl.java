package com.alonginfo.project.emergencyCommand.common.service.impl;

import com.alonginfo.project.emergencyCommand.common.mapper.DropListMapper;
import com.alonginfo.project.emergencyCommand.common.service.DropListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DropListServiceImpl implements DropListService {
    @Resource
    DropListMapper dropListMapper;

    @Override
    public List<Map<String,Object>> selectDropList(String codeType) {
        return dropListMapper.selectDropList(codeType);
    }

    @Override
    public List<Map<String, Object>> selectUserName() {
        return dropListMapper.selectUserName();
    }
    @Override
    public List<Map<String, Object>> selectCourtsName() {
        return dropListMapper.selectCourtsName();
    }
}

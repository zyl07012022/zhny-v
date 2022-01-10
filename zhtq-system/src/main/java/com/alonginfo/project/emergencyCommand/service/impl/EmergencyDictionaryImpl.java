package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.project.emergencyCommand.entity.EmergencyDictionary;
import com.alonginfo.project.emergencyCommand.mapper.EmergencyDictionaryMapper;
import com.alonginfo.project.emergencyCommand.service.IEmergencyDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyDictionaryImpl implements IEmergencyDictionary {

    @Autowired
    private EmergencyDictionaryMapper emergencyDictionaryMapper;

    @Override
    public List<EmergencyDictionary> find(Integer codeType) {
        return emergencyDictionaryMapper.find(codeType);
    }
}

package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.project.emergencyCommand.entity.EmergencyDictionary;

import java.util.List;

public interface IEmergencyDictionary {
    List<EmergencyDictionary> find(Integer codeType);
}

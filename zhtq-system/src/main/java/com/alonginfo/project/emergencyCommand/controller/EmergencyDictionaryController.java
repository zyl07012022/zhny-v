package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.EmergencyDictionary;
import com.alonginfo.project.emergencyCommand.service.IEmergencyDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary")
public class EmergencyDictionaryController {

    @Autowired
    private IEmergencyDictionary emergencyDictionary;

    @GetMapping("/find")
    public AjaxResult find(Integer codeType){
        List<EmergencyDictionary> list = emergencyDictionary.find(codeType);
        return AjaxResult.success(list);
    }
}

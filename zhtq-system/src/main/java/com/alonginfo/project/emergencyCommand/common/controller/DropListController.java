package com.alonginfo.project.emergencyCommand.common.controller;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.common.mapper.DropListMapper;
import com.alonginfo.project.emergencyCommand.common.service.DropListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class DropListController {
    @Autowired
    private DropListService dropListService;

    @Resource
    private DropListMapper dropListMapper;

    @GetMapping("/dropList")
    public AjaxResult dropList(@RequestParam(value = "codeType") String codeType) {
        return AjaxResult.success(dropListService.selectDropList(codeType));
    }

    @GetMapping("/userNameList")
    public AjaxResult selectUserName(){
        return AjaxResult.success(dropListService.selectUserName());
    }
    @GetMapping("/courtsNameList")
    public AjaxResult selectCourtsName(){
        return AjaxResult.success(dropListService.selectCourtsName());
    }
}

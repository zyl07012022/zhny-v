package com.alonginfo.project.component.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.component.entities.DropParams;
import com.alonginfo.project.component.service.DropService;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author ljg
 * @Date 2021/7/15 13:40
 */
@Api(tags = "")
@RestController
@RequestMapping("/dropList")
@CrossOrigin
public class DropController {

    @Autowired
    private DropService dropListService;

    /**
     * 变电站下拉选框集合
     * @return
     */
    @GetMapping("/stList")
    public AjaxResult stList( String query){
       List<DropParams> list =  dropListService.queryStList(query);
        return AjaxResult.success(list);
    }

    /**
     * 根据变电站下拉选框集合
     * @param dropParams
     * @return
     */
    @PostMapping("/feederList")
    public AjaxResult feederList(@RequestBody DropParams dropParams){
        List<DropParams> list =  dropListService.queryFeederList(dropParams);
        return AjaxResult.success(list);
    }

    /**
     * 根据馈线、巡检对象类型(设备类型)获取设备名称下拉选框数据
     * @param dropParams
     * @return
     */
    @PostMapping("/deviceList")
    public AjaxResult deviceList(@RequestBody Map map){
        List<DropParams> list =  dropListService.queryDeviceList(map);
        return AjaxResult.success(list);
    }

    /**
     * 查询深圳市的组织ID
     * @param
     * @return
     */
    @GetMapping("/queryOrgId")
    public AjaxResult queryOrgId() {
        List<Map<String,Object>> list =  dropListService.queryOrgId();
        return AjaxResult.success(list);
    }

}

package com.alonginfo.project.component.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.component.entities.*;
import com.alonginfo.project.component.service.ComponentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author 袁坤
 * @date 2020年9月22日
 */
@Api(tags = "")
@RestController
@RequestMapping("/component")
@CrossOrigin
public class ComponentController  {


    @Resource
    public ComponentService componentService;

    @GetMapping(value = "/queryFormData/{patrolSchemeNumber}")
    public AjaxResult queryFormData( @PathVariable String patrolSchemeNumber){
        Map<String,Object> map = new HashMap<String,Object>();
         Map<String,Object> inspectionDetails = componentService.queryFormData(patrolSchemeNumber);
        if(!StringUtils.isEmpty(inspectionDetails)){
            map.put("data",inspectionDetails);
            map.put("suc",true);
        }else{
            map.put("data",null);
            map.put("suc",false);
        }
        return AjaxResult.success(map);

    }

    @GetMapping(value = "/queryTableData/{patrolSchemeNumber}")
    public AjaxResult queryTableData( @PathVariable String patrolSchemeNumber){
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> inspectionDetails = componentService.queryTableData(patrolSchemeNumber);
        if(inspectionDetails!=null &&inspectionDetails.size()>0){
            map.put("data",inspectionDetails);
            map.put("suc",true);
        }else{
            map.put("data",null);
            map.put("suc",false);
        }
        return AjaxResult.success(map);

    }

    @GetMapping(value = "/querykgbhData/{patrolSchemeNumber}")
    public AjaxResult querykgbhData( @PathVariable String patrolSchemeNumber){
        List<ReponseResult> list = componentService.queryKgbhData(patrolSchemeNumber);
        return AjaxResult.success(list);
    }

    @GetMapping(value = "/queryTransformerTemperature/{patrolSchemeNumber}")
    public AjaxResult queryTransformerTemperature( @PathVariable String patrolSchemeNumber){
        List<ReponseResult> list = componentService.queryTransformerTemperature(patrolSchemeNumber);
        return AjaxResult.success(list);
    }


    @GetMapping(value = "/queryNotBalance/{patrolSchemeNumber}")
    public AjaxResult queryNotBalance( @PathVariable String patrolSchemeNumber){
        NotBalance notBalance = componentService.queryNotBalance(patrolSchemeNumber);
        return AjaxResult.success(notBalance);

    }

    @GetMapping(value = "/queryLowVoltage/{patrolSchemeNumber}")
    public AjaxResult queryLowVoltage( @PathVariable String patrolSchemeNumber){
        LowVoltage lowVoltage = componentService.queryLowVoltage(patrolSchemeNumber);
        return AjaxResult.success(lowVoltage);

    }

    @GetMapping(value = "/queryHeavyLoad/{patrolSchemeNumber}")
    public AjaxResult queryHeavyLoad( @PathVariable String patrolSchemeNumber){
        HeavyLoad heavyLoad = componentService.queryHeavyLoad(patrolSchemeNumber);
        return AjaxResult.success(heavyLoad);
    }

}

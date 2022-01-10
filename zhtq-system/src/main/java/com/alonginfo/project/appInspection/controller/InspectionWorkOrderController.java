package com.alonginfo.project.appInspection.controller;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.appInspection.entities.InspectionsRecord;
import com.alonginfo.project.appInspection.entities.Transformer;
import com.alonginfo.project.appInspection.service.IAppInspectionService;
import com.alonginfo.project.component.entities.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 袁坤
 * @date 2020年9月22日
 */
@RestController
@RequestMapping("/inspectionWorkOrder")
@CrossOrigin
public class InspectionWorkOrderController {

    @Resource
    private IAppInspectionService appInspectionService;

    @GetMapping(value = "/queryRestTime")
    public AjaxResult queryRestTime(@RequestParam("patrolSchemeNumber") String patrolSchemeNumber){
        InspectionsRecord inspectionsRecord = appInspectionService.queryRestTime(patrolSchemeNumber);
        return AjaxResult.success(inspectionsRecord);
    }
    @GetMapping(value = "/queryBaseInfo")
    public AjaxResult queryBaseInfo(@RequestParam("patrolSchemeNumber") String patrolSchemeNumber){
        InspectionsRecord inspectionsRecord = appInspectionService.queryBaseInfo(patrolSchemeNumber);
        return AjaxResult.success(inspectionsRecord);
    }
    @GetMapping(value = "/queryAreaInspection/{patrolSchemeNumber}")
    public AjaxResult queryAreaInspection(@PathVariable String patrolSchemeNumber){

        return AjaxResult.success(null);
    }

    @PostMapping (value = "/addInspectionDetails/{patrolSchemeNumber}")
    public AjaxResult addInspectionDetails(@RequestBody InspectionDetails inspectionDetails,@PathVariable String patrolSchemeNumber){
        inspectionDetails.setPatrolSchemeNumber(patrolSchemeNumber);
        int result = appInspectionService.addInspectionDetails(inspectionDetails);
        return AjaxResult.success(result);
    }

    @PostMapping (value = "/addHighVolSwitchInfo/{patrolSchemeNumber}")
    public AjaxResult addHighVolSwitchInfo(@RequestBody List<Switch> list,@PathVariable String patrolSchemeNumber ){
        for (Switch s:list) {
            s.setPatrolSchemeNumber(patrolSchemeNumber);
        }
        int result = appInspectionService.addHighVolSwitchInfo(list);
        return AjaxResult.success(result);
    }

    @PostMapping (value = "/addTransformerInfo/{patrolSchemeNumber}")
    public AjaxResult addTransformerInfo( @RequestBody List<Transformer> list,@PathVariable String patrolSchemeNumber){
        for (Transformer t:list) {
            t.setPatrolSchemeNumber(patrolSchemeNumber);
        }
        int result = appInspectionService.addTransformerInfo(list);
        return AjaxResult.success(result);
    }

    /**
     * 修改工单状态
     *
     * @return
     */
    @PostMapping("/updateState")
    public AjaxResult updateState(@RequestBody Map<String,Object> map) {
        return appInspectionService.updateState(map);
    }

    /**
     * 台区低压selectInspection
     *
     * @return
     */
    @PostMapping("/updateVoltageDetail/{patrolSchemeNumber}")
    public AjaxResult updateVoltageDetail(@RequestBody Map<String,Object> map,@PathVariable String patrolSchemeNumber) {
        LowVoltage lowVoltage = new LowVoltage();
        Map map1 = (LinkedHashMap)map.get("params");
        lowVoltage.setPatrolSchemeNumber(patrolSchemeNumber);
        lowVoltage.setTeama(String.valueOf(map1.get("loadRatea")));
        lowVoltage.setTeamb(String.valueOf(map1.get("loadRateb")));
        lowVoltage.setTeamc(String.valueOf(map1.get("loadRatec")));
        lowVoltage.setMinv(String.valueOf(map1.get("minv")));
        lowVoltage.setTimeMinimumImbalance(String.valueOf(map1.get("timeMinimumImbalance")));
        return appInspectionService.updateVoltageDetail(lowVoltage);
    }

    /**
     * 三相不平衡
     *
     * @return
     */
    @PostMapping("/updateTrigonalImbalance/{patrolSchemeNumber}")
    public AjaxResult updateTrigonalImbalance(@RequestBody Map<String,Object> map,@PathVariable String patrolSchemeNumber) {
        NotBalance notBalance = new NotBalance();
        Map map1 = (LinkedHashMap)map.get("params");
        notBalance.setLoadRatea(String.valueOf(map1.get("loadRatea")));
        notBalance.setLoadRateb(String.valueOf(map1.get("loadRateb")));
        notBalance.setLoadRateb(String.valueOf(map1.get("loadRatec")));
        notBalance.setMaxImbalance(String.valueOf(map1.get("maxImbalance")));
        notBalance.setTimeMaximumImbalance(String.valueOf(map1.get("timeMinimumImbalance")));
        return appInspectionService.updateTrigonalImbalance(notBalance);
    }

    /**
     * 台区重过载
     *
     * @return
     */
    @PostMapping("/updateOverloadDetail/{patrolSchemeNumber}")
    public AjaxResult updateOverloadDetail(@RequestBody Map<String,Object> map,@PathVariable String patrolSchemeNumber) {
        HeavyLoad heavyLoad = new HeavyLoad();
        Map map1 = (LinkedHashMap)map.get("params");
        heavyLoad.setLoadRatea(String.valueOf(map1.get("loadRatea")));
        heavyLoad.setLoadRateb(String.valueOf(map1.get("loadRateb")));
        heavyLoad.setLoadRateb(String.valueOf(map1.get("loadRatec")));
        heavyLoad.setMaxImbalance(String.valueOf(map1.get("maxImbalance")));
        heavyLoad.setTimeMaximumImbalance(String.valueOf(map1.get("timeMaximumImbalance")));
        return appInspectionService.updateOverloadDetail(heavyLoad);
    }
}

package com.alonginfo.project.inspectionWorkOrder.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.entity.Inspection;
import com.alonginfo.project.inspectionWorkOrder.service.impl.IInspectionServiceImpl;
import com.alonginfo.utils.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inspection")
public class    InspectionController {

    @Autowired
    private IInspectionServiceImpl inspectionService;

    /**
     * 查询巡检信息
     * @param map
     * @return
     */
    @ApiOperation("巡检信息")
    @PostMapping("/queryPage")
    public AjaxResult getInspection(@RequestBody CheckRepairOrder map){
        PageHelper.startPage(map.getPageNum(), map.getPageSize());
        List<Map<String,Object>> list = inspectionService.getInspection(map);
        return AjaxResult.success(new PageInfo(list));
    }

    /**
     * 影响用户信息
     *
     * @return
     */
    @ApiOperation("影响用户信息")
    @PostMapping("/queryUser")
    public AjaxResult queryUser(String pageNum, String pageSize,String patrolSchemeNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = inspectionService.queryUser(patrolSchemeNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    /**
     * 影响台区信息
     *
     * @return
     */
    @ApiOperation("影响用户信息")
    @GetMapping("/queryCourts")
    public AjaxResult queryCourts(String pageNum, String pageSize,String patrolSchemeNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = inspectionService.queryCourts(patrolSchemeNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    /**
     * 派工信息
     *
     * @return
     */
    @ApiOperation("派工信息")
    @GetMapping("/queryDispatch")
    public AjaxResult queryDispatch(@RequestBody Map map) {
        List<Map<String, String>> list = inspectionService.queryDispatch(map);
        return AjaxResult.success(list);
    }

    @GetMapping("/queryDispatchOther")
    public AjaxResult queryDispatchOther(String patrolSchemeNumber) {
        List<Map<String, String>> list = inspectionService.queryDispatchOther(patrolSchemeNumber);
        return AjaxResult.success(list);
    }

    /**
     * 计划巡检发起
     * @param checkRepairOrder
     * @return
     */
    @ApiOperation("计划巡检发起")
    @PostMapping(value = "/planAdd")
    public AjaxResult planAdd(@RequestBody CheckRepairOrder checkRepairOrder){
        inspectionService.planAdd(checkRepairOrder);
        return AjaxResult.success();
    }

    /**
     * 计划巡检修改
     * @param checkRepairOrder
     * @return
     */
    @ApiOperation("计划巡检修改")
    @PostMapping(value = "/planChange")
    public AjaxResult planChange(CheckRepairOrder checkRepairOrder){
        inspectionService.planChange(checkRepairOrder);
        return AjaxResult.success();
    }

    /**
     * 计划巡检删除
     * @param patrolSchemeNumber
     * @return
     */
    @ApiOperation("计划巡检删除")
    @PostMapping(value = "/delete")
    public AjaxResult deleteBypatrolSchemeNumber(String patrolSchemeNumber){
        inspectionService.deleteBypatrolSchemeNumber(patrolSchemeNumber);
        return AjaxResult.success();
    }

    /**
     * 工单派发
     * @param patrolSchemeNumber
     * @return
     */
    @ApiOperation("工单派发")
    @PostMapping(value = "/send")
    public AjaxResult operate(String patrolSchemeNumber){
        inspectionService.operate(patrolSchemeNumber);
        return AjaxResult.success();
    }

    /**
     * 工单归档
     * @param patrolSchemeNumber
     * @return
     */
    @ApiOperation("工单归档")
    @PostMapping(value = "/archived")
    public AjaxResult archived(String patrolSchemeNumber){
        inspectionService.archived(patrolSchemeNumber);
        return AjaxResult.success();
    }

    /**
     * 列表导出
     * @return
     */
    @ApiOperation("列表导出")
    @PostMapping(value = "/export")
    public AjaxResult export(@RequestBody Map map, HttpServletRequest request){
        String patrolStatus = StringUtils.valueOf(map.get("patrolStatus"));
        Map<String,Object> result =inspectionService.export(patrolStatus,request);
        return AjaxResult.success(result);
    }


}

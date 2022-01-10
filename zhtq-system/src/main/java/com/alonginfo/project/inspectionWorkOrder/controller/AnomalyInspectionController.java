package com.alonginfo.project.inspectionWorkOrder.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.service.AnomalyInspectionService;
import com.alonginfo.utils.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anomalyInspection")
public class AnomalyInspectionController extends BaseController {

    @Resource
    private AnomalyInspectionService anomalyInspectionService;

    /**
     * 异常巡检工单查询
     */
    @ApiOperation("巡检信息")
    @PostMapping("/selectAnomalyOrder")
    public AjaxResult selectAnomalyOrder(@RequestBody CheckRepairOrder checkRepairOrder) {
        PageHelper.startPage(checkRepairOrder.getPageNum(), checkRepairOrder.getPageSize());
        List<Map<String,String>> list = anomalyInspectionService.selectAnomalyOrder(checkRepairOrder);
        return AjaxResult.success(new PageInfo(list));
    }
    /**
     * 影响用户信息
     *
     * @return
     */
    @ApiOperation("影响用户信息")
    @PostMapping("/queryUser")
    public AjaxResult queryUser(String pageNum, String pageSize, @RequestParam(value = "patrolSchemeNumber") String patrolSchemeNumber) {
        AjaxResult ajaxResult = anomalyInspectionService.queryUser(pageNum, pageSize, patrolSchemeNumber);
        return ajaxResult;
    }

    /**
     * 影响台区信息
     *
     * @return
     */
    @ApiOperation("影响台区信息")
    @PostMapping("/queryCourts")
    public AjaxResult queryCourts(String pageNum, String pageSize, @RequestParam(value = "patrolSchemeNumber") String patrolSchemeNumber) {
        AjaxResult ajaxResult = anomalyInspectionService.queryCourts(pageNum, pageSize, patrolSchemeNumber);
        return ajaxResult;
    }

    /**
     * 派工信息
     *
     * @return
     */
    @ApiOperation("派工信息")
    @PostMapping("/queryDispatch")
    public AjaxResult queryDispatch(@RequestParam(value = "teamId",required = false) String teamId) {
        AjaxResult ajaxResult = anomalyInspectionService.queryDispatch(teamId);
        return ajaxResult;
    }

    /**
     * 操作工单状态
     *
     * @return
     */
    @ApiOperation("操作工单状态")
    @PostMapping("/operation")
    public AjaxResult operation(CheckRepairOrder checkRepairOrder) {
        AjaxResult ajaxResult = anomalyInspectionService.operation(checkRepairOrder);
        return ajaxResult;
    }

    /**
     * 批量添加合并工单
     * @param list
     * @return
     */
    @ApiOperation("批量添加合并工单")
    @PostMapping("/suborder")
    public AjaxResult suborder(@RequestBody List<CheckRepairOrder> list) {
        AjaxResult ajaxResult = anomalyInspectionService.suborder(list);
        return ajaxResult;
    }

    /**
     * 列表导出
     * @return
     */
    @ApiOperation("列表导出")
    @PostMapping(value = "/export")
    public AjaxResult export(String patrolStatus){
        List<CheckRepairOrder> list = anomalyInspectionService.export(patrolStatus);
        ExcelUtil<CheckRepairOrder> util = new ExcelUtil<CheckRepairOrder>(CheckRepairOrder.class);
        return util.exportExcel(list, "异常巡检数据");
    }
}

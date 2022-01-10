package com.alonginfo.project.emergencyCommand.controller;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RepairWorkOrder;
import com.alonginfo.project.emergencyCommand.entity.Suborder;
import com.alonginfo.project.emergencyCommand.service.RepairWorkOrderService;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "获取抢修工单信息")
@RestController
@RequestMapping("/repairWorkOrder")
public class RepairWorkOrderController {
    @Autowired
    RepairWorkOrderService repairWorkOrderService;

    /**
     * 获取抢修工单信息
     *
     * @return
     */
    @CrossOrigin
    @ApiOperation("获取抢修队伍信息")
    @PostMapping("/queryPage")
    public AjaxResult queryPage(String pageNum, String pageSize, RepairWorkOrder repairWorkOrder) {
        AjaxResult ajaxResult = repairWorkOrderService.queryPage(pageNum, pageSize, repairWorkOrder);
        return ajaxResult;
    }

    /**
     * 影响用户信息
     *
     * @return
     */
    @ApiOperation("影响用户信息")
    @PostMapping("/queryUser")
    public AjaxResult queryUser(String pageNum, String pageSize, @RequestParam(value = "workNumber") String workNumber) {
        AjaxResult ajaxResult = repairWorkOrderService.queryUser(pageNum, pageSize, workNumber);
        return ajaxResult;
    }

    /**
     * 影响台区信息
     *
     * @return
     */
    @ApiOperation("影响台区信息")
    @PostMapping("/queryCourts")
    public AjaxResult queryCourts(String pageNum, String pageSize, @RequestParam(value = "workNumber") String workNumber) {
        AjaxResult ajaxResult = repairWorkOrderService.queryCourts(pageNum, pageSize, workNumber);
        return ajaxResult;
    }

    /**
     * 派工信息
     *
     * @return
     */
    @ApiOperation("派工信息")
    @PostMapping("/queryDispatch")
    public AjaxResult queryDispatch(@RequestParam(value = "teamId", required = false) String teamId) {
        AjaxResult ajaxResult = repairWorkOrderService.queryDispatch(teamId);
        return ajaxResult;
    }

    /**
     * 操作工单状态
     *
     * @return
     */
    @ApiOperation("操作工单状态")
    @PostMapping("/operation")
    public AjaxResult operation(RepairWorkOrder repairWorkOrder) {
        AjaxResult ajaxResult = repairWorkOrderService.operation(repairWorkOrder);
        return ajaxResult;
    }

    /**
     * 批量添加合并工单
     *
     * @param list
     * @return
     */
    @ApiOperation("批量添加合并工单")
    @PostMapping("/suborder")
    public AjaxResult suborder(@RequestBody List<Suborder> list) {
        AjaxResult ajaxResult = repairWorkOrderService.suborder(list);
        return ajaxResult;
    }

    /**
     * 添加抢修工单
     *
     * @return
     */
    @ApiOperation("添加抢修工单")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody RepairWorkOrder repairWorkOrder) {
        repairWorkOrderService.save(repairWorkOrder);
        return AjaxResult.success();
    }

    /**
     * 导出抢修工单
     *
     * @return
     */
    @ApiOperation("导出抢修工单")
    @PostMapping("/export")
    public AjaxResult export(@RequestBody Map map, HttpServletRequest request) {
        List<RepairWorkOrder> list = repairWorkOrderService.export(map, request);
        ExcelUtil<RepairWorkOrder> util = new ExcelUtil<RepairWorkOrder>(RepairWorkOrder.class);
        return util.exportExcel(list, "抢修工单导表");
    }
}

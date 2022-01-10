package com.alonginfo.project.repairApp.controller;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.repairApp.entity.Orders;
import com.alonginfo.project.repairApp.service.RepairService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/repair")
public class RepairController extends BaseController {
    @Resource
    private RepairService repairService;

    /**
     * 查询抢修列表
     *
     * @return
     */
    @PostMapping("/selectRepair")
    public AjaxResult selectRepair(@RequestParam(value = "sendTime", required = false) String sendTime) {
        return repairService.selectRepair(sendTime);
    }

    /**
     * 查询班长列表
     *
     * @return
     */
    @PostMapping("/selectCaptain")
    public AjaxResult selectCaptain(@RequestParam(value = "station") String station) {
        return repairService.selectCaptain(station);
    }

    /**
     * 查询队伍信息
     *
     * @return
     */
    @PostMapping("/selectTeamInfo")
    public AjaxResult selectTeamInfo(@RequestParam(value = "teamId") String teamId) {
        return repairService.selectTeamInfo(teamId);
    }

    /**
     * 提交工单
     *
     * @return
     */
    @PostMapping("/submit")
    public AjaxResult submit(Orders orders) {
        return repairService.submit(orders);
    }

    /**
     * 修改工单状态
     *
     * @return
     */
    @PostMapping("/updateState")
    public AjaxResult updateState(Orders orders) {
        return repairService.updateState(orders);
    }

    /**
     * 查询工单信息
     *
     * @return
     */
    @PostMapping("/selectOrders")
    public AjaxResult selectOrders(@RequestParam(value = "workNumber") String workNumber) {
        return repairService.selectOrders(workNumber);
    }
}

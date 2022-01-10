package com.alonginfo.project.repairApp.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.repairApp.entity.Orders;

public interface RepairService {
    /**
     * 抢修列表
     *
     * @return
     */
    AjaxResult selectRepair(String sendTime);

    /**
     * 查询班长列表
     *
     * @return
     */
    AjaxResult selectCaptain(String station);

    /**
     * 查询队伍信息
     *
     * @return
     */
    AjaxResult selectTeamInfo(String teamId);

    /**
     * 提交工单
     *
     * @return
     */
    AjaxResult submit(Orders orders);

    /**
     * 修改工单状态
     *
     * @return
     */
    AjaxResult updateState(Orders orders);

    /**
     * 查询工单信息
     *
     * @return
     */
    AjaxResult selectOrders(String workNumber);
}

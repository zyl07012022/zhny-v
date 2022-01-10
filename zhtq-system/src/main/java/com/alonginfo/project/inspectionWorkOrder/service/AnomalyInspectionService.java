package com.alonginfo.project.inspectionWorkOrder.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;

import java.util.List;
import java.util.Map;

public interface AnomalyInspectionService {
    /**
     * 主表格查询
     *
     * @param checkRepairOrder
     * @return
     */
    List<Map<String,String>> selectAnomalyOrder(CheckRepairOrder checkRepairOrder);
    /**
     * 影响用户表格查询
     *
     * @param pageNum
     * @param pageSize
     * @param patrolNumber 工单编号
     * @return
     */
    AjaxResult queryUser(String pageNum, String pageSize, String patrolNumber);

    /**
     * 影响台区表格查询
     *
     * @param pageNum
     * @param pageSize
     * @param patrolNumber 工单编号
     * @return
     */
    AjaxResult queryCourts(String pageNum, String pageSize, String patrolNumber);

    /**
     * 派工信息查询
     *
     * @param teamId 抢修队伍编号
     * @return
     */
    AjaxResult queryDispatch(String teamId);

    /**
     * 操作工单状态
     *
     * @param checkRepairOrder
     * @return
     */
    AjaxResult operation(CheckRepairOrder checkRepairOrder);

    /**
     * 合并多条工单
     *
     * @param list
     * @return
     */
    AjaxResult suborder(List<CheckRepairOrder> list);


    /**
     * 列表导出
     * @param patrolStatus
     * @return
     */
    List<CheckRepairOrder> export(String patrolStatus);
}

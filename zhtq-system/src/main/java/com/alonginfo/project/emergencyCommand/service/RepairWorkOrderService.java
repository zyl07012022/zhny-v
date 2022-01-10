package com.alonginfo.project.emergencyCommand.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RepairWorkOrder;
import com.alonginfo.project.emergencyCommand.entity.Suborder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RepairWorkOrderService {
    /**
     * 主表格查询
     *
     * @param pageNum
     * @param pageSize
     * @param repairWorkOrder
     * @return
     */
    AjaxResult queryPage(String pageNum, String pageSize, RepairWorkOrder repairWorkOrder);

    /**
     * 影响用户表格查询
     *
     * @param pageNum
     * @param pageSize
     * @param workNumber 工单编号
     * @return
     */
    AjaxResult queryUser(String pageNum, String pageSize, String workNumber);

    /**
     * 影响台区表格查询
     *
     * @param pageNum
     * @param pageSize
     * @param workNumber 工单编号
     * @return
     */
    AjaxResult queryCourts(String pageNum, String pageSize, String workNumber);

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
     * @param repairWorkOrder
     * @return
     */
    AjaxResult operation(RepairWorkOrder repairWorkOrder);

    /**
     * 合并多条工单
     *
     * @param list
     * @return
     */
    AjaxResult suborder(List<Suborder> list);

    void save(RepairWorkOrder repairWorkOrder);

    List<RepairWorkOrder> export(Map map, HttpServletRequest request);
}

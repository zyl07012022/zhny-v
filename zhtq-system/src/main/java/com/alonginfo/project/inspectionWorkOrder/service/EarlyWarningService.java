package com.alonginfo.project.inspectionWorkOrder.service;

import com.alonginfo.framework.web.domain.AjaxResult;

import java.util.HashMap;
import java.util.List;

public interface EarlyWarningService {

    /**
     * 查询供电公司、供电所
     *
     * @return
     */
    AjaxResult selectCompany();

    /**
     * 查询预警信息
     *
     * @return
     */
    AjaxResult selectEarlyWarning(String itemType,String orgId);

    /**
     * 生成巡检单号
     *
     * @return
     */
    AjaxResult createWO(List<HashMap<String,Object>> list);

    /**
     * 查询异常列表
     * @param itemType
     * @param orgName
     * @return
     */
    AjaxResult selectAbnormalList(String itemType,String orgName,String patrolAbnormal);

    Integer selectNormalTr(String itemType,String orgId);

    String queryOrgName(String orgId);
}

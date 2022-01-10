package com.alonginfo.project.inspectionApp.service;

import com.alonginfo.framework.web.domain.AjaxResult;

public interface InspectionAppService {
    /**
     * 巡检列表
     *
     * @return
     */
    AjaxResult selectInspection(String patrolSendData);

    /**
     * 提交工单
     *
     * @return
     */
//    AjaxResult submit(Orders orders);
}

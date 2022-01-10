package com.alonginfo.project.alarmOrder.service;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.ByqInfo;

import java.util.Map;

/**
 * @author 任金义
 * @version 1.0
 * @date 2021/7/23 上午 10:54
 * @CreateTime: 2021-07-23 10:54
 */
public interface ByqInfoService {

    /**
     *  修改变压器信息
     * @param map
     * @return
     */
    AjaxResult updateBygInfo(Map map);

    /**
     *  添加变压器信息
     * @param map
     * @return
     */
    void insertBygInfo(Map map);

    /**
     * 删除变压器信息
     * @param map
     * @return
     */
    AjaxResult removeBygInfo(Map map);
}

 

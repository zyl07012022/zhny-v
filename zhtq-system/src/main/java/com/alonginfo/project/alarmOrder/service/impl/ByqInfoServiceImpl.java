package com.alonginfo.project.alarmOrder.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.ByqInfo;
import com.alonginfo.project.alarmOrder.mapper.ByqInfoMapper;
import com.alonginfo.project.alarmOrder.service.ByqInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author 任金义
 * @version 1.0
 * @date 2021/7/23 上午 10:57
 * @CreateTime: 2021-07-23 10:57
 */
@Service
public class ByqInfoServiceImpl implements ByqInfoService {

    @Autowired
    private ByqInfoMapper byqInfoMapper;

    /**
     * 修改变压器信息
     *
     * @param map
     * @return
     */
    @Override
    public AjaxResult updateBygInfo(Map map) {
        try {
             byqInfoMapper.updateBygInfo(map);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    /**
     * 添加变压器信息
     *
     * @param map
     * @return
     */
    @Override
    public void insertBygInfo(Map map) {
        byqInfoMapper.insertBygInfo(map);
    }

    /**
     * 删除变压器信息
     *
     * @param map
     * @return
     */
    @Override
    public AjaxResult removeBygInfo(Map map) {
        if ((byqInfoMapper.removeBygInfo(map)) > 0) {
            return AjaxResult.success("删除成功");
        }
        return  AjaxResult.error("删除失败!");
    }
}


 

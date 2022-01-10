package com.alonginfo.project.inspectionWorkOrder.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.mapper.AnomalyInspectionMapper;
import com.alonginfo.project.inspectionWorkOrder.service.AnomalyInspectionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnomalyInspectionServiceImpl implements AnomalyInspectionService {

    @Resource
    private AnomalyInspectionMapper anomalyInspectionMapper;

    @Override
    public List<Map<String,String>> selectAnomalyOrder(CheckRepairOrder checkRepairOrder) {
        List<Map<String,String>> list = anomalyInspectionMapper.selectAnomalyOrder(checkRepairOrder);
        System.out.println(checkRepairOrder.getActiveName());
        System.out.println(checkRepairOrder.getPatrolStatus());
        return list;
    }

    @Override
    public AjaxResult queryUser(String pageNum, String pageSize, String patrolSchemeNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = anomalyInspectionMapper.queryUser(patrolSchemeNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    @Override
    public AjaxResult queryCourts(String pageNum, String pageSize, String patrolSchemeNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = anomalyInspectionMapper.queryCourts(patrolSchemeNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    @Override
    public AjaxResult queryDispatch(String teamId) {
        if (StringUtil.isEmpty(teamId)) {
            return AjaxResult.success();
        } else {
            Map<String, Object> map = new HashMap<>();
            StringBuffer nameStr = new StringBuffer(); //队长名称
            StringBuffer phoneStr = new StringBuffer(); //手机号
            StringBuffer teamStr = new StringBuffer(); //队员
            StringBuffer vehicleStr = new StringBuffer(); //车牌号
            try {
                List<Map<String, String>> captainList = anomalyInspectionMapper.queryTeamInfo(teamId, "24");
                List<Map<String, String>> teamList = anomalyInspectionMapper.queryTeamInfo(teamId, "-1");
                List<Map<String, String>> vehicleList = anomalyInspectionMapper.queryVehicle(teamId);
                if (captainList.size() > 0) {
                    captainList.forEach(str -> {
                        nameStr.append(str.get("name")).append("、");
                        phoneStr.append(str.get("phone")).append("、");
                    });
                    nameStr.deleteCharAt(nameStr.length() - 1);
                    phoneStr.deleteCharAt(phoneStr.length() - 1);
                    map.put("name", nameStr);
                    map.put("phone", phoneStr);
                }
                if (teamList.size() > 0) {
                    teamList.forEach(str -> {
                        teamStr.append(str.get("name")).append("、");
                    });
                    teamStr.deleteCharAt(teamStr.length() - 1);
                    map.put("team", teamStr);
                }
                if (vehicleList.size() > 0) {
                    vehicleList.forEach(str -> {
                        vehicleStr.append(str.get("numbe_repair_car")).append("、");
                    });
                    vehicleStr.deleteCharAt(vehicleStr.length() - 1);
                    map.put("carNumber", vehicleStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return AjaxResult.success(map);
            }
        }
    }

    @Override
    public AjaxResult operation(CheckRepairOrder checkRepairOrder) {
        Integer i = 0;
        try {
            i = anomalyInspectionMapper.operation(checkRepairOrder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (i >= 1)
                return AjaxResult.success();
            else
                return AjaxResult.error();
        }
    }

    @Override
    public AjaxResult suborder(List<CheckRepairOrder> list) {
        Integer i = 0;
        try {
            i = anomalyInspectionMapper.suborder(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (i >= 1)
                return AjaxResult.success();
            else
                return AjaxResult.error();
        }
    }

    @Override
    public List<CheckRepairOrder> export(String patrolStatus) {
        return anomalyInspectionMapper.export(patrolStatus);
    }

}

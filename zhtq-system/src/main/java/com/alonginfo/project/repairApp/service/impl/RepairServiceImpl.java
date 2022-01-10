package com.alonginfo.project.repairApp.service.impl;

import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.fileParse.mapper.FADataMapper;
import com.alonginfo.project.repairApp.entity.Orders;
import com.alonginfo.project.repairApp.mapper.RepairMapper;
import com.alonginfo.project.repairApp.service.RepairService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepairServiceImpl implements RepairService {
    @Resource
    RepairMapper repairMapper;

    @Resource
    FADataMapper faDataMapper;

    @Override
    public AjaxResult selectRepair(String sendTime) {
        return AjaxResult.success(repairMapper.selectRepair(sendTime));
    }

    @Override
    public AjaxResult selectCaptain(String station) {
        return AjaxResult.success(repairMapper.selectCaptain( station));
    }

    @Override
    public AjaxResult selectTeamInfo(String teamId) {
        List<Map<String, Object>> teammateList = repairMapper.selectTeammate(teamId);
        List<Map<String, Object>> vehicleList = repairMapper.selectVehicle(teamId);
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        StringBuffer teammate = new StringBuffer();
        StringBuffer vehicle = new StringBuffer();
        if (teammateList != null && teammateList.size() > 0) {
            teammateList.forEach(str -> {
                teammate.append(str.get("name")).append("、");
            });
            if (teammate.length() > 0) {
                teammate.deleteCharAt(teammate.length() - 1);
            }
        } else {
            teammate.append("暂无队员");
        }
        if (vehicleList != null && vehicleList.size() > 0) {
            vehicleList.forEach(str -> {
                vehicle.append(str.get("carNumber")).append("、");
            });
            if (vehicle.length() > 0) {
                vehicle.deleteCharAt(vehicle.length() - 1);
            }
        } else {
            vehicle.append("暂无车辆");
        }
        map.put("teammate", teammate);
        map.put("vehicle", vehicle);
        data.add(map);
        return AjaxResult.success(data);
    }

    @Transactional
    @Override
    public AjaxResult submit(Orders orders) {
        try {
            if ("1".equals(orders.getTable())) {
                try {
                    String FAULT_TYPE = "1";
                    Integer y = repairMapper.suborder(orders.getWorkNumber(), orders.getSuborder());
                    Integer x = repairMapper.submit(orders.getWorkNumber(), orders.getTeamId());
                    Integer z = faDataMapper.updateDms(orders.getEmergencyNumber(),FAULT_TYPE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Integer y = repairMapper.suborder2(orders.getWorkNumber(), orders.getSuborder());
                Integer x = repairMapper.submit2(orders.getWorkNumber(), orders.getTeamId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult updateState(Orders orders) {
        try {
            if(orders.getWorkStatus().equals("4")||orders.getWorkStatus().equals("5")){
                String FAULT_TYPE = "2";
                Integer z = faDataMapper.updateDms(orders.getEmergencyNumber(),FAULT_TYPE);
            }
            repairMapper.updateState(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult selectOrders(String workNumber) {
        return AjaxResult.success(repairMapper.selectOrders(workNumber));
    }
}

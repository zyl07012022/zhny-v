package com.alonginfo.project.emergencyCommand.service.impl;

import com.alonginfo.common.utils.ResultUtil;
import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.aspectj.lang.annotation.DataSource;
import com.alonginfo.framework.aspectj.lang.enums.DataSourceType;
import com.alonginfo.framework.config.MxConfig;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.emergencyCommand.entity.RelevanceCourts;
import com.alonginfo.project.emergencyCommand.entity.RelevanceUser;
import com.alonginfo.project.emergencyCommand.entity.RepairWorkOrder;
import com.alonginfo.project.emergencyCommand.entity.Suborder;
import com.alonginfo.project.emergencyCommand.mapper.RepairWorkOrderMapper;
import com.alonginfo.project.emergencyCommand.service.RepairWorkOrderService;
import com.alonginfo.project.fileParse.mapper.FADataMapper;
import com.alonginfo.project.inspectionWorkOrder.entity.Inspection;
import com.alonginfo.project.inspectionWorkOrder.service.impl.IInspectionServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RepairWorkOrderServiceImpl implements RepairWorkOrderService {
    @Resource
    private RepairWorkOrderMapper repairWorkOrderMapper;

    @Resource
    private FADataMapper faDataMapper;

    @Override
    public AjaxResult queryPage(String pageNum, String pageSize, RepairWorkOrder repairWorkOrder) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = repairWorkOrderMapper.queryPage(repairWorkOrder);
        return AjaxResult.success(new PageInfo(list));
    }

    @Override
    public AjaxResult queryUser(String pageNum, String pageSize, String workNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = repairWorkOrderMapper.queryUser(workNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    @Override
    public AjaxResult queryCourts(String pageNum, String pageSize, String workNumber) {
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Map<String, String>> list = repairWorkOrderMapper.queryCourts(workNumber);
        return AjaxResult.success(new PageInfo(list));
    }

    @Override
    public AjaxResult queryDispatch(String teamId) {
        if (StringUtil.isEmpty(teamId)) {
            return AjaxResult.success();
        }
        Map<String, Object> map = new HashMap<>();
        StringBuffer nameStr = new StringBuffer(); //队长名称
        StringBuffer phoneStr = new StringBuffer(); //手机号
        StringBuffer teamStr = new StringBuffer(); //队员
        StringBuffer vehicleStr = new StringBuffer(); //车牌号
        try {
            List<Map<String, String>> captainList = repairWorkOrderMapper.queryTeamInfo(teamId, "24");
            List<Map<String, String>> teamList = repairWorkOrderMapper.queryTeamInfo(teamId, "-1");
            List<Map<String, String>> vehicleList = repairWorkOrderMapper.queryVehicle(teamId);
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

    @Override
    public AjaxResult operation(RepairWorkOrder repairWorkOrder) {
        Integer i = 0;
        try {
            i = repairWorkOrderMapper.operation(repairWorkOrder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (i >= 1) {
                if (StringUtils.isNotEmpty(repairWorkOrder.getEmergencyNumber())) {
                    String emergency_number = repairWorkOrder.getEmergencyNumber();
                    String FAULT_TYPE = repairWorkOrder.getFaultType();
                    faDataMapper.updateDms(emergency_number, FAULT_TYPE);
                }
                return AjaxResult.success();
            } else {
                return AjaxResult.error();
            }
        }
    }

    @Override
    public AjaxResult suborder(List<Suborder> list) {
        Integer i = 0;
        try {
            i = repairWorkOrderMapper.suborder(list);
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
    public void save(RepairWorkOrder repairWorkOrder) {
        String str1 = UUID.randomUUID().toString().replace("-", "");
        repairWorkOrder.setWorkNumber("QX" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        repairWorkOrder.setWorkStatus("0");
        if (StringUtils.isNotEmpty(repairWorkOrder.getUserId()))
            for (int i = 0; i < repairWorkOrder.getUserId().size(); i++) {
                String str = UUID.randomUUID().toString().replace("-", "");
                RelevanceUser relevanceUser = new RelevanceUser();
                relevanceUser.setUserId(repairWorkOrder.getUserId().get(i));
                relevanceUser.setWorkNumber(repairWorkOrder.getWorkNumber());
                repairWorkOrderMapper.addUser(relevanceUser);
            }
        if (StringUtils.isNotEmpty(repairWorkOrder.getCourtsNumber()))
            for (int i = 0; i < repairWorkOrder.getCourtsNumber().size(); i++) {
                String str = UUID.randomUUID().toString().replace("-", "");
                RelevanceCourts relevanceCourts = new RelevanceCourts();
                relevanceCourts.setCourtsNumber(repairWorkOrder.getCourtsNumber().get(i));
                relevanceCourts.setWorkNumber(repairWorkOrder.getWorkNumber());
                repairWorkOrderMapper.addCourts(relevanceCourts);
            }
        repairWorkOrderMapper.save(repairWorkOrder);
    }

    @Override
    public List<RepairWorkOrder> export(Map map, HttpServletRequest request) {
        List<RepairWorkOrder> list = repairWorkOrderMapper.selectExcelData(map);
        return list;
    }

}

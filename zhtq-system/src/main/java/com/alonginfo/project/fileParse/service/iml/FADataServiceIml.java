package com.alonginfo.project.fileParse.service.iml;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.DeviceFaultInfo;
import com.alonginfo.project.emergencyCommand.entity.RelevanceCourts;
import com.alonginfo.project.emergencyCommand.entity.RelevanceUser;
import com.alonginfo.project.emergencyCommand.entity.RepairWorkOrder;
import com.alonginfo.project.emergencyCommand.mapper.RepairWorkOrderMapper;
import com.alonginfo.project.fileParse.mapper.FADataMapper;
import com.alonginfo.project.fileParse.service.FADataService;
import com.alonginfo.utils.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljg
 * @date 2021/3/4
 */
@Slf4j
@Service
public class FADataServiceIml implements FADataService {

    @Resource
    private FADataMapper faDataMapper;

    @Resource
    private RepairWorkOrderMapper repairWorkOrderMapper;

    @Override
    public void addFileInfo(Map<String, String> map) {
        try {
            faDataMapper.addFileInfo(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---------ts_file_info数据插入成功--------------");
        }
    }

    @Override
    public List<Map<String, Object>> getDMSData(String status,String deviceId) {
       String table1 = "ts_file_info";
       String table2 = "ts_device_info";
        if (status.equals("1")){
            List<Map<String, Object>> list = faDataMapper.getDMSData(table1,deviceId);
            return list;
        }else {
            List<Map<String, Object>> list = faDataMapper.getDMSData(table2,deviceId);
            return list;
        }
    }

    @Override
    public void addGisOrder(Map map) {
        String str =new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String str2 = "QX"+str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RepairWorkOrder repairWorkOrder = new RepairWorkOrder();
        repairWorkOrder.setWorkStatus("0");
        repairWorkOrder.setWorkNumber(str2);
        repairWorkOrder.setWorkName("DMS故障");//DMS推送抢修工单
        String code_name = StringUtils.valueOf(map.get("power_supply"));
        repairWorkOrder.setPowerSupply(faDataMapper.getCodeName(code_name));
        repairWorkOrder.setRepairsTime(sdf.format(new Date()));
        repairWorkOrder.setVoltageNode(StringUtils.valueOf(map.get("voltage_node")));
        repairWorkOrder.setPointSite(StringUtils.valueOf(map.get("pointSite")));
        code_name = StringUtils.valueOf(map.get("voltage_classes"));
        repairWorkOrder.setVoltageClasses(faDataMapper.getCodeName(code_name));
        code_name = StringUtils.valueOf(map.get("county"));
        repairWorkOrder.setCounty(faDataMapper.getCodeName(code_name));
        code_name = StringUtils.valueOf(map.get("fault_phenomenon"));
        repairWorkOrder.setFaultPhenomenon(faDataMapper.getCodeName(code_name));
        code_name = StringUtils.valueOf(map.get("source"));
        repairWorkOrder.setSource(faDataMapper.getCodeName(code_name));
        repairWorkOrder.setEmergencyNumber(StringUtils.valueOf(map.get("DEVICE_ID")));
        try {
            String emergency_number = repairWorkOrder.getEmergencyNumber();
            String FAULT_TYPE = "1";
            Integer count = faDataMapper.updateDms(emergency_number,FAULT_TYPE);
            if(count>0) {
                if (StringUtils.isNotEmpty(repairWorkOrder.getUserId()))
                    for (int i = 0; i < repairWorkOrder.getUserId().size(); i++) {
                        RelevanceUser relevanceUser = new RelevanceUser();
                        relevanceUser.setUserId(repairWorkOrder.getUserId().get(i));
                        relevanceUser.setWorkNumber(repairWorkOrder.getWorkNumber());
                        repairWorkOrderMapper.addUser(relevanceUser);
                    }
                if (StringUtils.isNotEmpty(repairWorkOrder.getCourtsNumber()))
                    for (int i = 0; i < repairWorkOrder.getCourtsNumber().size(); i++) {
                        RelevanceCourts relevanceCourts = new RelevanceCourts();
                        relevanceCourts.setCourtsNumber(repairWorkOrder.getCourtsNumber().get(i));
                        relevanceCourts.setWorkNumber(repairWorkOrder.getWorkNumber());
                        repairWorkOrderMapper.addCourts(relevanceCourts);
                    }
                repairWorkOrderMapper.save(repairWorkOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("故障数据导入功能")
    @Override
    public AjaxResult importExcelData(List<DeviceFaultInfo> faultInfoList) throws IOException {
        try {
            Map<String,List<DeviceFaultInfo>> faultMap = new HashMap<>();
            faultMap.put("items",faultInfoList);
            faDataMapper.addFaultInfo(faultMap);
        } catch (Exception e) {
            return AjaxResult.error("数据导入失败！");
        } finally {
            System.out.println("---------ts_fault_info数据插入成功--------------");
        }
        return AjaxResult.success("数据导入成功");
    }

    @Override
    public void addDeviceInfo(Map<String, String> map2) {
        try {
            faDataMapper.addDeviceInfo(map2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---------ts_file_info数据插入成功--------------");
        }
    }

}

package com.alonginfo.project.inspectionWorkOrder.service.impl;

import com.alonginfo.common.utils.ResultUtil;
import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.config.MxConfig;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.component.entities.HeavyLoad;
import com.alonginfo.project.component.entities.LowVoltage;
import com.alonginfo.project.component.entities.NotBalance;
import com.alonginfo.project.emergencyCommand.entity.RelevanceCourts;
import com.alonginfo.project.emergencyCommand.entity.RelevanceUser;
import com.alonginfo.project.emergencyCommand.mapper.RepairWorkOrderMapper;
import com.alonginfo.project.inspectionWorkOrder.entity.CheckRepairOrder;
import com.alonginfo.project.inspectionWorkOrder.entity.Inspection;
import com.alonginfo.project.inspectionWorkOrder.mapper.InspectionMapper;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class IInspectionServiceImpl<InspectionService> implements com.alonginfo.project.inspectionWorkOrder.service.InspectionService {

    @Resource
    private InspectionMapper inspectionMapper;

    @Resource
    RepairWorkOrderMapper repairWorkOrderMapper;

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public List<Map<String, Object>> getInspection(CheckRepairOrder map) {
        return inspectionMapper.getInspection(map);
    }

    @Override
    public List<Map<String, String>> queryUser(String patrolSchemeNumber) {
        return inspectionMapper.queryUser(patrolSchemeNumber);
    }

    @Override
    public List<Map<String, String>> queryCourts(String patrolSchemeNumber) {
        return inspectionMapper.queryCourts(patrolSchemeNumber);
    }

    @Override
    public List<Map<String, String>> queryDispatch(Map map) {
        return inspectionMapper.queryDispatch(map);
    }

    @Override
    public List<Map<String, String>> queryDispatchOther(String patrolSchemeNumber) {
        return inspectionMapper.queryDispatchOther(patrolSchemeNumber);
    }

    @Override
    public void planAdd(CheckRepairOrder checkRepairOrder) {
        String str = UUID.randomUUID().toString().replace("-", "");
        if ("1".equals(checkRepairOrder.getPatrolType())) {
            checkRepairOrder.setPatrolSchemeNumber("JX" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        } else {
            checkRepairOrder.setPatrolSchemeNumber("YX" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        }

        checkRepairOrder.setPatrolData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            if (StringUtils.isNotEmpty(checkRepairOrder.getUserIds()))
                for (int i = 0; i < checkRepairOrder.getUserIds().size(); i++) {
                    String str1 = UUID.randomUUID().toString().replace("-", "");
                    RelevanceUser relevanceUser = new RelevanceUser();
                    relevanceUser.setUserId(checkRepairOrder.getUserIds().get(i).toString());
                    relevanceUser.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
                    repairWorkOrderMapper.addUser(relevanceUser);
                }
            if (StringUtils.isNotEmpty(checkRepairOrder.getCourtsNumber()))
                for (int i = 0; i < checkRepairOrder.getCourtsNumber().size(); i++) {
                    String str1 = UUID.randomUUID().toString().replace("-", "");
                    RelevanceCourts relevanceCourts = new RelevanceCourts();
                    relevanceCourts.setCourtsNumber(checkRepairOrder.getCourtsNumber().get(i));
                    relevanceCourts.setWorkNumber(checkRepairOrder.getPatrolSchemeNumber());
                    repairWorkOrderMapper.addCourts(relevanceCourts);
                }
            if (StringUtils.isNotEmpty(checkRepairOrder.getPatrolAbnormal())) {
                if ((checkRepairOrder.getPatrolAbnormal()).equals("39")) {
                    LowVoltage lowVoltage = new LowVoltage();
                    lowVoltage.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                    inspectionMapper.addVoltageDetail(lowVoltage);
                } else if ((checkRepairOrder.getPatrolAbnormal()).equals("40")) {
                    NotBalance notBalance = new NotBalance();
                    notBalance.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                    inspectionMapper.addTrigonalImbalance(notBalance);
                } else if ((checkRepairOrder.getPatrolAbnormal()).equals("41")) {
                    HeavyLoad heavyLoad = new HeavyLoad();
                    heavyLoad.setPatrolSchemeNumber(checkRepairOrder.getPatrolSchemeNumber());
                    inspectionMapper.addOverloadDetail(heavyLoad);
                }
            }
            checkRepairOrder.setPatrolStatus("0");
            checkRepairOrder.setPointSite("115.77807441,25.057235564");
            inspectionMapper.planAdd(checkRepairOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void planChange(CheckRepairOrder checkRepairOrder) {
        inspectionMapper.planChange(checkRepairOrder);
    }

    @Override
    public void deleteBypatrolSchemeNumber(String patrolSchemeNumber) {
        inspectionMapper.deleteBypatrolSchemeNumber(patrolSchemeNumber);
    }

    @Override
    public void operate(String patrolSchemeNumber) {
        String patrolNumber = "";
        String patrolSendData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        inspectionMapper.operate(patrolNumber, patrolSchemeNumber, patrolSendData);
    }

    @Override
    public void archived(String patrolSchemeNumber) {
        inspectionMapper.archived(patrolSchemeNumber);
    }

    @Override
    public Map<String, Object> export(String patrolStatus, HttpServletRequest request) {
        List<Inspection> list = inspectionMapper.export(patrolStatus);
        String fileName = new Date().getTime() + ".xls";
//        String targetFilePath = request.getSession().getServletContext()
//                .getRealPath("download")
//                + File.separator + fileName;
        String targetFilePath =  MxConfig.getDownloadPath() +fileName;
        File desc = new File(targetFilePath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        String sheet1 = "计划巡检工单导表";
        String[] title1 = {"序号", "分巡检计划名称", "巡检计划单号", "所属区域", "承担（所属）供电所", "所属变电站",
                "所属馈线", "巡检对象类型", "设备名称", "巡检反馈"};
        try {
            createInspectionDataExcel(targetFilePath, sheet1, title1, list);
        } catch (Exception localException) {
            return ResultUtil.returnMap(false, 0, "", null);
        }
        return ResultUtil.returnMap(true, 0, "", fileName);
    }

    private void createInspectionDataExcel(String targetFile, String sheet1, String[] title1, List<Inspection> list) {

        WritableWorkbook workbook = null;
        WritableSheet worksheet = null;
        Workbook wb = null;
        Label label = null;
        checkFile(targetFile);
        OutputStream os = null;
        try {
            os = new FileOutputStream(targetFile);
            workbook = Workbook.createWorkbook(os);
            worksheet = workbook.createSheet(sheet1, 0);
            for (int i = 0; i < title1.length; i++) {
                label = new Label(i, 0, title1[i]);
                worksheet.setColumnView(i, 20);
                worksheet.addCell(label);
            }
            int i = 0;
            List<Integer> ran1 = new ArrayList<>();
            List<Integer> ran2 = new ArrayList<>();
            ran1.add(1);
            int row1 = 1;
            for (Inspection res : list) {
                int height = 1;
                if (StringUtils.isNotEmpty(res.getPatrolObject())) {
                    height = res.getPatrolObject().split("//").length;
                }
                ran2.add(height);
                if (i > 0) {
                    row1 = ran1.get(i - 1) + ran2.get(i - 1);
                    ran1.add(row1);
                }
//                System.out.println(row1 + "--" + height);
                worksheet.addCell(new Label(0, row1, String.valueOf(i + 1)));//序号
                worksheet.mergeCells(0, row1, 0, row1+height-1);//合并单元格
                worksheet.addCell(new Label(1, row1, res.getPatrolSchemeName()));//计划巡检名称
                worksheet.mergeCells(1, row1, 1, row1+height-1);//合并单元格
                worksheet.addCell(new Label(2, row1, res.getPatrolSchemeNumber()));//计划巡检编号
                worksheet.mergeCells(2, row1, 2, row1+height-1);//合并单元格
                worksheet.addCell(new Label(3, row1, res.getCountyCompany()));//所属区域
                worksheet.mergeCells(3, row1, 3, row1+height-1);//合并单元格
                worksheet.addCell(new Label(4, row1, res.getPowerSupply()));//所属供电所
                worksheet.mergeCells(4, row1, 4, row1+height-1);//合并单元格
                worksheet.addCell(new Label(5, row1, res.getSubordinateSubstation()));//所属变电站
                worksheet.mergeCells(5, row1, 5, row1+height-1);//合并单元格
                worksheet.addCell(new Label(6, row1, res.getFeeder()));//所属馈线
                worksheet.mergeCells(6, row1, 6, row1+height-1);//合并单元格
                for (int j = 0; j < height; j++) {
                    String patrolTypes = StringUtils.valueOf(res.getPatrolTypes());
                    String patrolObject = StringUtils.valueOf(res.getPatrolObject());
                    String feedback = StringUtils.valueOf(res.getFeedback());
                    if(StringUtils.isEmpty(patrolTypes.split("//"))){
                        worksheet.addCell(new Label(7, row1 + j, ""));//设备名称
                    }else{
                        worksheet.addCell(new Label(7, row1 + j, patrolTypes.split("//")[j]));//巡检对象
                    }
                   if(StringUtils.isEmpty( patrolObject.split("//")[j])){
                       worksheet.addCell(new Label(8, row1 + j, ""));//设备名称
                   }else{
                       worksheet.addCell(new Label(8, row1 + j, patrolObject.split("//")[j]));//设备名称
                   }
                  if(StringUtils.isEmpty(feedback.split("//")[j])){
                      worksheet.addCell(new Label(9, row1 + j, ""));//巡检反馈
                  }else{
                      worksheet.addCell(new Label(9, row1 + j, feedback.split("//")[j]));//巡检反馈
                  }
                }
                i++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("计划巡检工单导表失败");
        }

    }

    public static void checkFile(String target) {
        int index = target.lastIndexOf("/");
        if (index <= 0) {
            index = target.lastIndexOf("\\");
        }
        String path = target.substring(0, index);
        checkDir(path);
        File file = new File(target);
        if (file.exists()) {
            file.delete();
        }
    }

    private static void checkDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void main(String[] args) {
        String[] str = "".split("\\||");
        for (int i = 0; i <1 ; i++) {
            System.out.println(str[i]+"--");
        }
    }

    private static int equal(int start, int val, int n) {
        return (start + (n - 1) * val);
    }

}

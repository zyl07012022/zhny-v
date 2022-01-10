package com.alonginfo.project.fileParse.controller;

import com.alonginfo.common.utils.ServletUtils;
import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.security.LoginUser;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.DeviceFaultInfo;
import com.alonginfo.project.emergencyCommand.service.RepairWorkOrderService;
import com.alonginfo.project.fileParse.service.FADataService;
import com.alonginfo.project.system.domain.SysUser;
import com.alonginfo.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljg
 * @date 2021/2/22
 */
@RestController
@RequestMapping("/sy")
@Api(tags = "首页GIS故障信息")
public class SyController {

    @Autowired
    private RepairWorkOrderService repairWorkOrderService;

    @Autowired
    private FADataService faDataService;

    @ApiOperation("前往代派工单列表")
    @PostMapping("/addGisOrder")
    public AjaxResult addGisOrder(@RequestBody Map map2) {
        HashMap<String,Object> map = new HashMap<>();
        map = (HashMap<String, Object>)map2;
        faDataService.addGisOrder(map);
        return AjaxResult.success();
    }

    @ApiOperation("DMS故障推送fa文件解析")
    @PostMapping("/getDMSData")
    public AjaxResult getDMSData(@RequestBody Map map2){
        HashMap map = new HashMap();
        map = (HashMap) map2;
        String status = StringUtils.valueOf(map.get("status"));
        String deviceId = StringUtils.valueOf(map.get("deviceId"));
        List<Map<String,Object>> list = faDataService.getDMSData(status,deviceId);
        return AjaxResult.success(list);
    }

    @ApiOperation("故障数据导入功能")
    @PostMapping("/importExcelData")
    public AjaxResult importExcelData(MultipartFile file) throws Exception {
        ExcelUtil<DeviceFaultInfo> util = new ExcelUtil<DeviceFaultInfo>(DeviceFaultInfo.class);
        List<DeviceFaultInfo> faultInfoList = util.importExcel(file.getInputStream());
        return faDataService.importExcelData(faultInfoList);
    }
}

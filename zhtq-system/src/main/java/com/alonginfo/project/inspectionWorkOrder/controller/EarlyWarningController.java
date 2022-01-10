package com.alonginfo.project.inspectionWorkOrder.controller;

import com.alonginfo.common.utils.StringUtils;
import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.inspectionWorkOrder.service.EarlyWarningService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/earlyWarning")
public class EarlyWarningController extends BaseController {
    @Resource
    private EarlyWarningService earlyWarningService;

    /**
     * 查询供电公司、供电所
     */
    @ApiOperation("查询供电公司、供电所")
    @PostMapping("/selectCompany")
    public AjaxResult selectCompany() {
        AjaxResult ajaxResult = earlyWarningService.selectCompany();
        return ajaxResult;
    }

    /**
     * @param --      itemType 区域类型  --02市 --03供电局  --04供电所
     * @param --orgId 区域id
     * @return
     */
    @ApiOperation("查询预警信息")
    @PostMapping("/selectEarlyWarning")
    public AjaxResult selectEarlyWarning(@RequestBody Map map) {
        String itemType = StringUtils.valueOf(map.get("itemType"));
        String orgId = StringUtils.valueOf(map.get("orgId"));
        AjaxResult ajaxResult = null;
        String orgName = "";
        if (StringUtils.isNotEmpty(orgId)) {
            orgName = earlyWarningService.queryOrgName(orgId);
        }
        if ("03".equals(itemType)) {
            ajaxResult = earlyWarningService.selectEarlyWarning(itemType, "");
        } else {
            ajaxResult = earlyWarningService.selectEarlyWarning(itemType, orgName);
        }
        Map<String, Object> data = (Map<String, Object>) ajaxResult.get("data");
        data.put("pbzsl", earlyWarningService.selectNormalTr(itemType, orgId));
        data.put("pbjcsl", earlyWarningService.selectNormalTr(itemType, orgId));
        return AjaxResult.success(data);
    }

    /**
     * 生成巡检单号
     */
    @ApiOperation("生成巡检单号")
    @PostMapping("/createWO")
    public AjaxResult createWO(@RequestBody List<HashMap<String, Object>> list) {
        AjaxResult ajaxResult = earlyWarningService.createWO(list);
        return ajaxResult;
    }

    /**
     * 异常巡检工单查询
     */
    @ApiOperation("巡检信息")
    @PostMapping("/selectAbnormalList")
    public AjaxResult selectAbnormalList(@RequestBody Map map) {
        HashMap map2 = new HashMap();
        map2 = (HashMap) map;
        String orgName = "";
        if (StringUtils.isNotEmpty(StringUtils.valueOf(map.get("orgId")))) {
            orgName = earlyWarningService.queryOrgName(StringUtils.valueOf(map.get("orgId")));
        }
        String itemType = earlyWarningService.queryOrgName(StringUtils.valueOf(map.get("itemType")));
        String patrolAbnormal = StringUtils.valueOf(map2.get("patrolAbnormal"));
        AjaxResult ajaxResult = earlyWarningService.selectAbnormalList(itemType, orgName, patrolAbnormal);
        return ajaxResult;
    }
}

package com.alonginfo.project.alarmOrder.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alonginfo.framework.web.domain.AjaxResult;
import com.alonginfo.project.alarmOrder.entity.ByqInfo;
import com.alonginfo.project.alarmOrder.service.AlarmOrderService;
import com.alonginfo.project.alarmOrder.service.ByqInfoService;
import com.alonginfo.project.alarmOrder.service.TrWaringDataService;
import com.alonginfo.project.alarmOrder.vo.Params;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 崔亚魁
 * 预警工单
 */
@RestController
@RequestMapping("/alarmOrder")
@CrossOrigin
public class AlarmController {
    @Autowired
    private AlarmOrderService alarmOrderService;

    @Autowired
    private TrWaringDataService trWaringDataService;

    @Autowired
    private ByqInfoService byqInfoService;

    @PostMapping("/calculate")
    public AjaxResult find(@RequestBody Params params){

        return AjaxResult.success(alarmOrderService.calculate(params));
    }

    @GetMapping("/task")
    public AjaxResult task(){
        trWaringDataService.tranformerData();
        return AjaxResult.success("变压器计算成功");
    }


    /**
     * 进度条刷新，数据从session当中取
     *
     * @param request
     * @return String
     */
    @ResponseBody
    @RequestMapping(params = "method=flushProgress")
    public String flushProgress(HttpServletRequest request) {
        HashMap<String, Object> map = null;
        try {
            map = new HashMap<String, Object>();
            Object percent = request.getSession().getAttribute("percent");
            //加上空判断是为了防止第一次读取session时，出现NULL
            if (null == percent || "".equals(percent)) {
                map.put("percent", 0);
            } else {
                map.put("percent", percent);
                request.getSession().removeAttribute("percent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(map).toString();
    }

    /**
     * 修改变压器信息
     * @param map
     * @return
     */
    @PostMapping("/bygInfo")
    public AjaxResult updateBygInfo(@RequestBody Map<String,Object> map){
        return byqInfoService.updateBygInfo(map);
    }

    /**
     * 添加变压器信息
     * @param map
     * @return
     */
    @PostMapping("/insertbygInfo")
    public AjaxResult insertBygInfo(@RequestBody Map<String,Object> map) {
         byqInfoService.insertBygInfo(map);
         return AjaxResult.success();
    }

    /**
     * 删除变压器信息
     * @param map
     * @return
     */
    @DeleteMapping("/removeBygInfo")
    public AjaxResult removeBygInfo(@RequestBody Map<String,Object> map) {
        return byqInfoService.removeBygInfo(map);
    }
}

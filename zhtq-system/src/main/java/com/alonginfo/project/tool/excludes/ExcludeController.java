package com.alonginfo.project.tool.excludes;

import com.alonginfo.framework.web.controller.BaseController;
import com.alonginfo.framework.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试以 mxDatav_开头的url不需要权限进行访问
 */

@RestController
@RequestMapping("/mxDatav_test/notice")
public class ExcludeController extends BaseController {

    @GetMapping(value = "/list")
    public AjaxResult getInfo() {
        return AjaxResult.success("请求验证成功！");
    }
}

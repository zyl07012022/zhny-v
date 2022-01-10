package com.alonginfo.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ljg
 * @Date 2021/7/28 10:47
 */
public class ResultUtil {
    public static Map<String, Object> returnMap(boolean isSuc, int total, String message, Object rows)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", Boolean.valueOf(isSuc));
        result.put("total", Integer.valueOf(total));
        result.put("message", message);
        result.put("value", rows);

        return result;
    }
}

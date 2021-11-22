package com.example.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import com.example.common.pojo.response.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author demo
 * @description
 */
public class ResponseUtil {

    /**
     * 响应异常，直接向前端写response，用于异常处理器捕获不到时手动抛出
     */
    public static void responseExceptionError(HttpServletResponse response,
                                              Integer code,
                                              String message,
                                              String exceptionClazz) throws IOException {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(ContentType.JSON.toString());
        ErrorResult<String> errorResult = new ErrorResult<>(code, message);
        errorResult.setExceptionClazz(exceptionClazz);
        String errorResultJsonData = new ObjectMapper().writeValueAsString(errorResult);
        response.getWriter().write(errorResultJsonData);
    }

    /**
     * 响应异常，向前端返回ErrorResponseData的json数据，用于全局异常处理器
     */
    public static ErrorResult<String> responseDataError(Integer code, String message, String exceptionClazz) {
        ErrorResult<String> errorResponseData = new ErrorResult<>(code, message);
        errorResponseData.setExceptionClazz(exceptionClazz);
        return errorResponseData;
    }

}

package com.example.auth.core.error;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.example.common.consts.AopSortConstant;
import com.example.common.consts.SymbolConstant;
import com.example.common.context.requestNo.RequestNoContext;
import com.example.common.exception.AuthException;
import com.example.common.exception.ServiceException;
import com.example.common.exception.enums.ParamExceptionEnum;
import com.example.common.exception.enums.RequestMethodExceptionEnum;
import com.example.common.exception.enums.RequestTypeExceptionEnum;
import com.example.common.exception.enums.ServerExceptionEnum;
import com.example.common.exception.enums.abs.AbstractBaseExceptionEnum;
import com.example.common.pojo.response.ErrorResult;
import com.example.core.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author demo
 * @description
 */
@ControllerAdvice
@Order(AopSortConstant.GLOBAL_EXP_HANDLER_AOP)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ErrorResult<String> missParamException(MissingServletRequestParameterException e) {
        logger.error(">>> 请求参数异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        String parameterType = e.getParameterType();
        String parameterName = e.getParameterName();
        String message = StrUtil.format(">>> 缺少请求的参数{}，类型为{}", parameterName, parameterType);
        return renderJson(500, message);
    }

    /**
     * 拦截参数格式传递异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorResult<String> httpMessageNotReadable(HttpMessageNotReadableException e) {
        logger.error(">>> 参数格式传递异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return renderJson(RequestTypeExceptionEnum.REQUEST_JSON_ERROR);
    }

    /**
     * 拦截不支持媒体类型异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ErrorResult<String> httpMediaTypeNotSupport(HttpMediaTypeNotSupportedException e) {
        logger.error(">>> 参数格式传递异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return renderJson(RequestTypeExceptionEnum.REQUEST_TYPE_IS_JSON);
    }

    /**
     * 拦截请求方法的异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ErrorResult<String> methodNotSupport(HttpServletRequest request) {
        if (ServletUtil.isPostMethod(request)) {
            logger.error(">>> 请求方法异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), RequestMethodExceptionEnum.REQUEST_METHOD_IS_GET.getMessage());
            return renderJson(RequestMethodExceptionEnum.REQUEST_METHOD_IS_GET);
        }
        if (ServletUtil.isGetMethod(request)) {
            logger.error(">>> 请求方法异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), RequestMethodExceptionEnum.REQUEST_METHOD_IS_POST.getMessage());
            return renderJson(RequestMethodExceptionEnum.REQUEST_METHOD_IS_POST);
        }
        return null;
    }


    /**
     * 拦截参数校验错误异常,JSON传参
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResult<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String argNotValidMessage = getArgNotValidMessage(e.getBindingResult());
        logger.error(">>> 参数校验错误异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), argNotValidMessage);
        return renderJson(ParamExceptionEnum.PARAM_ERROR.getCode(), argNotValidMessage);
    }

    /**
     * 拦截参数校验错误异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ErrorResult<String> paramError(BindException e) {
        String argNotValidMessage = getArgNotValidMessage(e.getBindingResult());
        logger.error(">>> 参数校验错误异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), argNotValidMessage);
        return renderJson(ParamExceptionEnum.PARAM_ERROR.getCode(), argNotValidMessage);
    }

    /**
     * 拦截认证失败异常
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ErrorResult<String> authFail(AuthException e) {
        logger.error(">>> 认证异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return renderJson(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ErrorResult<String> businessError(ServiceException e) {
        logger.error(">>> 业务异常，请求号为：{}，具体信息为：{}", RequestNoContext.get(), e.getMessage());
        return renderJson(e.getCode(), e.getErrorMessage(), e);
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult<String> serverError(Throwable e) {
        logger.error(">>> 服务器运行异常，请求号为：{}", RequestNoContext.get());
        e.printStackTrace();
        return renderJson(e);
    }

    /**
     * 渲染异常json
     */
    private ErrorResult<String> renderJson(Integer code, String message) {
        return renderJson(code, message, null);
    }

    /**
     * 渲染异常json
     */
    private ErrorResult<String> renderJson(AbstractBaseExceptionEnum baseExceptionEnum) {
        return renderJson(baseExceptionEnum.getCode(), baseExceptionEnum.getMessage(), null);
    }

    /**
     * 渲染异常json
     */
    private ErrorResult<String> renderJson(Throwable throwable) {
        return renderJson(((AbstractBaseExceptionEnum) ServerExceptionEnum.SERVER_ERROR).getCode(),
                ((AbstractBaseExceptionEnum) ServerExceptionEnum.SERVER_ERROR).getMessage(), throwable);
    }

    /**
     * 渲染异常json
     * <p>
     * 根据异常枚举和Throwable异常响应，异常信息响应堆栈第一行
     */
    private ErrorResult<String> renderJson(Integer code, String message, Throwable e) {
        if (ObjectUtil.isNotNull(e)) {

            //获取所有堆栈信息
            StackTraceElement[] stackTraceElements = e.getStackTrace();

            //默认的异常类全路径为第一条异常堆栈信息的
            String exceptionClassTotalName = stackTraceElements[0].toString();

            return ResponseUtil.responseDataError(code, message, exceptionClassTotalName);
        } else {
            return ResponseUtil.responseDataError(code, message, null);
        }
    }

    /**
     * 获取请求参数不正确的提示信息
     * <p>
     * 多个信息，拼接成用逗号分隔的形式
     */
    private String getArgNotValidMessage(BindingResult bindingResult) {
        if (bindingResult == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        //多个错误用逗号分隔
        List<ObjectError> allErrorInfos = bindingResult.getAllErrors();
        for (ObjectError error : allErrorInfos) {
            stringBuilder.append(SymbolConstant.COMMA).append(error.getDefaultMessage());
        }

        //最终把首部的逗号去掉
        return StrUtil.removePrefix(stringBuilder.toString(), SymbolConstant.COMMA);
    }
}

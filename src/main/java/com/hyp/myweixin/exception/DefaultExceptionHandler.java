/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.hyp.myweixin.exception;

import com.alibaba.fastjson.JSON;
import com.hyp.myweixin.pojo.vo.result.MyError;
import com.hyp.myweixin.pojo.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 *
 * @author langhsu
 */
@Slf4j
@Component
public class DefaultExceptionHandler implements HandlerExceptionResolver {
    private static final String errorView = "/error";

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {

        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException || ex instanceof MyDefinitionException) {
            log.error("全局异常处理，自定义异常处理：" + ex.getMessage());
        } else {
            log.error("异常通用处理：" + ex.getMessage(), ex);
        }

        ModelAndView view = null;
        String ret = ex.getMessage();

        if (isAjax(handler, request)) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSON.toJSONString(Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, ret)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            view = new ModelAndView();
        } else {
            log.info("页面错误");
            Map<String, Object> map = new HashMap<String, Object>(1);
            MyError myError = new MyError(500, ret, request.getContextPath());
            map.put("myError", myError);
            view = new ModelAndView(errorView, map);
        }
        return view;
    }

    /**
     * 判断是否 ajax 调用
     *
     * @param handler
     * @return
     */
    private boolean isAjax(Object handler, HttpServletRequest request) {
        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class);
            if (responseBodyAnn == null) {
                return StringUtils.isNotEmpty(request.getHeader("X-Requested-With"));
            } else {
                return responseBodyAnn != null;
            }
        }

        return false;
    }

}

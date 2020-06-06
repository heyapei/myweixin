package com.hyp.myweixin.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
@Slf4j
public class HttpAspect {


    //@Pointcut("execution(public * com.hyp.myweixin.controller.*.*(..))")
    @Pointcut("execution(* com.hyp.myweixin.controller..*.*(..))")
    private void pointcut() {
    }


    @Before("pointcut()")
    public void around(JoinPoint joinPoint) {
        //记录http请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //从request中获取http请求的url/请求的方法类型／响应该http请求的类方法／IP地址／请求中的参数
        //得到session对象
        HttpSession session = request.getSession(false);
        //取出请求用户
        //ip
        String logString = "访问IP:{" + request.getRemoteAddr() + "}" + ",请求地址:{" + request.getRequestURI() + "}" + ",HTTP方法:{" + request.getMethod() + "}" + ",控制层:{" + joinPoint.getSignature().getDeclaringTypeName() + "}" + ",请求服务:{" + joinPoint.getSignature().getName() + "}";

      /*  //url
        log.info("请求地址[URL] = {" + request.getRequestURI() + "}");
        //method
        log.info("HTTP方法[method] = {" + request.getMethod() + "}");
        //控制层
        log.info("控制层[class] = {" + joinPoint.getSignature().getDeclaringTypeName() + "}");
        //方法
        log.info("请求服务[method] = {" + joinPoint.getSignature().getName() + "}");*/

        String ua = request.getHeader("User-Agent").toLowerCase();
        String deviceType = this.check(ua) ? "mobile" : "pc";
        String deviceName = this.getDeviceName(ua);
        logString = logString + "，请求设备类型：" + deviceType + "，请求设备名：" + deviceName;
        //获取请求参数
        try {
            String reqBody = this.getReqBody();
            if (reqBody == null || reqBody.length() <= 0) {
                reqBody += "{";
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    reqBody += "\""+entry.getKey() + "\":";
                    for (String s : entry.getValue()) {
                        reqBody += s + ";";
                    }
                }
                reqBody += "}";
            }
            logString = logString + ",请求参数:" + reqBody;
        } catch (Exception ex) {
            log.error("get Request Error: " + ex.getMessage());
        }
        log.info(logString);
    }


    /**
     * \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
     * 字符串在编译时会被转码一次,所以是 "\\b"
     * \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
     */
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry" + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]" + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    /**
     *
     */
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    /**
     * 移动设备正则匹配：手机端、平板
     */
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否是移动设备访问
     *
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     * @Title: check
     * @Date : 2014-7-7 下午01:29:07
     */
    protected boolean check(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find() || matcherTable.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设备名称
     *
     * @param userAgent
     * @return
     */
    protected String getDeviceName(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find()) {
            return matcherPhone.group();
        } else if (matcherTable.find()) {
            return matcherTable.group();
        } else {
            return "pc";
        }
    }


    /**
     * 后置通知，切点后执行
     *
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(Object ret) {
        try {
            log.info("响应结果RESPONSE: " + JSON.toJSONString(ret));
        } catch (Exception ex) {
            log.error("get Response Error: " + ex.getMessage());
        }
    }


    private final String REQUEST_GET = "GET";

    private final String REQUEST_POST = "POST";

    /**
     * 返回调用参数
     *
     * @return ReqBody
     */
    private String getReqBody() {
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = this.getHttpServletRequest();
        //获取请求方法GET/POST
        String method = request.getMethod();
        Optional.ofNullable(method).orElse("UNKNOWN");
        if (REQUEST_POST.equals(method)) {
            return this.getPostReqBody(request);
        } else if (REQUEST_GET.equals(method)) {
            return this.getGetReqBody(request);
        }
        return "get Request Parameter Error";
    }

    /**
     * 获取request
     * Spring对一些（如RequestContextHolder、TransactionSynchronizationManager、LocaleContextHolder等）中非线程安全状态的bean采用ThreadLocal进行处理
     * 让它们也成为线程安全的状态
     *
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

    /**
     * 获取GET请求数据
     *
     * @param request
     * @return
     */
    private String getGetReqBody(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> parameterMap = new HashMap<>(16);
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        return parameterMap.toString();
    }

    /**
     * 获取POST请求数据
     *
     * @param request
     * @return 返回POST参数
     */
    private String getPostReqBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("get Post Request Parameter err : " + e.getMessage());
        }
        return stringBuilder.toString();
    }

}
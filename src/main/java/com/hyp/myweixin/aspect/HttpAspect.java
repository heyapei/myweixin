package com.hyp.myweixin.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
        //参数
        Object[] objects = joinPoint.getArgs();
        // 参数名
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (objects.length > 0) {
            logString = logString + ",参数[";
        }
        for (int i = 0; i < objects.length; i++) {
            //log.info("参数[" + argNames[i] + "] = {" + objects[i].toString() + "}");
            logString = logString + argNames[i] + "= {" + objects[i].toString() + "}";
        }
        if (objects.length > 0) {
            logString = logString + "]";
        }
        //获取请求参数
        try {
            String reqBody = this.getReqBody();
            logString = logString +",请求参数:" + reqBody;
        } catch (Exception ex) {
            log.error("get Request Error: " + ex.getMessage());
        }
        log.info(logString);
    }

    //@Before 在方法执行之前执行
    /*@Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {

    }*/

    //    //@After在方法执行之后执行
   /* @After("pointcut()")
    public void doAfter() {
        System.out.println("doAfter");
    }*/

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
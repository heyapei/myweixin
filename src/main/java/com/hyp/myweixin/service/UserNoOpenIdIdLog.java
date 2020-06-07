package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinUserOptionLog;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:20
 * @Description: TODO
 */
public interface UserNoOpenIdIdLog {

    /**
     * 添加微信用户操作日志
     *
     * @param weixinUserOptionLog
     * @param httpServletRequest
     * @return 主键ID
     */
    Integer addUserOperationLog(WeixinUserOptionLog weixinUserOptionLog, HttpServletRequest httpServletRequest);


}

package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.mapper.WeixinUserOptionLogMapper;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.dto.AmapIpToAddressDTO;
import com.hyp.myweixin.pojo.modal.WeixinUserOptionLog;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.service.UserNoOpenIdIdLog;
import com.hyp.myweixin.utils.MyIpMacUtil;
import com.hyp.myweixin.utils.amaputil.AmapApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 14:31
 * @Description: TODO
 */
@Service
@Slf4j
public class UserNoOpenIdIdLogImpl implements UserNoOpenIdIdLog {


    @Autowired
    private AmapApiUtil amapApiUtil;
    @Autowired
    private MyIpMacUtil myIpMacUtil;

    @Autowired
    private WeixinUserOptionLogMapper weixinUserOptionLogMapper;

    @Autowired
    private WeixinVoteBaseMapper weixinVoteBaseMapper;

    /**
     * 添加微信用户操作日志
     *
     * @param weixinUserOptionLog
     * @param httpServletRequest
     * @return 主键ID
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public Integer addUserOperationLog(WeixinUserOptionLog weixinUserOptionLog,
                                       HttpServletRequest httpServletRequest) {

        String userAgent = httpServletRequest.getHeader("User-Agent");
        String realIP = myIpMacUtil.getRealIP(httpServletRequest);
        if (StringUtils.isBlank(realIP)) {
            log.error("该服务类要求必须传递IP地址");
            return 0;
        }

        if (StringUtils.isNotBlank(weixinUserOptionLog.getOptionObject())) {
            WeixinVoteBase weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(Integer.parseInt(weixinUserOptionLog.getOptionObject()));
            //log.info("查询活动数据：" + weixinVoteBase.toString());
            if (weixinVoteBase == null) {
                log.error("未能查询到用户浏览的活动数据，浏览的活动ID为：{}", weixinUserOptionLog.getOptionObject());
                return 0;
            }
        }


        AmapIpToAddressDTO ipPositionNoAsync = amapApiUtil.getIpPositionNoAsync(realIP);
        if (ipPositionNoAsync != null) {
            String province = ipPositionNoAsync.getProvince();
            if (province != null) {
                weixinUserOptionLog.setProvince(province);
            }
            String city = ipPositionNoAsync.getCity();
            if (city != null) {
                weixinUserOptionLog.setCity(city);
            }
        }

        String mobileName = myIpMacUtil.getMobileName(userAgent);
        if (StringUtils.isNotBlank(mobileName)) {
            weixinUserOptionLog.setDeviceName(mobileName);
        }
        String mobileType = myIpMacUtil.getMobileType(userAgent);
        if (StringUtils.isNotBlank(mobileType)) {
            weixinUserOptionLog.setDeviceType(mobileType);
        }

        if (ipPositionNoAsync != null && ipPositionNoAsync.getRectangle() != null) {
            String geocodeByIpAddressGeneral = amapApiUtil.getGeocodeByIpAddressGeneralNoAsync(ipPositionNoAsync.getRectangle());
            if (StringUtils.isNotBlank(geocodeByIpAddressGeneral)) {
                weixinUserOptionLog.setGeneralAddress(geocodeByIpAddressGeneral);
            }
        }

        weixinUserOptionLog.setIp(myIpMacUtil.ipToLong(realIP));

        try {
            weixinUserOptionLogMapper.insertUseGeneratedKeys(weixinUserOptionLog);
            return (Integer) weixinUserOptionLog.getId();
        } catch (Exception e) {
            log.error("添加微信用户操作日志出现失败，原因{}", e.toString());
        }
        return 0;
    }
}

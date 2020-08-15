package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateFirstQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateSecondQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:51
 * @Description: TODO
 */
@Service
@Slf4j
public class QubaomingActiveCreateServiceImpl implements QubaomingActiveCreateService {

    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;
    @Autowired
    private WechatCompanyService wechatCompanyService;

    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;
    @Autowired
    private QubaomingActiveConfigService qubaomingActiveConfigService;

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;


    /**
     * 创建第二页的信息 该页内容为配置信息
     *
     * @param activeCreateSecondQuery
     * @return 配置表的主键信息
     * @throws MyDefinitionException
     */
    @Override
    public Integer createActiveSecond(ActiveCreateSecondQuery activeCreateSecondQuery) throws MyDefinitionException {
        if (activeCreateSecondQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveConfig qubaomingActiveConfig = null;
        try {
            qubaomingActiveConfig = MyEntityUtil.entity2VM(activeCreateSecondQuery, QubaomingActiveConfig.class);
        } catch (Exception e) {
            log.error("配置数据转换错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("配置数据转换错误");
        }

        if (qubaomingActiveConfig == null) {
            log.error("配置数据转换错误，错误原因：{}", "转换失败了数据为空");
            throw new MyDefinitionException("配置数据转换了但是数据为空");
        } else {
            try {
                qubaomingActiveConfig = (QubaomingActiveConfig) MyEntityUtil.entitySetDefaultValue(qubaomingActiveConfig);
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
        }

        try {
            List<QubaomingActiveConfig> qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeCreateSecondQuery.getActiveId());
            if (!qubaomingActiveConfigs.isEmpty()) {
                throw new MyDefinitionException("无法为一个活动创建多个配置信息");
            }
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        Integer integer = null;
        try {
            integer = qubaomingActiveConfigService.insertReturnPk(qubaomingActiveConfig);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }

    /**
     * 创建第一页的信息
     *
     * @param activeCreateFirstQuery
     * @return 0失败 1成功
     * @throws MyDefinitionException
     */
    @Override
    public Integer createActiveFirst(ActiveCreateFirstQuery activeCreateFirstQuery) throws MyDefinitionException {

        if (activeCreateFirstQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        try {
            qubaomingWeixinUserService.validateUserRight(activeCreateFirstQuery.getUserId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeCreateFirstQuery.getActiveId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("没有发现指定的活动");
        }

        String accessTokenByAppName = null;
        try {
            accessTokenByAppName = weixinSmallContentDetectionApiService.getAccessTokenByAppName("qubaoming");
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*文本验证接口*/
        Boolean aBoolean = false;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(activeCreateFirstQuery.getText(), accessTokenByAppName);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (!aBoolean) {
            throw new MyDefinitionException("当前文字违规重新填写");
        }


        qubaomingActiveBase.setUpdateTime(System.currentTimeMillis());
        if (activeCreateFirstQuery.getType().equalsIgnoreCase("title")) {
            qubaomingActiveBase.setActiveName(activeCreateFirstQuery.getText());
            qubaomingActiveBase.setActiveImg(activeCreateFirstQuery.getImg());
            return qubaomingActiveBaseService.updateActiveNameAndImg(qubaomingActiveBase);
        } else if (activeCreateFirstQuery.getType().equalsIgnoreCase("desc")) {
            qubaomingActiveBase.setActiveDesc(activeCreateFirstQuery.getText());
            qubaomingActiveBase.setActiveDescImg(activeCreateFirstQuery.getImg());
            return qubaomingActiveBaseService.updateActiveDescAndImg(qubaomingActiveBase);
        } else if (activeCreateFirstQuery.getType().equalsIgnoreCase("detail")) {
            qubaomingActiveBase.setActiveDetail(activeCreateFirstQuery.getText());
            qubaomingActiveBase.setActiveDetailImg(activeCreateFirstQuery.getImg());
            return qubaomingActiveBaseService.updateActiveDetailAndImg(qubaomingActiveBase);
        } else {
            throw new MyDefinitionException("当前指定的数据类型不在设计范围类，请联系管理员");
        }

    }


    /**
     * 创建活动时 预检查
     *
     * @param activeUserId 用户ID
     * @return 检查结果
     * @throws MyDefinitionException
     */
    @Override
    public ValidateUnCompleteByActiveUserIdVO validateUnCompleteByActiveUserId(Integer activeUserId) throws MyDefinitionException {
        if (activeUserId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        try {
            qubaomingWeixinUserService.validateUserRight(activeUserId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        Integer userUnCompleteActiveId = null;
        try {
            userUnCompleteActiveId = qubaomingActiveBaseService.getUserUnCompleteActiveId(activeUserId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        List<WechatCompany> wechatCompanies = null;
        try {
            wechatCompanies = wechatCompanyService.selectListByUserId(activeUserId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*返回视图值*/
        ValidateUnCompleteByActiveUserIdVO validateUnCompleteByActiveUserIdVO = new ValidateUnCompleteByActiveUserIdVO();
        validateUnCompleteByActiveUserIdVO.setActiveId(userUnCompleteActiveId);
        validateUnCompleteByActiveUserIdVO.setUserId(activeUserId);
        if (wechatCompanies.isEmpty()) {
            validateUnCompleteByActiveUserIdVO.setHasCompanyInfo(false);
        } else {
            validateUnCompleteByActiveUserIdVO.setHasCompanyInfo(true);
        }
        return validateUnCompleteByActiveUserIdVO;
    }
}

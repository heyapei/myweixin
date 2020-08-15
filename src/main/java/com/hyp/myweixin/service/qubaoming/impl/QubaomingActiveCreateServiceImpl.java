package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateFirstQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveBaseService;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveCreateService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
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
    private WeixinVoteUserService weixinVoteUserService;


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
            weixinVoteUserService.validateUserRight(activeCreateFirstQuery.getUserId());
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

        qubaomingActiveBase.setUpdateTime(System.currentTimeMillis());
        if (activeCreateFirstQuery.getType().equalsIgnoreCase("title")) {
            qubaomingActiveBase.setActiveName(activeCreateFirstQuery.getText());
            qubaomingActiveBase.setActiveImg(activeCreateFirstQuery.getImg());
            return qubaomingActiveBaseService.updateActiveNameAndImg(qubaomingActiveBase);
        } else if (activeCreateFirstQuery.getType().equalsIgnoreCase("desc")) {
            qubaomingActiveBase.setActiveDesc(activeCreateFirstQuery.getText());
            qubaomingActiveBase.setActiveDescImg(activeCreateFirstQuery.getImg());
            return qubaomingActiveBaseService.updateActiveDescAndImg(qubaomingActiveBase);
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

        WeixinVoteUser userById = weixinVoteUserService.getUserById(activeUserId);
        if (userById == null) {
            throw new MyDefinitionException("没有找到当前用户信息，请重新进行授权操作");
        } else if (userById.getEnable().equals(WeixinVoteUser.ENABLEENUM.UN_ENABLE.getCode())) {
            throw new MyDefinitionException("当前用户已被禁用");
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

package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingUserSignUp;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerIndexQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveManagerIndexVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveSignUpShowVO;
import com.hyp.myweixin.service.AdministratorsOptionService;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.utils.MyEnumUtil;
import com.hyp.myweixin.utils.MySeparatorUtil;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 9:35
 * @Description: TODO
 */
@Service
@Slf4j
public class ActiveManagerServiceImpl implements ActiveManagerService {

    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;
    @Autowired
    private QuBaoMingActiveConfigService qubaomingActiveConfigService;
    @Autowired
    private QubaomingUserSignUpService qubaomingUserSignUpService;
    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;

    /**
     * 获取活动管理页面的信息
     *
     * @param activeManagerIndexQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public ActiveManagerIndexVO getActiveManagerIndexVOByActiveId(ActiveManagerIndexQuery activeManagerIndexQuery) throws MyDefinitionException {
        if (activeManagerIndexQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeManagerIndexQuery.getActiveId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能找到指定的活动数据");
        }
        if (!administratorsOptionService.isQuBaoMingSuperAdministrators(activeManagerIndexQuery.getUserId())) {
            if (!qubaomingActiveBase.getActiveUserId().equals(activeManagerIndexQuery.getUserId())) {
                throw new MyDefinitionException("不是当前活动管理员无法查看报名数据");
            }
        }

        ActiveManagerIndexVO activeManagerIndexVO = new ActiveManagerIndexVO();
        activeManagerIndexVO.setActiveId(qubaomingActiveBase.getId());
        activeManagerIndexVO.setActiveName(qubaomingActiveBase.getActiveName());
        activeManagerIndexVO.setCreateTime(MyDateUtil.numberDateFormat(String.valueOf(qubaomingActiveBase.getCreateTime()), "yyyy-MM-dd HH:mm"));
        activeManagerIndexVO.setActiveImgList(qubaomingActiveBase.getActiveImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
        activeManagerIndexVO.setActiveViewNum(qubaomingActiveBase.getActiveViewNum());
        activeManagerIndexVO.setActiveStatus(MyEnumUtil.getByIntegerTypeCode(QubaomingActiveBase.ActiveStatusEnum.class, "getCode", qubaomingActiveBase.getActiveStatus()).getMsg());

        QubaomingActiveConfig qubaomingActiveConfig = qubaomingActiveConfigService.selectOneByActiveId(activeManagerIndexQuery.getActiveId());
        if (qubaomingActiveConfig.getActiveEndTime() < System.currentTimeMillis()) {
            if (!MyEnumUtil.getByIntegerTypeCode(QubaomingActiveBase.ActiveStatusEnum.class,
                    "getCode", qubaomingActiveBase.getActiveStatus()).getCode().equals(QubaomingActiveBase.ActiveStatusEnum.END.getCode())) {
                qubaomingActiveBase.setActiveStatus(QubaomingActiveBase.ActiveStatusEnum.END.getCode());
                qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
            }
        }
        return activeManagerIndexVO;
    }

    /**
     * 分页查询参与数据
     *
     * @param activeManagerByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getAllSignUpByPageQuery(ActiveManagerByPageQuery activeManagerByPageQuery) throws MyDefinitionException {
        if (activeManagerByPageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeManagerByPageQuery.getActiveId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能找到指定的活动数据");
        }
        if (!administratorsOptionService.isQuBaoMingSuperAdministrators(activeManagerByPageQuery.getUserId())) {
            if (!qubaomingActiveBase.getActiveUserId().equals(activeManagerByPageQuery.getUserId())) {
                throw new MyDefinitionException("不是当前活动管理员无法查看报名数据");
            }
        }
        QubaomingActiveConfig qubaomingActiveConfig = null;
        try {
            qubaomingActiveConfig = qubaomingActiveConfigService.selectOneByActiveId(activeManagerByPageQuery.getActiveId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveConfig == null) {
            throw new MyDefinitionException("未能查找到活动配置数据");
        }

        String activeRequireOptionList[] = null;
        String activeRequireOption = qubaomingActiveConfig.getActiveRequireOption();
        if (activeManagerByPageQuery != null) {
            activeRequireOptionList = activeRequireOption.split(MySeparatorUtil.SEMICOLON_SEPARATOR);
        }
        if (activeManagerByPageQuery == null) {
            throw new MyDefinitionException("未能发现活动配置的有必填数据");
        }

        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeManagerByPageQuery.getActiveId());
        example.orderBy("createTime").desc();
        PageHelper.startPage(activeManagerByPageQuery.getPageNum(), activeManagerByPageQuery.getPageSize());
        PageInfo pageInfo = null;
        List<QubaomingUserSignUp> qubaomingUserSignUpList = null;
        try {
            qubaomingUserSignUpList = qubaomingUserSignUpService.selectQubaomingUserSignUpByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        pageInfo = new PageInfo(qubaomingUserSignUpList);

        List<ActiveSignUpShowVO> activeSignUpVOList = getActiveSignUpVO(activeRequireOptionList, qubaomingUserSignUpList);
        pageInfo.setList(activeSignUpVOList);
        return pageInfo;
    }

    @Override
    public List<ActiveSignUpShowVO> getActiveSignUpVO(String[] activeRequireOptionList, List<QubaomingUserSignUp> qubaomingUserSignUpList) throws MyDefinitionException {
        List<ActiveSignUpShowVO> activeSignUpVOList = new ArrayList<>();
        for (QubaomingUserSignUp qubaomingUserSignUp : qubaomingUserSignUpList) {
            String[] signUpInfoList = null;
            String signUpInfo = qubaomingUserSignUp.getSignUpInfo();
            if (signUpInfo != null) {
                signUpInfoList = signUpInfo.split(MySeparatorUtil.SEMICOLON_SEPARATOR);
            }
            if (signUpInfoList == null) {
                continue;
            }
            ActiveSignUpShowVO activeSignUpVO = ActiveSignUpShowVO.init();
            activeSignUpVO.setCreateTime(String.valueOf(qubaomingUserSignUp.getCreateTime()));
            QubaomingWeixinUser qubaomingWeixinUser = null;
            try {
                qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingUserSignUp.getUserId());
                activeSignUpVO.setAvatar(qubaomingWeixinUser.getAvatarUrl());
            } catch (MyDefinitionException e) {

            }
            for (int i = 0; i < activeRequireOptionList.length; i++) {
                if (activeRequireOptionList[i].equalsIgnoreCase("1")) {
                    activeSignUpVO.setName(signUpInfoList[i]);
                } else if (activeRequireOptionList[i].equalsIgnoreCase("2")) {
                    activeSignUpVO.setAge(signUpInfoList[i]);
                } else if (activeRequireOptionList[i].equalsIgnoreCase("3")) {
                    activeSignUpVO.setPhone(signUpInfoList[i]);
                } else if (activeRequireOptionList[i].equalsIgnoreCase("4")) {
                    activeSignUpVO.setGender(signUpInfoList[i]);
                }
            }
            activeSignUpVOList.add(activeSignUpVO);
        }
        return activeSignUpVOList;
    }
}

package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveBaseService;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveUserCollectionService;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.hyp.myweixin.service.qubaoming.UserEnrollActiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:42
 * @Description: TODO
 */
@Service
@Slf4j
public class UserEnrollActiveServiceImpl implements UserEnrollActiveService {


    @Autowired
    private QubaomingActiveUserCollectionService qubaomingActiveUserCollectionService;
    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;

    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;

    /**
     * 用户收藏活动
     * 1. 删除收藏
     * 2. 减少收藏数
     *
     * @param userId
     * @param activeId
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer UserUnEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        Integer integer = null;
        try {
            integer = qubaomingActiveUserCollectionService.deleteQubaomingActiveUserCollectionByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        if (integer == null || integer <= 0) {
            throw new MyDefinitionException("未能找到您收藏的活动数据");
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能发现指定的活动");
        }

        /*更新收藏数*/
        qubaomingActiveBase.setActiveCollectionNum(qubaomingActiveBase.getActiveCollectionNum() - 1);
        try {
            qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }

    /**
     * 用户收藏活动
     * 1. 判断用户权限
     * 2. 判断活动是否存在
     * 3. 添加收藏
     * 4. 添加收藏数
     *
     * @param userId
     * @param activeId
     * @return 用户收藏活动的主键信息
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer UserEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }


        try {
            qubaomingWeixinUserService.validateUserRight(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        /*如果已经有收藏了 不允许多次收藏*/
        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        QubaomingActiveUserCollection qubaomingActiveUserCollection1 = qubaomingActiveUserCollectionService.selectOneQubaomingActiveUserCollectionByExample(example);
        if (qubaomingActiveUserCollection1 != null) {
            throw new MyDefinitionException("您已经收藏了该活动不允许重复收藏");
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能发现指定的活动");
        }


        QubaomingActiveUserCollection qubaomingActiveUserCollection = QubaomingActiveUserCollection.init();
        qubaomingActiveUserCollection.setUserId(userId);
        qubaomingActiveUserCollection.setActiveId(activeId);

        /*添加收藏*/
        Integer integer = null;
        try {
            integer = qubaomingActiveUserCollectionService.insertReturnPk(qubaomingActiveUserCollection);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*更新收藏数*/
        qubaomingActiveBase.setActiveCollectionNum(qubaomingActiveBase.getActiveCollectionNum() + 1);
        try {
            qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        return integer;
    }
}

package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.service.AdministratorsOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/23 19:41
 * @Description: TODO
 */
@Slf4j
@Service
@PropertySource("classpath:administrators.properties")
public class AdministratorsOptionServiceImpl implements AdministratorsOptionService {

    @Value("#{'${administrators.super.user.ids}'.split(';')}")
    private Integer[] superUserIds;

    @Value("#{'${qubaoming.administrators.super.user.ids}'.split(';')}")
    private Integer[] quBaoMingSuperUserIds;


    /**
     * 判断是否为趣报名的超级管理员
     *
     * @param userId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public boolean isQuBaoMingSuperAdministrators(Integer userId) throws MyDefinitionException {
        if (userId == null) {
            throw new MyDefinitionException("用户ID为空，判断失败");
        }
        for (Integer superUserId : quBaoMingSuperUserIds) {
            if (superUserId.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过userId判断这个userId的用户是不是超级管理员
     *
     * @param userId 用户ID
     * @return 是否为超级管理员
     * @throws MyDefinitionException
     */
    @Override
    public boolean isSuperAdministrators(Integer userId) throws MyDefinitionException {

        if (userId == null) {
            throw new MyDefinitionException("用户ID为空，判断失败");
        }
        for (Integer superUserId : superUserIds) {
            if (superUserId.equals(userId)) {
                return true;
            }
        }
        return false;
    }
}

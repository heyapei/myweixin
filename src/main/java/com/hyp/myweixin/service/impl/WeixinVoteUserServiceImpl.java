package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteUserMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.service.WeixinVoteUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/5 19:28
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteUserServiceImpl implements WeixinVoteUserService {


    @Autowired
    private WeixinVoteUserMapper weixinVoteUserMapper;

    /**
     * 添加用户信息
     *
     * @param weixinVoteUser 微信用户信息
     * @return
     */
    @Override
    public int addWechatInfo(WeixinVoteUser weixinVoteUser) {
        try {
            weixinVoteUserMapper.insertUseGeneratedKeys(weixinVoteUser);
        } catch (Exception e) {
            log.info(e.toString());
            throw new MyDefinitionException("保存微信用户数错误");
        }

        return weixinVoteUser.getId();
    }

    /**
     * 通过openId获取用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public WeixinVoteUser getUserByOpenId(String openId) {
        WeixinVoteUser weixinVoteUser = null;
        try {
            Example example = new Example(WeixinVoteUser.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("openId", openId);
            List<WeixinVoteUser> weixinVoteUsers = weixinVoteUserMapper.selectByExample(example);
            if (weixinVoteUsers != null && weixinVoteUsers.size() > 0) {
                weixinVoteUser = weixinVoteUsers.get(0);
            }
        } catch (Exception e) {
            log.info(e.toString());
            throw new MyDefinitionException("通过openId查询用户数据失败");
        }
        return weixinVoteUser;
    }
}

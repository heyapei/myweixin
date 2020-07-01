package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteOrganisersMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import com.hyp.myweixin.service.WeixinVoteOrganisersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 19:01
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteOrganisersServiceImpl implements WeixinVoteOrganisersService {
    @Autowired
    private WeixinVoteOrganisersMapper weixinVoteOrganisersMapper;


    /**
     * 保存活动公司信息
     *
     * @param weixinVoteOrganisers
     * @return
     */
    @Override
    public Integer saveWeixinVoteOrganisers(WeixinVoteOrganisers weixinVoteOrganisers) {
        int i = 0;
        try {
            i = weixinVoteOrganisersMapper.insertSelective(weixinVoteOrganisers);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存活动主办方信息错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存活动主办方信息错误");
        }
        return i;
    }
}

package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:59
 * @Description: TODO
 */
public interface WeixinVoteOrganisersService {

    /**
     * 保存活动公司信息
     *
     * @param weixinVoteOrganisers
     * @return
     */
    Integer saveWeixinVoteOrganisers(WeixinVoteOrganisers weixinVoteOrganisers);
}

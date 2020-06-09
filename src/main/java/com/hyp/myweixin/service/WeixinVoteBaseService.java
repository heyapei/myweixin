package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.IndexWorksVO;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 20:09
 * @Description: TODO
 */
public interface WeixinVoteBaseService {

    /**
     * 分页查询投票活动列表
     *
     * @param weixinVoteBase
     * @param pageInfo
     * @return
     */
    PageInfo getVoteWorkByPage(WeixinVoteBase weixinVoteBase, PageInfo pageInfo);


    /**
     * 通过活动的ID查询活动的详情
     * @param workId 活动ID
     * @return
     */
    IndexWorksVO getVoteWorkByWorkId(Integer workId);


}
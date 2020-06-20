package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteWorkSimpleVO;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 18:57
 * @Description: TODO
 */
public interface WeixinVoteUserWorkService {




    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @return
     */
    int addUserVote(WeixinVoteUserWork weixinVoteUserWork);





    /**
     * 查询活动下面的作品
     *
     * @param activeId
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkByWorkId(Integer activeId);


    /**
     * 获取用户作品投票人的信息
     *
     * @param weixinVoteUserWork
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteUserWorkByPage(WeixinVoteUserWork weixinVoteUserWork, PageInfo pageInfo);


    /**
     * 获取投票人的头像信息
     *
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteUserWorkSimpleVOByPageInfo(PageInfo pageInfo);

}

package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareActiveVO;
import com.hyp.myweixin.service.ShareUserWorkOrActiveService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/29 22:38
 * @Description: TODO
 */
@Slf4j
@Service
public class ShareUserWorkOrActiveServiceImpl implements ShareUserWorkOrActiveService {


    private static final String SEMICOLON_SEPARATOR = ";";

    @Autowired
    private WeixinVoteConfService weixinVoteConfService;

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;

    /**
     * 通过活动ID获得分享用数据
     *
     * @param activeId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public ShareActiveVO getShareActiveVOByActiveId(Integer activeId) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("获得活动分享信息要求必须指定活动ID");
        }

        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);

        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("没有找到指定的活动数据");
        }

        WeixinVoteConf weixinVoteConfByVoteWorkId = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteConfByVoteWorkId == null) {
            throw new MyDefinitionException("没有找到指定的活动配置数据");
        }

        ShareActiveVO shareActiveVO = new ShareActiveVO();
        shareActiveVO.setActiveId(activeId);
        shareActiveVO.setActiveName(weixinVoteBaseByWorkId.getActiveName());
        shareActiveVO.setActiveImg(weixinVoteBaseByWorkId.getActiveImg().replaceAll(SEMICOLON_SEPARATOR, ""));
        shareActiveVO.setActiveShareImg(weixinVoteConfByVoteWorkId.getActiveConfShareImg().replaceAll(SEMICOLON_SEPARATOR, ""));


        return shareActiveVO;
    }
}

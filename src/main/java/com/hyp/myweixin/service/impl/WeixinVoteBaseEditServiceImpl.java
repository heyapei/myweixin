package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;
import com.hyp.myweixin.service.WeixinVoteBaseEditService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:54
 * @Description: TODO
 */
@Slf4j
@Service
public class WeixinVoteBaseEditServiceImpl implements WeixinVoteBaseEditService {

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;

    private static final String SEMICOLON_SEPARATOR = ";";


    /**
     * 通过活动ID查询活动第一页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第一页需要回显的数据
     * @throws MyDefinitionException
     */
    @Override
    public ActiveEditFirstVO getActiveEditFirstVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }


        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                throw new MyDefinitionException("您不是该活动的管理员");
            }
        }

        ActiveEditFirstVO activeEditFirstVO = new ActiveEditFirstVO();
        activeEditFirstVO.setUserId(userId);
        activeEditFirstVO.setActiveId(activeId);
        activeEditFirstVO.setActiveImg(weixinVoteBase.getActiveImg().replaceAll(SEMICOLON_SEPARATOR, ""));
        activeEditFirstVO.setActiveDesc(weixinVoteBase.getActiveDesc());
        if (weixinVoteBase.getActiveDescImg() != null && weixinVoteBase.getActiveDescImg().contains(SEMICOLON_SEPARATOR)) {
            activeEditFirstVO.setActiveDescImgS(weixinVoteBase.getActiveDescImg().split(SEMICOLON_SEPARATOR));
        } else {
            activeEditFirstVO.setActiveDescImgS(new String[0]);
        }

        activeEditFirstVO.setActiveName(weixinVoteBase.getActiveName());
        activeEditFirstVO.setActiveReward(weixinVoteBase.getActiveReward());
        String activeRewardImg = weixinVoteBase.getActiveRewardImg();
        if (activeRewardImg != null && activeRewardImg.contains(SEMICOLON_SEPARATOR)) {
            activeEditFirstVO.setActiveRewardImgS(activeRewardImg.split(SEMICOLON_SEPARATOR));
        } else {
            activeEditFirstVO.setActiveRewardImgS(new String[0]);
        }



        return activeEditFirstVO;
    }
}

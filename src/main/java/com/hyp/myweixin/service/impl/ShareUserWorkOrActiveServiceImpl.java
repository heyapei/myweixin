package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareActiveVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareUserWorkVO;
import com.hyp.myweixin.service.ShareUserWorkOrActiveService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteConfService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.dateutil.MyDateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
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

    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;


    /**
     * 通过作品ID获取作品的分享书
     *
     * @param userWorkId 作品ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public ShareUserWorkVO getShareUserWorkVOByUserWorkId(Integer userWorkId) throws MyDefinitionException {

        if (userWorkId == null) {
            throw new MyDefinitionException("获得作品分享信息要求必须指定作品ID");
        }

        VoteDetailCompleteVO weixinVoteWorkByUserWorkId = weixinVoteWorkService.getWeixinVoteWorkByUserWorkId(userWorkId);
        if (weixinVoteWorkByUserWorkId == null) {
            throw new MyDefinitionException("没有找到指定的作品");
        }

        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(weixinVoteWorkByUserWorkId.getActiveVoteBaseId());
        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("没有作品所属活动信息");
        }

        ShareUserWorkVO shareUserWorkVO = new ShareUserWorkVO();
        shareUserWorkVO.setActiveDesc(weixinVoteBaseByWorkId.getActiveDesc());
        shareUserWorkVO.setActiveId(weixinVoteBaseByWorkId.getId());
        shareUserWorkVO.setActiveName(weixinVoteBaseByWorkId.getActiveName());
        shareUserWorkVO.setUserWorkId(userWorkId);
        shareUserWorkVO.setUserWorkName(weixinVoteWorkByUserWorkId.getVoteWorkUserName());
        shareUserWorkVO.setUserWorkDesc(weixinVoteWorkByUserWorkId.getVoteWorkDesc());
        shareUserWorkVO.setActiveEndTimeFormat(MyDateUtil.DateToString(weixinVoteBaseByWorkId.getActiveEndTime(),MyDateStyle.YYYY_MM_DD_HH_MM));

        String[] voteWorkImgS = weixinVoteWorkByUserWorkId.getVoteWorkImgS();
        if (voteWorkImgS != null && voteWorkImgS.length > 0) {
            shareUserWorkVO.setUserWorkFirstImg(voteWorkImgS[0]);
        } else {
            shareUserWorkVO.setUserWorkFirstImg("");
        }
        shareUserWorkVO.setWeixinShareQrCode("/upload/share/share20200801210720.jpg");


        return shareUserWorkVO;
    }

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
        shareActiveVO.setActiveDesc(weixinVoteBaseByWorkId.getActiveDesc());
        shareActiveVO.setActiveEndTime(weixinVoteBaseByWorkId.getActiveEndTime());
        shareActiveVO.setActiveEndTimeFormat(MyDateUtil.DateToString(weixinVoteBaseByWorkId.getActiveEndTime(), MyDateStyle.YYYY_MM_DD_HH_MM));
        shareActiveVO.setWeixinShareQrCode("/upload/share/share20200801210720.jpg");


        return shareActiveVO;
    }
}

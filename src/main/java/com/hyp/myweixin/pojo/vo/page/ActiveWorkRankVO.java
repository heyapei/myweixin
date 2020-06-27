package com.hyp.myweixin.pojo.vo.page;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 19:32
 * @Description: TODO
 */
@Data
public class ActiveWorkRankVO {
    private WeixinVoteBase weixinVoteBase;
    private PageInfo voteWorkPageInfo;
}

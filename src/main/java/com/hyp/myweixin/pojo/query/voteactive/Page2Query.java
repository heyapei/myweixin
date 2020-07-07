package com.hyp.myweixin.pojo.query.voteactive;

import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/7 13:00
 * @Description: TODO
 */
@Data
public class Page2Query {

    @NotNull(message = "用户ID不可以为空")
    private Integer userId;

    @NotNull(message = "活动ID不可以为空")
    private Integer voteWorkId;

    @NotNull(message = "是否展示到首页不能为空")
    private Integer isShowIndex;

    @NotNull(message = "分享图不可以为空")
    private String shareImg;

    @NotNull(message = "是否填写了公司信息不可以为空")
    private Integer hasOrganisers;

    private WeixinVoteOrganisers weixinVoteOrganisers;

}

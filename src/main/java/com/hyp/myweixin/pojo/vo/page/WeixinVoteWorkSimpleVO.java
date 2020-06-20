package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/20 13:43
 * @Description: TODO 用户作品的简单信息
 */
@Data
public class WeixinVoteWorkSimpleVO {
    /**
     * 作品人姓名
     */
    private String voteWorkUserName;
    /**
     * 作品描述
     */
    private String voteWorkDesc;
    /**
     * 作品名称
     */
    private String voteWorkName;
    /**
     * 作品在活动中的序号
     */
    private Integer voteWorkOr;
    /**
     * 作品ID
     */
    private Integer id;
    /**
     * 作品被投票次数
     */
    private Integer voteWorkCountNum;
    /**
     * 作品被浏览次数
     */
    private Integer voteWorkCountViewNum;
}

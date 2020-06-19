package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author heyapei
 */

@Data
public class VoteDetailCompleteVO {

    private Integer id;
    private String voteWorkUserName;
    private String voteWorkUserPhone;
    private String voteWorkDesc;
    private String voteWorkName;
    private String voteWorkVideoUrl;
    private Date voteWorkCreateTime;
    private Integer activeVoteBaseId;
    /**
     * 投票数
     */
    private Integer voteWorkCountNum;
    private String[] voteWorkImgS;
    /**
     * 被查看总次数
     */
    private Integer voteWorkCountViewNum;
    /**
     * 当前排名
     */
    private Integer rankNum;
    /**
     * 作品用户头像
     */
    private String voteWorkUserAvatar;
    private Integer voteWorkOr;

}
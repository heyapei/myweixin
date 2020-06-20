package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/20 12:24
 * @Description: TODO
 */
@Data
public class WeixinVoteWorkCommentVO {

    private Integer likeCount;
    private Integer commentOr;
    private String workComment;
    private Date createTime;
    private String avatarUrl;
    private String nickName;

}

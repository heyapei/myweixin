package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/10 19:25
 * @Description: TODO
 */
@Data
public class VoteDetailByWorkIdVO {
    private String activeImg;
    private String activeName;
    private Integer activeJoinCount;
    private Integer activeVoteCount;
    private Integer activeViewCount;
    private Date activeStartTime;
    private Date activeEndTime;
    private String organisersName;
    private String organisersLogoImg;
    private String activeMusic;
    private String activeBgImg;


}

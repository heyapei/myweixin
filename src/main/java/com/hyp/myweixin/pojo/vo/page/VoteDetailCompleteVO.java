package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import javax.persistence.*;

/**
 * @author heyapei
 */

@Data
public class VoteDetailCompleteVO {

    private Integer id;
    private String voteWorkUserName ;
    private String voteWorkUserPhone;
    private String voteWorkDesc;
    private String voteWorkName;
    private String voteWorkVideoUrl;
    private Date voteWorkCreateTime;
    private Integer activeVoteBaseId;
    private Integer voteWorkCountNum;
    private String voteWorkImg;
    private Integer voteWorkCountViewNum;

}
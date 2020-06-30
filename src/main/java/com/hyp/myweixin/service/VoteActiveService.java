package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.vo.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:33
 * @Description: TODO
 */
public interface VoteActiveService {


    /**
     * 创建活动
     * @param weixinVoteWorkDTO
     * @return
     */
    Integer createVoteWork(WeixinVoteWorkDTO weixinVoteWorkDTO);


    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     */
    Result saveSingleRes(MultipartFile file, String type);

}

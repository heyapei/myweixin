package com.hyp.myweixin.mapper.qubaoming;

import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface QubaomingActiveBaseMapper extends MyMapper<QubaomingActiveBase> {


    /**
     * 更新活动描述和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     */
    Integer updateActiveDescAndImg(QubaomingActiveBase qubaomingActiveBase);


    /**
     * 更新活动标题和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     */
    Integer updateActiveNameAndImg(QubaomingActiveBase qubaomingActiveBase);

}
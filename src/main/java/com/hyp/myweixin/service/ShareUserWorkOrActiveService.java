package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareActiveVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/29 22:35
 * @Description: TODO
 */

public interface ShareUserWorkOrActiveService {

    /**
     * 通过活动ID获得分享用数据
     *
     * @param activeId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    ShareActiveVO getShareActiveVOByActiveId(Integer activeId) throws MyDefinitionException;


}

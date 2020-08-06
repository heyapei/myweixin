package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareActiveVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareUserWorkVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/29 22:35
 * @Description: TODO
 */

public interface ShareUserWorkOrActiveService {


    /**
     * 通过作品ID获取作品的分享书
     *
     * @param userWorkId 作品ID
     * @return
     * @throws MyDefinitionException
     */
    ShareUserWorkVO getShareUserWorkVOByUserWorkId(Integer userWorkId) throws MyDefinitionException;


    /**
     * 通过活动ID获得分享用数据
     *
     * @param activeId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    ShareActiveVO getShareActiveVOByActiveId(Integer activeId) throws MyDefinitionException;


}

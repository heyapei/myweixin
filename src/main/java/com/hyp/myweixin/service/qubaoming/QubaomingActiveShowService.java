package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveShowByCompanyIdQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowActiveByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveDetailShowVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveShareImgVO;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 14:25
 * @Description: TODO
 */
public interface QubaomingActiveShowService {

    /**
     * 通过公司id查询活动列表
     *
     * @param activeShowByCompanyIdQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getActiveListByCompanyId(ActiveShowByCompanyIdQuery activeShowByCompanyIdQuery) throws MyDefinitionException;


    /**
     * 增加分享量
     *
     * @param activeId
     * @return
     * @throws MyDefinitionException
     */
    Integer addActiveShareNumByActiveId(Integer activeId) throws MyDefinitionException;


    /**
     * 获取活动的分享图
     *
     * @param activeId
     * @return
     * @throws MyDefinitionException
     */
    ActiveShareImgVO getActiveShareImgByActiveId(Integer activeId) throws MyDefinitionException;


    /**
     * 通过activeId查询具体的活动详情
     * userId用于判断报名人数
     *
     * @param userId
     * @param activeId
     * @return 视图
     * @throws MyDefinitionException
     */
    ActiveDetailShowVO getActiveShowDetailByActiveId(Integer userId, Integer activeId) throws MyDefinitionException;

    /**
     * 分页查询热门活动信息
     *
     * @param showActiveByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getAllActiveByShowActiveByPageQuery(ShowActiveByPageQuery showActiveByPageQuery) throws MyDefinitionException;


    /**
     * 分页查询热门活动信息
     *
     * @param showActiveByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getHotActiveByShowActiveByPageQuery(ShowActiveByPageQuery showActiveByPageQuery) throws MyDefinitionException;


}

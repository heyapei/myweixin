package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowActiveByPageQuery;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 14:25
 * @Description: TODO
 */
public interface QubaomingActiveShowService {

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

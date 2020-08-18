package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowHotActiveByPageQuery;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveBaseService;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveShowService;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 14:27
 * @Description: TODO
 */
@Service
@Slf4j
public class QubaomingActiveShowServiceImpl implements QubaomingActiveShowService {


    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;


    /**
     * 分页查询热门活动信息
     *
     * @param showHotActiveByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getHotActiveByShowHotActiveByPageQuery(ShowHotActiveByPageQuery showHotActiveByPageQuery) throws MyDefinitionException {
        if (showHotActiveByPageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveBase.class);
        Example.Criteria criteria = example.createCriteria();
        /*近一个月内创建的项目*/
        criteria.andBetween("createTime", Long.parseLong(MyDateUtil.numberDateFormatToDate(MyDateUtil.addDay(new Date(), -30), 13)), System.currentTimeMillis());
        example.orderBy("activeShowOrder").desc();
        example.orderBy("activeJoinNum").desc();
        example.orderBy("activeShareNum").desc();
        example.orderBy("activeCollectionNum").desc();
        example.orderBy("activeViewNum").desc();

        PageHelper.startPage(showHotActiveByPageQuery.getPageNum(), showHotActiveByPageQuery.getPageSize());
        PageInfo pageInfo = null;
        try {
            List<QubaomingActiveBase> qubaomingActiveBases = qubaomingActiveBaseService.selectUserActiveByExample(example);
            pageInfo = new PageInfo(qubaomingActiveBases);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pageInfo;
    }
}

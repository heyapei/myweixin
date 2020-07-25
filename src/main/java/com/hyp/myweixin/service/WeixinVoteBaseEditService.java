package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditFourthQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditSecondQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditThirdQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFourthVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditSecondVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditThirdVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:52
 * @Description: TODO 活动编辑接口
 */
public interface WeixinVoteBaseEditService {


    /**
     * 根据以下数据进行更新活动第四页中信息操作
     * 1. 要求是管理员
     *
     * @param activeEditFourthQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    Integer editActiveEditFourthQuery(ActiveEditFourthQuery activeEditFourthQuery) throws MyDefinitionException;




    /**
     * 通过活动ID查询活动第四页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第四页需要回显的数据
     * @throws MyDefinitionException
     */
    ActiveEditFourthVO getActiveEditFourthVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;



    /**
     * 根据以下数据进行更新活动第三页中信息操作
     * 1. 要求是管理员
     *
     * @param activeEditThirdQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    Integer editActiveEditThirdQuery(ActiveEditThirdQuery activeEditThirdQuery) throws MyDefinitionException;



    /**
     * 通过活动ID查询活动第三页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第三页需要回显的数据
     * @throws MyDefinitionException
     */
    ActiveEditThirdVO getActiveEditThirdVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;


    /**
     * 根据以下数据进行更新活动第二页中信息操作
     * 1. 要求是管理员
     *
     * @param activeEditSecondQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    Integer editActiveEditSecondQuery(ActiveEditSecondQuery activeEditSecondQuery) throws MyDefinitionException;


    /**
     * 通过活动ID查询活动第二页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第二页需要回显的数据
     * @throws MyDefinitionException
     */
    ActiveEditSecondVO getActiveEditSecondVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;


    /**
     * 根据以下数据进行更新操作
     * 1. 要求是管理员
     * 2. 要求是创建完成的数据
     *
     * @param userId     用户ID
     * @param activeId   活动ID
     * @param type       上传的数据类型
     * @param activeText 上传的文本 非必须
     * @param activeImg  上传的图片 使用英文;拼接好的
     * @return
     */
    Integer editBaseVoteWorkSavePageAndImg(int userId, int activeId, String type, String activeText, String activeImg);


    /**
     * 通过活动ID查询活动第一页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第一页需要回显的数据
     * @throws MyDefinitionException
     */
    ActiveEditFirstVO getActiveEditFirstVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;


}

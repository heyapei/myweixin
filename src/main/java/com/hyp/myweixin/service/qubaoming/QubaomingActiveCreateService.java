package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateFirstQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateSecondQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateThirdQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.GetActiveFirstVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.GetActiveSecondVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.GetActiveThirdVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:47
 * @Description: TODO
 */
public interface QubaomingActiveCreateService {


    /**
     * 获取第三页面的信息
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第三页的信息
     * @throws MyDefinitionException
     */
    GetActiveThirdVO getActiveThirdByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;




    /**
     * 获取第二页面的信息
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第二页的信息
     * @throws MyDefinitionException
     */
    GetActiveSecondVO getActiveSecondByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;



    /**
     * 获取第一页面的信息
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第一页的信息
     * @throws MyDefinitionException
     */
    GetActiveFirstVO getActiveFirstByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;


    /**
     * 新增活动分享图
     *
     * @param activeId 活动ID
     * @param shareImg 分享图片
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer addActiveShareImg(Integer activeId, String shareImg) throws MyDefinitionException;


    /**
     * 创建第三页的信息 该页内容为公司信息配置
     * 该步骤需要更新活动基础表中的 公司ID
     *
     * @param activeCreateThirdQuery
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer createActiveThird(ActiveCreateThirdQuery activeCreateThirdQuery) throws MyDefinitionException;


    /**
     * 创建第二页的信息 该页内容为配置信息
     *
     * @param activeCreateSecondQuery
     * @return 配置表的主键信息
     * @throws MyDefinitionException
     */
    Integer createActiveSecond(ActiveCreateSecondQuery activeCreateSecondQuery) throws MyDefinitionException;


    /**
     * 创建第一页的信息
     *
     * @param activeCreateFirstQuery
     * @return 0失败 1成功
     * @throws MyDefinitionException
     */
    Integer createActiveFirst(ActiveCreateFirstQuery activeCreateFirstQuery) throws MyDefinitionException;


    /**
     * 创建活动时 预检查
     *
     * @param activeUserId 用户ID
     * @return 检查结果
     * @throws MyDefinitionException
     */
    ValidateUnCompleteByActiveUserIdVO validateUnCompleteByActiveUserId(Integer activeUserId) throws MyDefinitionException;


}

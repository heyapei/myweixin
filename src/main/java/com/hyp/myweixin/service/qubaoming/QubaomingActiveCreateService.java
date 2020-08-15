package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateFirstQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateSecondQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:47
 * @Description: TODO
 */
public interface QubaomingActiveCreateService {


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

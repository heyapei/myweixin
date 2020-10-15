package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingMessageSubmit;
import com.hyp.myweixin.pojo.qubaoming.query.message.UserSubmitMessageQuery;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/10/15 19:35
 * @Description: TODO
 */
public interface UserSubmitMessageService {


    /**
     * 添加用户订阅消息 请求预处理
     *
     * @param userSubmitMessageQuery 用户提交信息
     */
    void addQubaomingMessageSubmit(UserSubmitMessageQuery userSubmitMessageQuery);


    /**
     * 添加用户订阅消息
     *
     * @param userId    用户ID
     * @param activeId  活动ID
     * @param messageId 消息模板Id
     * @return
     * @throws MyDefinitionException
     */
    Integer addQubaomingMessageSubmit(Integer userId, Integer activeId, String messageId) throws MyDefinitionException;


    /**
     * 通过查询条件查询资源
     *
     * @param example 查询条件
     * @return 返回数据
     * @throws MyDefinitionException
     */
    List<QubaomingMessageSubmit> getQubaomingMessageSubmitByExample(Example example) throws MyDefinitionException;


    /**
     * 更新用户订阅消息
     *
     * @param qubaomingMessageSubmit 订阅信息
     * @return
     * @throws MyDefinitionException
     */
    Integer updateQubaomingMessageSubmit(QubaomingMessageSubmit qubaomingMessageSubmit) throws MyDefinitionException;






}

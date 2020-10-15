package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingMessageSubmitMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingMessageSubmit;
import com.hyp.myweixin.pojo.qubaoming.query.message.UserSubmitMessageQuery;
import com.hyp.myweixin.service.qubaoming.UserSubmitMessageService;
import com.hyp.myweixin.utils.MySeparatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/10/15 19:42
 * @Description: TODO
 */
@Service
@Slf4j
public class UserSubmitMessageServiceImpl implements UserSubmitMessageService {


    @Autowired
    private QubaomingMessageSubmitMapper qubaomingMessageSubmitMapper;


    /**
     * 添加用户订阅消息 请求预处理
     *
     * @param userSubmitMessageQuery 用户提交信息
     * @return
     */
    @Override
    public void addQubaomingMessageSubmit(UserSubmitMessageQuery userSubmitMessageQuery) {

        if (userSubmitMessageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        String messageId = userSubmitMessageQuery.getMessageId();
        if (StringUtils.isNotBlank(messageId)) {
            if (messageId.contains(MySeparatorUtil.SEMICOLON_SEPARATOR)) {
                for (String s : messageId.split(MySeparatorUtil.SEMICOLON_SEPARATOR)) {
                    try {
                        addQubaomingMessageSubmit(userSubmitMessageQuery.getUserId(), userSubmitMessageQuery.getActiveId()
                                , s);
                    } catch (MyDefinitionException e) {
                        log.error("添加用户订阅信息数据错误，错误原因：{}",e.toString());
                    }
                }
            } else {
                try {
                    addQubaomingMessageSubmit(userSubmitMessageQuery.getUserId(), userSubmitMessageQuery.getActiveId()
                            , messageId);
                } catch (MyDefinitionException e) {
                    log.error("添加用户订阅信息数据错误，错误原因：{}",e.toString());
                }
            }
        }
    }

    /**
     * 添加用户订阅消息
     *
     * @param userId    用户ID
     * @param activeId  活动ID
     * @param messageId 消息模板Id
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer addQubaomingMessageSubmit(Integer userId, Integer activeId, String messageId) throws MyDefinitionException {
        if (StringUtils.isBlank(messageId)) {
            throw new MyDefinitionException("消息模板必须指定");
        }

        if (userId == null || userId <= 0) {
            throw new MyDefinitionException("用户必须登录");
        }

        if (activeId == null || activeId <= 0) {
            throw new MyDefinitionException("必须指定活动ID");
        }

        QubaomingMessageSubmit qubaomingMessageSubmit = QubaomingMessageSubmit.init();
        qubaomingMessageSubmit.setUserId(userId);
        qubaomingMessageSubmit.setActiveId(activeId);
        qubaomingMessageSubmit.setMessageId(messageId);
        int i = 0;
        try {
            i = qubaomingMessageSubmitMapper.insertUseGeneratedKeys(qubaomingMessageSubmit);
        } catch (Exception e) {
            //throw e;
        }
        return i;
    }

    /**
     * 通过查询条件查询资源
     *
     * @param example 查询条件
     * @return 返回数据
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingMessageSubmit> getQubaomingMessageSubmitByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("必须指定查询条件");
        }
        List<QubaomingMessageSubmit> qubaomingMessageSubmits = null;
        try {
            qubaomingMessageSubmits = qubaomingMessageSubmitMapper.selectByExample(example);
        } catch (Exception e) {
            throw e;
        }
        return qubaomingMessageSubmits;
    }

    /**
     * 更新用户订阅消息
     *
     * @param qubaomingMessageSubmit 订阅信息
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateQubaomingMessageSubmit(QubaomingMessageSubmit qubaomingMessageSubmit) throws MyDefinitionException {

        if (qubaomingMessageSubmit == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingMessageSubmitMapper.updateByPrimaryKey(qubaomingMessageSubmit);
        } catch (Exception e) {
            throw e;
        }
        return i;
    }
}

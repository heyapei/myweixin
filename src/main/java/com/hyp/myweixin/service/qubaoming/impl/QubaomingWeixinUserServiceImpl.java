package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingWeixinUserMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 22:58
 * @Description: TODO
 */

@Service
@Slf4j
public class QubaomingWeixinUserServiceImpl implements QubaomingWeixinUserService {

    @Autowired
    private QubaomingWeixinUserMapper qubaomingWeixinUserMapper;

    /**
     * 判断当前用户是否拥有完整权限 是否被禁用了 如果没有问题则不会被catch住
     *
     * @param id 用户ID
     * @throws MyDefinitionException
     */
    @Override
    public void validateUserRight(Integer id) throws MyDefinitionException {
        QubaomingWeixinUser qubaomingWeixinUser = selectByPkId(id);
        if (qubaomingWeixinUser == null) {
            throw new MyDefinitionException("没有找到当前用户信息，请重新进行授权操作");
        }else if (qubaomingWeixinUser.getEnable().equals(QubaomingWeixinUser.ENABLEENUM.UN_ENABLE.getCode())) {
            throw new MyDefinitionException("当前用户已被禁用");
        }
    }

    /**
     * 通过OpenId查询用户信息
     *
     * @param openId 微信的唯一值
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingWeixinUser getQubaomingWeixinUserByOpenId(String openId) throws MyDefinitionException {
        if (openId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingWeixinUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId", openId);
        QubaomingWeixinUser qubaomingWeixinUser = null;
        try {
            qubaomingWeixinUser = qubaomingWeixinUserMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("通过OpenId查询用户信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询用户信息操作过程错误");
        }

        return qubaomingWeixinUser;
    }

    /**
     * 创建趣报名用户基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingWeixinUser
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingWeixinUser qubaomingWeixinUser) throws MyDefinitionException {

        if (qubaomingWeixinUser == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        /*处理表情包问题*/
        String nickName = qubaomingWeixinUser.getNickName();
        if (StringUtils.isNotBlank(nickName)) {
            String s = EmojiParser.removeAllEmojis(nickName);
            if (StringUtils.isNotBlank(s)) {
                qubaomingWeixinUser.setNickName(s);
            }
        }

        Integer pkId = null;

        try {
            int i = qubaomingWeixinUserMapper.insertUseGeneratedKeys(qubaomingWeixinUser);
            if (i > 0) {
                pkId = qubaomingWeixinUser.getId();
            }
        } catch (Exception e) {
            log.error("创建趣报名用户基础信息返回主键操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("创建趣报名用户基础信息返回主键操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除趣报名数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPk(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingWeixinUserMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除趣报名数据操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("删除趣报名数据操作过程错误");
        }
        return  i;
    }

    /**
     * 更新有值的趣报名数据信息 要求必须有主键信息
     *
     * @param qubaomingWeixinUser
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveQubaomingWeixinUserBase(QubaomingWeixinUser qubaomingWeixinUser) throws MyDefinitionException {
        if (qubaomingWeixinUser == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        /*处理表情包问题*/
        String nickName = qubaomingWeixinUser.getNickName();
        if (StringUtils.isNotBlank(nickName)) {
            String s = EmojiParser.removeAllEmojis(nickName);
            if (StringUtils.isNotBlank(s)) {
                qubaomingWeixinUser.setNickName(s);
            }
        }

        int i = 0;
        try {
            i = qubaomingWeixinUserMapper.updateByPrimaryKeySelective(qubaomingWeixinUser);
        } catch (Exception e) {
            log.error("更新有值的趣报名数据信息操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("更新有值的趣报名数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找趣报名数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingWeixinUser selectByPkId(Integer pkId) throws MyDefinitionException {

        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        QubaomingWeixinUser qubaomingWeixinUser = null;
        try {
            qubaomingWeixinUser = qubaomingWeixinUserMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找趣报名数据操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("查找趣报名数据操作过程错误");
        }
        return qubaomingWeixinUser;
    }
}

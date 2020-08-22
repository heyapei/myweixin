package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingUserSignUpMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingUserSignUp;
import com.hyp.myweixin.service.qubaoming.QubaomingUserSignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/19 20:35
 * @Description: TODO
 */
@Slf4j
@Service
public class QubaomingUserSignUpServiceImpl implements QubaomingUserSignUpService {

    @Autowired
    private QubaomingUserSignUpMapper qubaomingUserSignUpMapper;

    /**
     * 删除符合条件的用户报名数据
     *
     * @param example 查询条件
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteQubaomingUserSignUpByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingUserSignUpMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error("删除符合条件的用户报名数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除符合条件的数据操作过程错误");
        }
        return i;


    }

    /**
     * 根据查询条件查询一条用户报名结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingUserSignUp selectOneQubaomingUserSignUpByExample(Example example) throws MyDefinitionException {
        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        QubaomingUserSignUp qubaomingUserSignUp = null;
        try {
             qubaomingUserSignUp = qubaomingUserSignUpMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("根据查询条件查询一条用户报名数据结果操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询操作过程错误");
        }

        return qubaomingUserSignUp;
    }

    /**
     * 根据查询条件查询用户报名结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingUserSignUp> selectQubaomingUserSignUpByExample(Example example) throws MyDefinitionException {
        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        List<QubaomingUserSignUp> qubaomingUserSignUpList = null;
        try {
            qubaomingUserSignUpList = qubaomingUserSignUpMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("根据查询条件查询全部用户报名结果操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询操作过程错误");
        }

        return qubaomingUserSignUpList;
    }

    /**
     * 创建用户报名基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingUserSignUp
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingUserSignUp qubaomingUserSignUp) throws MyDefinitionException {
        if (qubaomingUserSignUp == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = qubaomingUserSignUpMapper.insertUseGeneratedKeys(qubaomingUserSignUp);
            if (i > 0) {
                pkId = qubaomingUserSignUp.getId();
            }
        } catch (Exception e) {
            log.error("创建用户报名基础信息并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除用户报名数据
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
            i = qubaomingUserSignUpMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除用户报名数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的用户报名数据信息 要求必须有主键信息
     *
     * @param qubaomingUserSignUp
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveQubaomingUserSignUp(QubaomingUserSignUp qubaomingUserSignUp) throws MyDefinitionException {
        if (qubaomingUserSignUp == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingUserSignUpMapper.updateByPrimaryKeySelective(qubaomingUserSignUp);
        } catch (Exception e) {
            log.error("更新有值的用户报名数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找用户报名数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingUserSignUp selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingUserSignUp qubaomingUserSignUp = null;
        try {
            qubaomingUserSignUp = qubaomingUserSignUpMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找用户报名数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找数据操作过程错误");
        }
        return qubaomingUserSignUp;
    }
}

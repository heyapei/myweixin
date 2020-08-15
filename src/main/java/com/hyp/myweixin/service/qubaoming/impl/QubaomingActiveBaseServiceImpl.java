package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingActiveBaseMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 14:57
 * @Description: TODO
 */
@Service
@Slf4j
public class QubaomingActiveBaseServiceImpl implements QubaomingActiveBaseService {
    @Autowired
    private QubaomingActiveBaseMapper qubaomingActiveBaseMapper;


    /**
     * 更新活动描述和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateActiveDescAndImg(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException {
        Integer integer = null;
        try {
            integer = qubaomingActiveBaseMapper.updateActiveDescAndImg(qubaomingActiveBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新趣报名活动描述和图片操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("更新趣报名活动描述和图片操作过程错误");
        }
        return integer;
    }

    /**
     * 更新活动标题和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateActiveNameAndImg(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException {
        Integer integer = null;
        try {
            integer = qubaomingActiveBaseMapper.updateActiveNameAndImg(qubaomingActiveBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新趣报名活动名称和图片操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("更新趣报名活动名称和图片操作过程错误");
        }
        return integer;
    }

    /**
     * 获取用户未完成的活动ID 如果没有则新建一个给用户
     *
     * @param activeUserId 用户ID
     * @return 活动ID
     * @throws MyDefinitionException
     */
    @Override
    public Integer getUserUnCompleteActiveId(Integer activeUserId) throws MyDefinitionException {
        if (activeUserId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        List<QubaomingActiveBase> qubaomingActiveBases = selectUserActiveByStatus(activeUserId, QubaomingActiveBase.ActiveStatusEnum.UN_COMPLETE.getCode());
        if (qubaomingActiveBases != null && qubaomingActiveBases.size() > 0) {
            qubaomingActiveBase = qubaomingActiveBases.get(0);
        } else {
            qubaomingActiveBase = QubaomingActiveBase.init();
            qubaomingActiveBase.setActiveStatus(QubaomingActiveBase.ActiveStatusEnum.UN_COMPLETE.getCode());
            qubaomingActiveBase.setActiveUserId(activeUserId);
            try {
                Integer pkId = insertReturnPk(qubaomingActiveBase);
                qubaomingActiveBase.setId(pkId);
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
        }
        return qubaomingActiveBase.getId();
    }

    /**
     * 检查用户创建的不同活动状态的活动列表 不分页
     *
     * @param activeUserId 用户ID
     * @param activeStatus 活动状态
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingActiveBase> selectUserActiveByStatus(Integer activeUserId, Integer activeStatus) throws MyDefinitionException {
        Example example = new Example(QubaomingActiveBase.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeUserId", activeUserId);
        criteria.andEqualTo("activeStatus", activeStatus);
        List<QubaomingActiveBase> qubaomingActiveBases = null;
        try {
            qubaomingActiveBases = qubaomingActiveBaseMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询用户创建的不同活动状态的活动列表不分页操作过程错误，错误原因：{}", e, toString());
            throw new MyDefinitionException("查询用户创建的不同活动状态的活动列表不分页操作过程错误");
        }
        return qubaomingActiveBases;
    }

    /**
     * 创建活动基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveBase
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException {

        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = qubaomingActiveBaseMapper.insertUseGeneratedKeys(qubaomingActiveBase);
            if (i > 0) {
                pkId = qubaomingActiveBase.getId();
            }
        } catch (Exception e) {
            log.error("创建活动基础信息并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建活动基础信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPk(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数能为空");
        }

        int i = 0;
        try {
            i = qubaomingActiveBaseMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的数据信息
     *
     * @param qubaomingActiveBase
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveQubaomingActiveBase(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException {
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("参数能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveBaseMapper.updateByPrimaryKeySelective(qubaomingActiveBase);
        } catch (Exception e) {
            log.error("更新有值的数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingActiveBase selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数能为空");
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找数据操作过程错误");
        }
        return qubaomingActiveBase;
    }
}

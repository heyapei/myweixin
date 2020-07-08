package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteConfMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.service.WeixinVoteConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:59
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteConfServiceImpl implements WeixinVoteConfService {


    @Autowired
    private WeixinVoteConfMapper weixinVoteConfMapper;

    /**
     * 保存配置信息 请填写完整的数据 需要返回一个主键
     *
     * @param weixinVoteConf 完成的数据
     * @return 主键
     */
    @Override
    public Integer saveWeixinVoteConf(WeixinVoteConf weixinVoteConf) {

        int i = 0;
        try {
            i = weixinVoteConfMapper.insertUseGeneratedKeys(weixinVoteConf);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存配置信息并返回主键错误，错误原因：{}", e.toString());
        }
        if (i > 0) {
            return weixinVoteConf.getId();
        }
        return null;
    }

    /**
     * 通过主键查询配置信息
     *
     * @param id 主键
     * @return 配置表的信息
     */
    @Override
    public WeixinVoteConf getWeixinVoteConfByID(Integer id) {
        WeixinVoteConf weixinVoteConf = null;
        try {
            weixinVoteConf = weixinVoteConfMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过主键查询配置信息错误，错误原因：{}", e.toString());

        }
        return weixinVoteConf;
    }

    /**
     * 通过活动查询当前活动的配置表
     *
     * @param baseWorkId
     * @return
     */
    @Override
    public WeixinVoteConf getWeixinVoteConfByVoteWorkId(Integer baseWorkId) {


        Example example = new Example(WeixinVoteConf.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", baseWorkId);

        WeixinVoteConf weixinVoteConf = null;
        try {
            List<WeixinVoteConf> weixinVoteConfs = weixinVoteConfMapper.selectByExample(example);
            if (weixinVoteConfs != null && weixinVoteConfs.size() > 0) {
                return weixinVoteConfs.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过活动查询当前活动的配置表错误，错误原因：{}", e.toString());
        }

        return weixinVoteConf;
    }

    /**
     * 更新 只更新传过来的实体中有值的内容数据 按照主键
     *
     * @param weixinVoteConf 需要更新的实体类
     * @return 受影响的行数
     */
    @Override
    public Integer updateWeixinVoteConf(WeixinVoteConf weixinVoteConf) {
        int i = 0;
        try {
            i = weixinVoteConfMapper.updateByPrimaryKeySelective(weixinVoteConf);
        } catch (Exception e) {
            e.printStackTrace();

            log.error("按照主键只更新传过来的实体中有值的内容数据错误，错误原因：{}", e.toString());
        }
        return i;
    }

    /**
     * 保存活动配置项
     *
     * @param weixinVoteConf
     * @return
     */
    @Override
    public Integer saveSelectiveWeixinVoteConf(WeixinVoteConf weixinVoteConf) {

        int i = 0;
        try {
            i = weixinVoteConfMapper.insertSelective(weixinVoteConf);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存活动配置项错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存活动配置项错误");
        }
        return i;
    }
}

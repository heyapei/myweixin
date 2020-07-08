package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteOrganisersMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import com.hyp.myweixin.service.WeixinVoteOrganisersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 19:01
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteOrganisersServiceImpl implements WeixinVoteOrganisersService {
    @Autowired
    private WeixinVoteOrganisersMapper weixinVoteOrganisersMapper;


    /**
     * 保存公司信息 请填写完整的数据 需要返回一个主键
     *
     * @param weixinVoteOrganisers 完成的数据
     * @return 主键
     */
    @Override
    public Integer saveWeixinVoteOrganisers(WeixinVoteOrganisers weixinVoteOrganisers) {
        int i = 0;
        try {
            i = weixinVoteOrganisersMapper.insertUseGeneratedKeys(weixinVoteOrganisers);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存公司信息完整的数据返回一个主键错误，错误原因：{}", e.toString());
        }
        if (i > 0) {
            return weixinVoteOrganisers.getId();
        }
        return null;
    }

    /**
     * 通过主键查询公司信息
     *
     * @param id 主键
     * @return 公司信息
     */
    @Override
    public WeixinVoteOrganisers getWeixinVoteOrganisersByID(Integer id) {
        WeixinVoteOrganisers weixinVoteOrganisers = null;
        try {
            weixinVoteOrganisers = weixinVoteOrganisersMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过主键查询公司信息错误，错误原因：{}", e.toString());
        }
        return weixinVoteOrganisers;
    }

    /**
     * 通过活动查询当前活动的公司信息
     *
     * @param baseWorkId 活动ID
     * @return 公司信息
     */
    @Override
    public WeixinVoteOrganisers getWeixinVoteConfByVoteWorkId(Integer baseWorkId) {
        Example example = new Example(WeixinVoteOrganisers.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("voteBaseId", baseWorkId);

        WeixinVoteOrganisers weixinVoteOrganisers = null;
        try {
            List<WeixinVoteOrganisers> weixinVoteOrganisersList = weixinVoteOrganisersMapper.selectByExample(example);
            if (weixinVoteOrganisersList != null && weixinVoteOrganisersList.size() > 0) {
                return weixinVoteOrganisersList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过活动查询当前活动的公司信息，错误原因：{}", e.toString());
        }
        return weixinVoteOrganisers;
    }

    /**
     * 更新公司信息 只更新传过来的实体中有值的内容数据 按照主键
     *
     * @param weixinVoteOrganisers 需要更新的实体类
     * @return 受影响的行数
     */
    @Override
    public Integer updateSelectiveWeixinVoteOrganisers(WeixinVoteOrganisers weixinVoteOrganisers) {

        int i = 0;
        try {
            i = weixinVoteOrganisersMapper.updateByPrimaryKeySelective(weixinVoteOrganisers);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新公司信息错误，错误原因：{}", e.toString());
        }
        return i;
    }

    /**
     * 保存活动公司信息
     *
     * @param weixinVoteOrganisers
     * @return
     */
    @Override
    public Integer saveSelectiveWeixinVoteOrganisers(WeixinVoteOrganisers weixinVoteOrganisers) {
        int i = 0;
        try {
            i = weixinVoteOrganisersMapper.insertSelective(weixinVoteOrganisers);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存活动主办方信息错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存活动主办方信息错误");
        }
        return i;
    }
}

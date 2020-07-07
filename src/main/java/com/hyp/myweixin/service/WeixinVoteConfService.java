package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinVoteConf;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:46
 * @Description: TODO
 */
public interface WeixinVoteConfService {

    /*自定义比较多写在上面*/
    /*自定义实体操作*/



    /*表格实体操作*/

    /**
     * 保存配置信息 请填写完整的数据 需要返回一个主键
     *
     * @param weixinVoteConf 完成的数据
     * @return 主键
     */
    Integer saveWeixinVoteConf(WeixinVoteConf weixinVoteConf);

    /**
     * 通过主键查询配置信息
     *
     * @param id 主键
     * @return 配置表的信息
     */
    WeixinVoteConf getWeixinVoteConfByID(Integer id);

    /**
     * 通过活动查询当前活动的配置表
     *
     * @param baseWorkId 活动的ID
     * @return 活动配置表信息
     */
    WeixinVoteConf getWeixinVoteConfByVoteWorkId(Integer baseWorkId);

    /**
     * 更新 只更新传过来的实体中有值的内容数据 按照主键
     *
     * @param weixinVoteConf 需要更新的实体类
     * @return 受影响的行数
     */
    Integer updateWeixinVoteConf(WeixinVoteConf weixinVoteConf);

    /**
     * 保存活动配置项 只保存有值的数据
     *
     * @param weixinVoteConf 数据实体
     * @return 影响的行数
     */
    Integer saveSelectiveWeixinVoteConf(WeixinVoteConf weixinVoteConf);
}

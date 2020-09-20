package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinResource;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/20 23:30
 * @Description: TODO
 */
public interface RotationChartService {

    /**
     * 获取趣报名的轮播图信息
     *
     * @return
     * @throws MyDefinitionException
     */
    List<WeixinResource> getRotationChart() throws MyDefinitionException;


    /**
     * 按照主键删除文件信息 当前是只能删除config为6的数据
     *
     * @param rcId 文件主键
     * @return 影响函数
     * @throws MyDefinitionException
     */
    Integer deleteRotationChartById(Integer rcId) throws MyDefinitionException;

}

package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import com.hyp.myweixin.service.WeixinResourceService;
import com.hyp.myweixin.service.qubaoming.RotationChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/20 23:31
 * @Description: TODO
 */
@Service
@Slf4j
public class RotationChartServiceImpl implements RotationChartService {

    @Autowired
    private WeixinResourceService weixinResourceService;


    /**
     * 获取趣报名的轮播图信息
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public List<WeixinResource> getRotationChart() throws MyDefinitionException {
        List<WeixinResource> weixinResourceByConfigId = null;
        try {
            weixinResourceByConfigId = weixinResourceService.getWeixinResourceByConfigId(6, WeixinResource.Status.Allow.getState());
        } catch (Exception e) {
            throw new MyDefinitionException(e.getMessage());
        }
       /* String[] imgS = null;
        if (weixinResourceByConfigId != null) {
            imgS = new String[weixinResourceByConfigId.size()];
            for (int i = 0; i < weixinResourceByConfigId.size(); i++) {
                imgS[i] = weixinResourceByConfigId.get(i).getThumbnailPath1();
            }
        }*/
        return weixinResourceByConfigId;
    }

    /**
     * 按照主键删除文件信息 当前是只能删除config为6的数据
     *
     * @param rcId 文件主键
     * @return 影响函数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteRotationChartById(Integer rcId) throws MyDefinitionException {

        Example example = new Example(WeixinResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceConfigId", WeixinResourceConfig.ConfigType.QuBaoMingRotationChart.getConfigType());
        criteria.andEqualTo("id", rcId);
        Integer integer = null;
        try {
            integer = weixinResourceService.deleteByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return integer;
    }
}

package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingUserSignUp;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerIndexQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveManagerIndexVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveSignUpShowVO;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 9:34
 * @Description: TODO
 */
public interface ActiveManagerService {


    /**
     * 获取活动管理页面的信息
     *
     * @param activeManagerIndexQuery
     * @return
     * @throws MyDefinitionException
     */
    ActiveManagerIndexVO getActiveManagerIndexVOByActiveId(ActiveManagerIndexQuery activeManagerIndexQuery) throws MyDefinitionException;


    /**
     * 分页查询参与数据
     *
     * @param activeManagerByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getAllSignUpByPageQuery(ActiveManagerByPageQuery activeManagerByPageQuery) throws MyDefinitionException;


    /**
     * 获取报名必填数据
     * @param activeRequireOptionList
     * @param qubaomingUserSignUpList
     * @return
     * @throws MyDefinitionException
     */
    List<ActiveSignUpShowVO> getActiveSignUpVO(String[] activeRequireOptionList, List<QubaomingUserSignUp> qubaomingUserSignUpList) throws MyDefinitionException;
}

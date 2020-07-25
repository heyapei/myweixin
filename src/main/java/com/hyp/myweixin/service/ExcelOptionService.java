package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 15:56
 * @Description: TODO
 */
public interface ExcelOptionService {

    /**
     * 通过活动id获取excel导出的地址
     * 必须管理员权限
     *
     * @param activeId 活动id
     * @param userId   用户ID
     * @return 导出地址
     * @throws MyDefinitionException
     */
    String getExcelExportUrlByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;

}

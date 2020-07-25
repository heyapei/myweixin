package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.excel.active.ActiveVoteWorkExcelExportVO;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 15:56
 * @Description: TODO
 */
public interface ExcelOptionService {

    /**
     * 获取活动下的所有的作品并转换成excel的数据（ActiveVoteWorkExcelExport）
     *
     * @param activeId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    List<ActiveVoteWorkExcelExportVO> exportActiveVoteWorkExcelExportVOByActiveId(Integer activeId) throws MyDefinitionException;


    /**
     * 判断是否能够使用该地址进行导出处理
     *
     * @param excelExportKey 导出excel用的key 用于判断是否能够使用该地址进行导出处理
     * @return 返回需要导出的活动ID
     * @throws MyDefinitionException
     */
    Integer judgeExportRight(String excelExportKey) throws MyDefinitionException;


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

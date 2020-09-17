package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveComment;
import com.hyp.myweixin.pojo.qubaoming.query.comment.ActiveCommentPageQuery;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 13:06
 * @Description: TODO
 */
public interface QubaomingActiveCommentService {


    /**
     * 分页查询评价信息 按照时间先后顺序
     *
     * @param activeCommentPageQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo getPageInfoActiveCommentByActiveCommentPageQuery(ActiveCommentPageQuery activeCommentPageQuery) throws MyDefinitionException;


    /**
     * 添加评论信息
     *
     * @param userId
     * @param activeId
     * @param commentContent
     * @return
     * @throws MyDefinitionException
     */
    Integer addActiveCommentByUserIdActiveIdContent(Integer userId, Integer activeId, String commentContent) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveComment
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingActiveComment qubaomingActiveComment) throws MyDefinitionException;

    /**
     * 根据主键删除数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 根据条件删除数据
     *
     * @param example 条件
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByExample(Example example) throws MyDefinitionException;

    /**
     * 更新有值的数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveComment
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelective(QubaomingActiveComment qubaomingActiveComment) throws MyDefinitionException;

    /**
     * 通过主键查找数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveComment selectByPkId(Integer pkId) throws MyDefinitionException;

    /**
     * 通过活动ID查找数据 如果没有返回null
     *
     * @param activeId 活动ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveComment> selectListByActiveId(Integer activeId) throws MyDefinitionException;


    /**
     * 通过查询条件查找数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveComment> selectListByExample(Example example) throws MyDefinitionException;


}

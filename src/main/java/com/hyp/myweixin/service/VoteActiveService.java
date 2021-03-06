package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.query.voteactive.Page2OrgShowQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page3RegulationQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page4RegulationQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.utils.MyErrorList;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:33
 * @Description: TODO
 */
public interface VoteActiveService {


    /**
     * 添加第四屏幕的内容
     * 1. 性别限制
     * <p>
     * 逻辑：
     * 1.检查是否有配置数据 如果有则更新没有就生成 生成过程中写入数据
     *
     * @param page4RegulationQuery 查询实体类
     * @return 判断result的值
     */
    MyErrorList createPage4Regulation(Page4RegulationQuery page4RegulationQuery);


    /**
     * 添加第三屏幕的内容
     * 1. 活动开始时间 必须
     * 2. 活动结束时间 必须
     * 3. 是否可以对对手重复点赞 必须
     * 4. 是否开启在线报名 必须
     * 5. 报名开始时间 必须
     * 6. 报名结束时间 必须
     * 7. 报名手机号必填 必填
     * 8. 报名微信号必填 必填
     * 逻辑：
     * 1.检查是否有配置数据 如果有则更新没有就生成 生成过程中写入数据
     *
     * @param page3RegulationQuery 查询实体类
     * @return 判断result的值
     */
    MyErrorList createPage3Regulation(Page3RegulationQuery page3RegulationQuery);


    /**
     * 添加第二屏幕的内容
     * 1. 是否展示到首页 必须
     * 2. 活动分享图 必须
     * 3. 是否有主办方信息 必须
     * 4. 主办方信息
     * 逻辑：
     * 1.检查是否有配置数据 如果有则更新没有就生成 生成过程中写入数据
     * 2. 检查主办方信息 如果有则更新没有就生成 生成过程中写入数据
     *
     * @param page2Query 查询实体类
     * @return
     */
    Integer createPage2AndImg(Page2OrgShowQuery page2Query) throws MyDefinitionException;


    /**
     * 根据以下数据进行更新操作
     *
     * @param userId     用户ID
     * @param workId     活动ID
     * @param type       上传的数据类型
     * @param activeText 上传的文本 非必须
     * @param activeImg  上传的图片 使用英文;拼接好的
     * @return
     */
    Integer createBaseVoteWorkSavePageAndImg(int userId, int workId, String type,
                                             String activeText, String activeImg) throws MyDefinitionException;


    /**
     * 返回活动的ID 如果用户没有创建完成的数据则创建一个 ，如果有则返回新的活动ID
     *
     * @param userId
     * @return
     */
    Integer createBaseVoteWork(int userId);

    /**
     * 创建活动
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    Integer createVoteWork(WeixinVoteWorkDTO weixinVoteWorkDTO);


    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     * @throws MyDefinitionException
     */
    Result saveSingleRes(MultipartFile file, String type) throws MyDefinitionException;

}

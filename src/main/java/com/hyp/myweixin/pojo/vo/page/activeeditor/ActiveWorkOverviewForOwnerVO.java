package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 20:13
 * @Description: TODO 我的 数据总览 包含了个人的信息
 */
@Data
public class ActiveWorkOverviewForOwnerVO {


    /**
     * 生成带有默认值的数据结构
     *
     * @return
     */
    public static ActiveWorkOverviewForOwnerVO init() {
        ActiveWorkOverviewForOwnerVO activeWorkOverviewForOwnerVO = new ActiveWorkOverviewForOwnerVO();

        activeWorkOverviewForOwnerVO.setLaunchActiveNum(0);
        activeWorkOverviewForOwnerVO.setJoinUserWorkNum(0);
        activeWorkOverviewForOwnerVO.setVoteUserWorkNum(0);
        activeWorkOverviewForOwnerVO.setNickName("");
        activeWorkOverviewForOwnerVO.setAvatarUrl("");
        activeWorkOverviewForOwnerVO.setUserId(0);

        return activeWorkOverviewForOwnerVO;
    }

    /**
     * 我发起的活动
     */
    private Integer launchActiveNum;
    /**
     * 我上传的作品
     */
    private Integer joinUserWorkNum;
    /**
     * 我点赞的作品
     */
    private Integer voteUserWorkNum;

    /**
     * 用户名
     */
    private String nickName;
    /**
     * 用户头像信息
     */
    private String avatarUrl;
    /**
     * 用户ID
     */
    private Integer userId;


}

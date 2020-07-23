package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/23 21:23
 * @Description: TODO 管理员查看作品信息
 */
@Data
public class UserWorkDetailVO {

    private Integer userWorkId;

    private Integer userWorkOr;

    private String userWorkName;

    private String userWorkDesc;

    private String userPhone;

    private String userWeixin;

    private String[] userWorkImgS;



}

package com.hyp.myweixin.pojo.query.voteactive;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/7 13:00
 * @Description: TODO
 */
@Data
public class Page2OrgShowQuery {

    @NotNull(message = "用户ID不可以为空")
    private Integer userId;

    @NotNull(message = "活动ID不可以为空")
    private Integer voteWorkId;

    @NotNull(message = "是否展示到首页不能为空")
    private Integer isShowIndex;

    @NotNull(message = "分享图不可以为空")
    private String shareImg;

    @NotNull(message = "是否填写了公司信息不可以为空")
    private Integer hasOrganisers;

    /**
     * 公司
     */
    private String orgName;
    /**
     * 公司二维码
     */
    private String orgWeixinQrCode;

    /**
     * 公司的logo
     */
    private String orgLogoImg;
    /**
     * 公司联系电话
     */
    private String orgPhone;


}

package com.hyp.myweixin.pojo.qubaoming.vo.active;

import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/20 19:56
 * @Description: TODO
 */
@Data
public class ActiveDetailShowVO {
    @ApiModelProperty(value = "活动内容", name = "qubaomingActiveBase", required = true)
    private QubaomingActiveBase qubaomingActiveBase;


    @ApiModelProperty(value = "活动标题图", name = "activeImgList", required = true)
    private String[] activeImgList;
    @ApiModelProperty(value = "活动描述图", name = "activeDescImgList", required = true)
    private String[] activeDescImgList;
    @ApiModelProperty(value = "活动详情图", name = "activeDetailImgList", required = true)
    private String[] activeDetailImgList;
    @ApiModelProperty(value = "活动分享图", name = "activeShareImgList", required = true)
    private String[] activeShareImgList;
    @ApiModelProperty(value = "logo图", name = "logoImgList", required = true)
    private String[] logoImgList;
    @ApiModelProperty(value = "微信二维码", name = "weixinQrCodeList", required = true)
    private String[] weixinQrCodeList;


    @ApiModelProperty(value = "公司数据", name = "wechatCompany", required = true)
    private WechatCompany wechatCompany;

    @ApiModelProperty(value = "活动配置数据", name = "qubaomingActiveConfig", required = true)
    private QubaomingActiveConfig qubaomingActiveConfig;

    @ApiModelProperty(value = "已报名数量", name = "signUpNum", required = true)
    private Integer signUpNum;


}

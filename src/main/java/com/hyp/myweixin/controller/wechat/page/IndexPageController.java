package com.hyp.myweixin.controller.wechat.page;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.IndexImgVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinResourceService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 21:25
 * @Description: TODO
 */
@RestController
@RequestMapping("/page/index")
@Slf4j
public class IndexPageController {

    @Autowired
    WeixinResourceService weixinResourceService;
    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;


    /**
     * 分页返回投票活动的信息
     *
     * @param request
     * @return
     */
    @PostMapping("/vote/works")
    public Result getVoteWorkByPage(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "5") int pageSize) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);

        WeixinVoteBase weixinVoteBase = new WeixinVoteBase();
        weixinVoteBase.setActivePublic(1);
        weixinVoteBase.setStatus(1);
        log.info("当前查询条件：{}", weixinVoteBase.toString());
        PageInfo voteWorkByPage = weixinVoteBaseService.getVoteWorkByPage(weixinVoteBase, pageInfo);


        return Result.buildResult(Result.Status.OK, voteWorkByPage);
    }


    /**
     * 获取轮播图的内容
     *
     * @param request
     * @return
     */
    @GetMapping("/carousel/image")
    public Result getIndexCarousel(HttpServletRequest request) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        /*todo 2020年5月28日 好像还要加上端口号 不知道以后服务器上不需要加上*/
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";


        List<WeixinResource> weixinResourceList = weixinResourceService.getWeixinResourceByConfigId(
                WeixinResourceConfig.ConfigType.HeadImg.getConfigType(),
                WeixinResource.Status.Allow.getState());
        List<IndexImgVO> indexImgList = new ArrayList<>(6);

        for (WeixinResource weixinResource : weixinResourceList) {
            IndexImgVO indexImg = new IndexImgVO();
            indexImg.setUrl(basePath + weixinResource.getPath());
            indexImg.setTitle(weixinResource.getTitle());
            indexImgList.add(indexImg);
        }
        return Result.buildResult(Result.Status.OK, indexImgList);
    }

}

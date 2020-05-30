package com.hyp.myweixin.controller.wechat.page;

import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import com.hyp.myweixin.pojo.vo.page.IndexImg;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class IndexController {

    @Autowired
    WeixinResourceService weixinResourceService;


    @GetMapping("/carousel/image")
    public Result getIndexCarousel(HttpServletRequest request) {
        /*todo 2020年5月28日 好像还要加上端口号 不知道以后服务器上不需要加上*/
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


        List<WeixinResource> weixinResourceList = weixinResourceService.getWeixinResourceByConfigId(
                WeixinResourceConfig.ConfigType.HeadImg.getConfigType(),
                WeixinResource.Status.Allow.getState());
        List<IndexImg> indexImgList = new ArrayList<>(6);

        for (WeixinResource weixinResource : weixinResourceList) {
            IndexImg indexImg = new IndexImg();
            indexImg.setUrl(basePath + weixinResource.getPath());
            indexImg.setTitle(weixinResource.getTitle());
            indexImgList.add(indexImg);
        }
        return Result.buildResult(Result.Status.OK, indexImgList);
    }

}

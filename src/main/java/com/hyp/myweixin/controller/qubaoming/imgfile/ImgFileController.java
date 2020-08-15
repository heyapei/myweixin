package com.hyp.myweixin.controller.qubaoming.imgfile;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.ImgFileService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 23:58
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/resource/option")
@Slf4j
public class ImgFileController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ImgFileService imgFileService;


    @ApiOperation(value = "图片数据保存", tags = {"资源文件保存"})
    @RequestMapping("img/upload")
    public Result fileUpload(@RequestParam("file") MultipartFile file, String type) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Result coversImg = null;
        try {
            coversImg = imgFileService.saveSingleRes(file, type);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }
        log.info("保存数据：{}", coversImg.toString());
        return coversImg;
    }

}

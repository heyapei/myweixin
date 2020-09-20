package com.hyp.myweixin.controller.qubaoming.imgfile;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.qubaoming.query.CommonQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.ImgFileService;
import com.hyp.myweixin.service.qubaoming.RotationChartService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Autowired
    private RotationChartService rotationChartService;


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


    @ApiOperation(value = "获取趣报名的轮播图", tags = {"资源文件保存"})
    @RequestMapping("img/getRotationChart")
    public Result getRotationChart(
            @Validated CommonQuery commonQuery,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        try {
            List<WeixinResource> rotationChart = rotationChartService.getRotationChart();
            if (rotationChart == null || rotationChart.size()<=0) {
                return Result.buildResult(Result.Status.NOT_FOUND, "没有找到可用的轮播图数据");
            } else {
                return Result.buildResult(Result.Status.OK, rotationChart);
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "按照文件主键删除趣报名的轮播图", tags = {"资源文件保存"})
    @RequestMapping("img/deleteRotationChart/id")
    public Result deleteRotationChartById(Integer rcId, @Validated CommonQuery commonQuery,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        if (rcId == null || rcId <= 0) {
            return Result.buildResult(Result.Status.BAD_REQUEST, "必须指定要删除的轮播图ID");
        }
        try {
            Integer integer = rotationChartService.deleteRotationChartById(rcId);
            if (integer == null || integer <= 0) {
                return Result.buildResult(Result.Status.BAD_REQUEST, "未能成功找到要删除的趣报名轮播图");
            } else {
                return Result.buildResult(Result.Status.OK, "删除成功");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }
    }


}

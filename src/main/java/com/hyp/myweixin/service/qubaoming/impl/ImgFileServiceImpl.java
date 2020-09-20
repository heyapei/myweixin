package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.config.imgvideres.ImgVideResConfig;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.ResourceSimpleDTO;
import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinResourceConfigService;
import com.hyp.myweixin.service.WeixinResourceService;
import com.hyp.myweixin.service.qubaoming.ImgFileService;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.fileutil.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 0:00
 * @Description: TODO
 */
@Slf4j
@Service
public class ImgFileServiceImpl implements ImgFileService {


    @Autowired
    private ImgVideResConfig imgVideResConfig;
    @Autowired
    private WeixinResourceService weixinResourceService;
    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;
    @Autowired
    private WeixinResourceConfigService weixinResourceConfigService;


    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     */
    @Override
    public Result saveSingleRes(MultipartFile file, String type) throws MyDefinitionException {

        if (type == null) {
            throw new MyDefinitionException("未指定上传文件自定义类型");
        }
        if (file == null) {
            throw new MyDefinitionException("上传文件为空");
        }


        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
            //log.info("系统路径：{}", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MyDefinitionException("获取项目路径失败");
        }


        int resource_config_id = 0;
        String savePath = path + imgVideResConfig.getQuBaoMingActiveImgBasePath();
        if (type.equalsIgnoreCase(imgVideResConfig.getQuBaoMingActiveVoteWorkPath())) {
            savePath += imgVideResConfig.getQuBaoMingActiveVoteWorkPath();
            resource_config_id = 4;
        } else if (type.equalsIgnoreCase(imgVideResConfig.getQuBaoMingActiveUserWorkPath())) {
            resource_config_id = 5;
            savePath += imgVideResConfig.getQuBaoMingActiveUserWorkPath();
        } else if (type.equalsIgnoreCase(imgVideResConfig.getQuBaoMingRotationChartPath())) {
            resource_config_id = 6;
            savePath += imgVideResConfig.getQuBaoMingRotationChartPath();
        } else {
            throw new MyDefinitionException("还未定义数据，还请和技术沟通");
        }


        ResourceSimpleDTO resourceSimpleDTO = new ResourceSimpleDTO();
        String fileMd5 = MyFileUtil.getFileMd5(file);
        log.info("文件MD5：{}", fileMd5);
        WeixinResource weixinResourceByMD5 = weixinResourceService.getWeixinResourceByMD5AndConfigId(fileMd5, resource_config_id);
        // 如果存在则直接返回数据
        if (weixinResourceByMD5 != null) {
            // 2020年7月28日 返回压缩过的缩略图 如果有就返回 如果没有就返回原图
            if (StringUtils.isNotBlank(weixinResourceByMD5.getThumbnailPath1())) {
                resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getThumbnailPath1());
            } else {
                resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getPath());
            }
            resourceSimpleDTO.setFileType(weixinResourceByMD5.getType());
            resourceSimpleDTO.setSaveFileName(weixinResourceByMD5.getName());
            resourceSimpleDTO.setOriginalFileName(weixinResourceByMD5.getRealName());
            resourceSimpleDTO.setFileSize(weixinResourceByMD5.getSize());
            log.info("直接返回数据：{}", weixinResourceByMD5.toString());
            return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
        }


        String accessTokenByAppName = null;
        try {
            accessTokenByAppName = weixinSmallContentDetectionApiService.getAccessTokenByAppName("qubaoming");
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        /*如果当前不在数据库中 则请求图片验证接口*/
        Boolean aBoolean = false;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkImgSecCheckApi(file, accessTokenByAppName);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (!aBoolean) {
            throw new MyDefinitionException("图片违规重新选择");
        }

        weixinResourceByMD5 = weixinResourceService.getWeixinResourceByMD5AndConfigId(fileMd5, resource_config_id);
        // 如果存在则直接返回数据
        if (weixinResourceByMD5 != null) {
            // 2020年7月28日 返回压缩过的缩略图 如果有就返回 如果没有就返回原图
            if (StringUtils.isNotBlank(weixinResourceByMD5.getThumbnailPath1())) {
                resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getThumbnailPath1());
            } else {
                resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getPath());
            }
            resourceSimpleDTO.setFileType(weixinResourceByMD5.getType());
            resourceSimpleDTO.setSaveFileName(weixinResourceByMD5.getName());
            resourceSimpleDTO.setOriginalFileName(weixinResourceByMD5.getRealName());
            resourceSimpleDTO.setFileSize(weixinResourceByMD5.getSize());
            log.info("直接返回数据：{}", weixinResourceByMD5.toString());
            return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
        }


        // 文件大小
        resourceSimpleDTO.setFileSize(file.getSize());
        // 文件类型
        // 获得文件类型 image/jpeg
        String fileType = file.getContentType();
        // 获得文件后缀名称 jpeg
        fileType = fileType.substring(fileType.indexOf("/") + 1);
        resourceSimpleDTO.setFileType(fileType);

        // 原名称 test.jpg
        String fileName = file.getOriginalFilename();

        // 文件原名称 去除后缀
        String fileOriginalFilename = fileName.substring(0, file.getOriginalFilename().lastIndexOf("."));
        resourceSimpleDTO.setOriginalFileName(fileOriginalFilename);

        String filename = fileName.substring(fileName.lastIndexOf("\\") + 1);
        //得到文件保存的名称 f1a315ae4b7a4c81b5cf1cb396fbfdd2_test.jpg
        String saveFilename = MyFileUtil.makeFileName(filename);
        //log.info("图片最后名字{}", saveFilename);
        resourceSimpleDTO.setSaveFileName(saveFilename.substring(0, saveFilename.lastIndexOf(".")));
        //得到文件的保存目录
        String realSavePath = MyFileUtil.makePath(saveFilename, savePath);
        try {
            MyFileUtil.upload(file, realSavePath, saveFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String dataSavePath = File.separator + realSavePath.substring(realSavePath.indexOf(imgVideResConfig.getActiveImgBasePath()));
        String dataSavePath = "/" + realSavePath.substring(realSavePath.indexOf(imgVideResConfig.getQuBaoMingActiveImgBasePath()));
        // 拼接图片url
        String fileUrl = dataSavePath + "/" + saveFilename;
        resourceSimpleDTO.setFileUrl(fileUrl);


        WeixinResource weixinResource = new WeixinResource();
        weixinResource.setCreateTime(new Date());
        weixinResource.setType(resourceSimpleDTO.getFileType());
        weixinResource.setDescription("用户上传文件");
        weixinResource.setName(resourceSimpleDTO.getSaveFileName());
        weixinResource.setRealName(resourceSimpleDTO.getOriginalFileName());
        weixinResource.setMd5(fileMd5);
        weixinResource.setPath(fileUrl);
        weixinResource.setTitle("资源文件");
        weixinResource.setResourceConfigId(resource_config_id);
        weixinResource.setStatus(0);
        weixinResource.setSize(resourceSimpleDTO.getFileSize());


        String thumbnailPath1 = null;
        String activeUserWorkThumbnailsPath = imgVideResConfig.getQuBaoMingActiveUserWorkThumbnailsPath();
        String activeVoteWorkThumbnailsPath = imgVideResConfig.getQuBaoMingActiveVoteWorkThumbnailsPath();
        String rotationChartThumbnailsPath = imgVideResConfig.getQuBaoMingRotationChartThumbnailsPath();
        String activeUserWorkPath = imgVideResConfig.getQuBaoMingActiveUserWorkPath();
        String activeVoteWorkPath = imgVideResConfig.getQuBaoMingActiveVoteWorkPath();
        String rotationChartPath = imgVideResConfig.getQuBaoMingRotationChartPath();

        if (fileUrl.contains(activeUserWorkPath)) {
            thumbnailPath1 = fileUrl.replaceAll(activeUserWorkPath, activeUserWorkThumbnailsPath);
        } else if (fileUrl.contains(activeVoteWorkPath)) {
            thumbnailPath1 = fileUrl.replaceAll(activeVoteWorkPath, activeVoteWorkThumbnailsPath);
        }else if (fileUrl.contains(rotationChartPath)) {
            thumbnailPath1 = fileUrl.replaceAll(rotationChartPath, rotationChartThumbnailsPath);
        }


        String thumbnailPath1Temp = path + thumbnailPath1;
        String fileUrlTemp = path + fileUrl;
        String thumbnailPath1FileUrl = thumbnailPath1Temp.substring(0, thumbnailPath1Temp.lastIndexOf("/"));
        //log.info("临时文件地址：{}", thumbnailPath1FileUrl);
        File thumbnailPath1File = new File(thumbnailPath1FileUrl);
        if (!thumbnailPath1File.exists()) {
            //创建目录
            thumbnailPath1File.mkdirs();
        }
        //log.info("yuantu:" + fileUrlTemp);
        //log.info("thumbnailPath1Temp:" + thumbnailPath1Temp);
        /*只压缩大小不裁剪*/
        try {
            Thumbnails.of(fileUrlTemp).scale(1f).outputQuality(0.5f).toFile(thumbnailPath1Temp);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("将原图转换图片压缩图失败");
        }
        weixinResource.setThumbnailPath1(thumbnailPath1);

        /*文件压缩完成后删除源文件 实在是有点大了
         * 按照分配20G的硬盘容量 每张图片3M那么也只能容纳6862张图片
         * 当前仅仅是测试就已经有了300多张图片了
         * 完全不够用
         * */
        try {
            File fileDelete = new File(fileUrlTemp);
            if (fileDelete.delete()) {
                weixinResource.setPath("原文件已删除");
            } else {
                log.error("原文件删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("源文件删除失败，失败原因：{}", e.toString());
        }

        /**
         * 如果缩略图生成成功则返回缩略图
         */
        if (StringUtils.isNotBlank(thumbnailPath1)) {
            resourceSimpleDTO.setFileUrl(thumbnailPath1);
        }

        weixinResource.setThumbnailPath2("");
        weixinResource.setThumbnailPath3("");
        weixinResource.setThumbnailUrl1("");
        weixinResource.setThumbnailUrl2("");
        weixinResource.setThumbnailUrl3("");

        WeixinResourceConfig weixinResourceConfigById = weixinResourceConfigService.getWeixinResourceConfigById(resource_config_id);
        if (weixinResourceConfigById != null) {
            weixinResource.setTitle(weixinResourceConfigById.getKeyWord());
            weixinResource.setDescription(weixinResourceConfigById.getDescription());
        } else {
            weixinResource.setTitle("");
            weixinResource.setDescription("");
        }
        weixinResource.setCreateUserId(0);

        weixinResourceService.addWeixinResource(weixinResource);


        return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
    }
}

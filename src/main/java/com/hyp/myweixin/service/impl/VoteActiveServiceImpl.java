package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.config.imgvideres.ImgVideResConfig;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.ResourceSimpleDTO;
import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.fileutil.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:37
 * @Description: TODO
 */
@Service
@Slf4j
public class VoteActiveServiceImpl implements VoteActiveService {


    @Autowired
    private ImgVideResConfig imgVideResConfig;

    @Autowired
    private WeixinResourceService weixinResourceService;
    @Autowired
    private WeixinResourceConfigService weixinResourceConfigService;

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteConfService weixinVoteConfService;
    @Autowired
    private WeixinVoteOrganisersService weixinVoteOrganisersService;

    /**
     * 创建活动
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createVoteWork(WeixinVoteWorkDTO weixinVoteWorkDTO) {

        /*在这里做一下预判断 因为有可能会出现图片链接地址出现空的情况*/

        /*活动展示封面*/
        String activeImg = weixinVoteWorkDTO.getActiveImg();
        if (activeImg.contains(";")) {
            String[] split = activeImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveImg(imgUrlS.toString());
        }

        /*活动介绍*/
        String activeDescImg = weixinVoteWorkDTO.getActiveDescImg();
        if (activeDescImg.contains(";")) {
            String[] split = activeDescImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveDescImg(imgUrlS.toString());
        }
        /*活动奖励*/
        String activeRewardImg = weixinVoteWorkDTO.getActiveRewardImg();
        if (activeRewardImg.contains(";")) {
            String[] split = activeRewardImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveRewardImg(imgUrlS.toString());
        }


        Integer resultVal = null;

        WeixinVoteBase weixinVoteBaseWithWeixinVoteWorkDTO = getWeixinVoteBaseWithWeixinVoteWorkDTO(weixinVoteWorkDTO);
        log.info("查询数据：{}", weixinVoteBaseWithWeixinVoteWorkDTO.toString());
        Integer saveVoteBaseKey = weixinVoteBaseService.saveVoteBase(weixinVoteBaseWithWeixinVoteWorkDTO);
        resultVal = saveVoteBaseKey;
        if (saveVoteBaseKey != null && saveVoteBaseKey > 0) {
            log.info("保存活动基础信息成功成功");
            /*提取weixinVoteOrganisers*/
            WeixinVoteOrganisers weixinVoteOrganisersWithWeixinVoteWorkDTO = getWeixinVoteOrganisersWithWeixinVoteWorkDTO(weixinVoteWorkDTO);
            weixinVoteOrganisersWithWeixinVoteWorkDTO.setVoteBaseId(saveVoteBaseKey);
            Integer saveWeixinVoteOrganisers = weixinVoteOrganisersService.saveWeixinVoteOrganisers(weixinVoteOrganisersWithWeixinVoteWorkDTO);
            resultVal = saveWeixinVoteOrganisers;
            if (saveWeixinVoteOrganisers != null && saveWeixinVoteOrganisers > 0) {
                log.info("保存活动主办方信息成功");
                /*提取WeixinVoteConf数据*/
                WeixinVoteConf weixinVoteConfWithWeixinVoteWorkDTO = getWeixinVoteConfWithWeixinVoteWorkDTO(weixinVoteWorkDTO);

                weixinVoteConfWithWeixinVoteWorkDTO.setActiveVoteBaseId(saveVoteBaseKey);

                Integer saveWeixinVoteConf = weixinVoteConfService.saveWeixinVoteConf(weixinVoteConfWithWeixinVoteWorkDTO);
                resultVal = saveWeixinVoteConf;
                if (saveWeixinVoteConf != null && saveWeixinVoteConf > 0) {
                    log.info("保存活动配置信息成功");
                }
            }
        }
        return resultVal;
    }


    /**
     * 从weixinVoteWorkDTO提取weixinVoteConf数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteConf getWeixinVoteConfWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteConf weixinVoteConf = null;
        try {
            weixinVoteConf = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteConf.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取weixinVoteConf数据错误，错误原因：{}", e.toString());
        }
        return weixinVoteConf;
    }

    /**
     * 从weixinVoteWorkDTO提取weixinVoteOrganisers数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteOrganisers getWeixinVoteOrganisersWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteOrganisers weixinVoteOrganisers = null;
        try {
            weixinVoteOrganisers = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteOrganisers.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取weixinVoteOrganisers数据错误，错误原因：{}", e.toString());
        }
        return weixinVoteOrganisers;
    }


    /**
     * 从weixinVoteWorkDTO提取WeixinVoteBase数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteBase getWeixinVoteBaseWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取WeixinVoteBase数据错误，错误原因：{}", e.toString());
        }
        weixinVoteBase.setViewCountNum(0);
        weixinVoteBase.setVoteCountNum(0);
        return weixinVoteBase;
    }

    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     */
    @Override
    public Result saveSingleRes(MultipartFile file, String type) {

        log.info("数据类型");
        log.info("数据类型，{}", type);

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


        ResourceSimpleDTO resourceSimpleDTO = new ResourceSimpleDTO();


        String fileMd5 = MyFileUtil.getFileMd5(file);
        log.info("文件MD5：{}", fileMd5);

        WeixinResource weixinResourceByMD5 = weixinResourceService.getWeixinResourceByMD5(fileMd5);
        // 如果存在则直接返回数据
        if (weixinResourceByMD5 != null) {
            resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getPath());
            resourceSimpleDTO.setFileType(weixinResourceByMD5.getType());
            resourceSimpleDTO.setSaveFileName(weixinResourceByMD5.getName());
            resourceSimpleDTO.setOriginalFileName(weixinResourceByMD5.getRealName());
            resourceSimpleDTO.setFileSize(weixinResourceByMD5.getSize());
            log.info("直接返回数据：{}", weixinResourceByMD5.toString());
            return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
        }

        log.info("数据类型：{}", type);

        int resource_config_id = 0;
        String savePath = path + imgVideResConfig.getActiveImgBasePath();
        if (type.equalsIgnoreCase(imgVideResConfig.getActiveVoteWorkPath())) {
            savePath += imgVideResConfig.getActiveVoteWorkPath();
            resource_config_id = 2;
        } else if (type.equalsIgnoreCase(imgVideResConfig.getActiveUserWorkPath())) {
            resource_config_id = 3;
            savePath += imgVideResConfig.getActiveUserWorkPath();
        } else {
            throw new MyDefinitionException("还未定义数据，还请和技术沟通");
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
        String dataSavePath = "/" + realSavePath.substring(realSavePath.indexOf(imgVideResConfig.getActiveImgBasePath()));
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

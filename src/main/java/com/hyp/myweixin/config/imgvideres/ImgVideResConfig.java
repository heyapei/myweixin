package com.hyp.myweixin.config.imgvideres;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:39
 * @Description: TODO
 */
@Getter
@Component
@PropertySource("classpath:imgvideres.properties")
public class ImgVideResConfig {

    @Value("${active.img.base.path}")
    private String activeImgBasePath;

    @Value("${active.img.vote.work.path}")
    private String activeVoteWorkPath;

    @Value("${active.img.user.work.path}")
    private String activeUserWorkPath;

    @Value("${active.img.vote.work.thumbnails.path}")
    private String activeVoteWorkThumbnailsPath;

    @Value("${active.img.user.work.thumbnails.path}")
    private String activeUserWorkThumbnailsPath;




    @Value("${active.img.qubaoming.base.path}")
    private String quBaoMingActiveImgBasePath;

    @Value("${active.img.qubaoming.vote.work.path}")
    private String quBaoMingActiveVoteWorkPath;

    @Value("${active.img.qubaoming.user.work.path}")
    private String quBaoMingActiveUserWorkPath;

    @Value("${active.img.qubaoming.rotation.chart.path}")
    private String quBaoMingRotationChartPath;

    @Value("${active.img.qubaoming.vote.work.thumbnails.path}")
    private String quBaoMingActiveVoteWorkThumbnailsPath;

    @Value("${active.img.qubaoming.user.work.thumbnails.path}")
    private String quBaoMingActiveUserWorkThumbnailsPath;

    @Value("${active.img.qubaoming.rotation.chart.thumbnails.path}")
    private String quBaoMingRotationChartThumbnailsPath;


}

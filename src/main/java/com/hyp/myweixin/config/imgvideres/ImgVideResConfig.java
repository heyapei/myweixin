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


}

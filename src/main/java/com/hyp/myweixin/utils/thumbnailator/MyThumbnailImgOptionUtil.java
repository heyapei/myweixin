package com.hyp.myweixin.utils.thumbnailator;

import com.hyp.myweixin.exception.MyDefinitionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 9:53
 * @Description: TODO
 */
public interface MyThumbnailImgOptionUtil {

    /**
     * inputStream转成File
     *
     * @param ins
     * @param file
     * @throws MyDefinitionException
     */
    void inputStreamToFile(InputStream ins, File file) throws MyDefinitionException;


    /**
     * file转MultipartFile
     *
     * @param file
     * @return
     * @throws MyDefinitionException
     */
    MultipartFile fileToMultipartFile(File file) throws MyDefinitionException;

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws MyDefinitionException
     */
    Long getFileSize(File file) throws MyDefinitionException;

    /**
     * 压缩图片
     *
     * @param multipartFile      图片文件
     * @param myThumbnailImgType 需要转换的类型 填写null为不转换图片类型
     * @param width              压缩宽度
     * @param length             压缩高度
     * @param outputQuality      压缩质量比 outputQuality是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差
     *                           /@param scale   不可以和size同时使用           压缩比例 缩放比例，大于1就是变大，小于1就是缩小 可使用该公式计算 1.0 / (num / (1024 * 1024) + 1.0)
     * @return
     * @throws MyDefinitionException
     */
    InputStream compressImage(MultipartFile multipartFile,
                              MyThumbnailImgType myThumbnailImgType,
                              Integer width, Integer length,
                              Float outputQuality) throws MyDefinitionException;


}

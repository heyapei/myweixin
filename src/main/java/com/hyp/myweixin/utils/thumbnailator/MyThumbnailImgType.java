package com.hyp.myweixin.utils.thumbnailator;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 9:59
 * @Description: TODO
 */
public enum MyThumbnailImgType {
    /**
     * 几种常见的图片格式
     */
    IMAGE_TYPE_JPEG("jpeg", "联合照片专家组"),
    IMAGE_TYPE_BMP("bmp", "英文Bitmap（位图）的简写,它是Windows操作系统中的标准图像文件格式"),
    IMAGE_TYPE_PNG("png", "可移植网络图形"),
    IMAGE_TYPE_PSD("psd", "Photoshop的专用格式Photoshop"),
    IMAGE_TYPE_GIF("gif", "图形交换格式"),
    IMAGE_TYPE_JPG("jpg", "联合照片专家组");


    private String ImgType;

    private String ImgTypeDesc;

    MyThumbnailImgType(String imgType, String imgTypeDesc) {
        ImgType = imgType;
        ImgTypeDesc = imgTypeDesc;
    }

    public String getImgType() {
        return ImgType;
    }

    public String getImgTypeDesc() {
        return ImgTypeDesc;
    }
}

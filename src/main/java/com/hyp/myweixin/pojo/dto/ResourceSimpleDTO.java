package com.hyp.myweixin.pojo.dto;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:46
 * @Description: TODO 资源类文件简单传输类
 */
@Data
public class ResourceSimpleDTO {

    /**
     * 文件原始名称
     */
    private String originalFileName;

    /**
     * 文件保存名称
     */
    private String saveFileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件网络地址
     */
    private String fileUrl;


}

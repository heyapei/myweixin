package com.hyp.myweixin.pojo.dto;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:25
 * @Description: TODO 高德的iP转Address地址的接口返回信息
 */
@Data
public class AmapIpToAddressDTO {

    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;

}

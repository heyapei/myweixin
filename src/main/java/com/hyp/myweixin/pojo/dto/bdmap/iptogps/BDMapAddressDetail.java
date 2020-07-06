/**
  * Copyright 2020 bejson.com 
  */
package com.hyp.myweixin.pojo.dto.bdmap.iptogps;

import lombok.Data;

/**
 * Auto-generated: 2020-07-06 15:19:13
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class BDMapAddressDetail {

    private String city;
    private int city_code;
    private String district;
    private String province;
    private String street;
    private String street_number;
    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setCity_code(int city_code) {
         this.city_code = city_code;
     }
     public int getCity_code() {
         return city_code;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setStreet(String street) {
         this.street = street;
     }
     public String getStreet() {
         return street;
     }

    public void setStreet_number(String street_number) {
         this.street_number = street_number;
     }
     public String getStreet_number() {
         return street_number;
     }

}
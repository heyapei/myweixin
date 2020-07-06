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
public class BDMapContent {

    private String address;
    private BDMapAddressDetail address_detail;
    private BDMapPoint point;
    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setAddress_detail(BDMapAddressDetail address_detail) {
         this.address_detail = address_detail;
     }
     public BDMapAddressDetail getAddress_detail() {
         return address_detail;
     }

    public void setPoint(BDMapPoint point) {
         this.point = point;
     }
     public BDMapPoint getPoint() {
         return point;
     }

}
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
public class BDMapIpToGpsDTO {

    private String address;
    private BDMapContent content;
    private int status;
    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setContent(BDMapContent content) {
         this.content = content;
     }
     public BDMapContent getContent() {
         return content;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

}
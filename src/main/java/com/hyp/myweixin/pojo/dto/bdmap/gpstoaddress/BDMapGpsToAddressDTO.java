/**
  * Copyright 2020 bejson.com 
  */
package com.hyp.myweixin.pojo.dto.bdmap.gpstoaddress;

import lombok.Data;

/**
 * Auto-generated: 2020-07-06 15:23:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class BDMapGpsToAddressDTO {

    private int status;
    private BDMapResult result;
    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setResult(BDMapResult result) {
         this.result = result;
     }
     public BDMapResult getResult() {
         return result;
     }

}
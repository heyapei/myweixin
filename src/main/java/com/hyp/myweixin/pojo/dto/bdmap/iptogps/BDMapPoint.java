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
public class BDMapPoint {

    private String x;
    private String y;
    public void setX(String x) {
         this.x = x;
     }
     public String getX() {
         return x;
     }

    public void setY(String y) {
         this.y = y;
     }
     public String getY() {
         return y;
     }

}
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
public class BDMapLocation {

    private double lng;
    private double lat;
    public void setLng(double lng) {
         this.lng = lng;
     }
     public double getLng() {
         return lng;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

}
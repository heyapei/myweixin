/**
  * Copyright 2020 bejson.com 
  */
package com.hyp.myweixin.pojo.dto.bdmap.gpstoaddress;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2020-07-06 15:23:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class BDMapResult {

    private BDMapLocation location;
    private String formatted_address;
    private String business;
    private BDMapAddressComponent addressComponent;
    private List<String> pois;
    private List<String> roads;
    private List<String> poiRegions;
    private String sematic_description;
    private int cityCode;
    public void setLocation(BDMapLocation location) {
         this.location = location;
     }
     public BDMapLocation getLocation() {
         return location;
     }

    public void setFormatted_address(String formatted_address) {
         this.formatted_address = formatted_address;
     }
     public String getFormatted_address() {
         return formatted_address;
     }

    public void setBusiness(String business) {
         this.business = business;
     }
     public String getBusiness() {
         return business;
     }

    public void setAddressComponent(BDMapAddressComponent addressComponent) {
         this.addressComponent = addressComponent;
     }
     public BDMapAddressComponent getAddressComponent() {
         return addressComponent;
     }

    public void setPois(List<String> pois) {
         this.pois = pois;
     }
     public List<String> getPois() {
         return pois;
     }

    public void setRoads(List<String> roads) {
         this.roads = roads;
     }
     public List<String> getRoads() {
         return roads;
     }

    public void setPoiRegions(List<String> poiRegions) {
         this.poiRegions = poiRegions;
     }
     public List<String> getPoiRegions() {
         return poiRegions;
     }

    public void setSematic_description(String sematic_description) {
         this.sematic_description = sematic_description;
     }
     public String getSematic_description() {
         return sematic_description;
     }

    public void setCityCode(int cityCode) {
         this.cityCode = cityCode;
     }
     public int getCityCode() {
         return cityCode;
     }

}
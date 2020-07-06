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
public class BDMapAddressComponent {

    private String country;
    private int country_code;
    private String country_code_iso;
    private String country_code_iso2;
    private String province;
    private String city;
    private int city_level;
    private String district;
    private String town;
    private String town_code;
    private String adcode;
    private String street;
    private String street_number;
    private String direction;
    private String distance;
    public void setCountry(String country) {
         this.country = country;
     }
     public String getCountry() {
         return country;
     }

    public void setCountry_code(int country_code) {
         this.country_code = country_code;
     }
     public int getCountry_code() {
         return country_code;
     }

    public void setCountry_code_iso(String country_code_iso) {
         this.country_code_iso = country_code_iso;
     }
     public String getCountry_code_iso() {
         return country_code_iso;
     }

    public void setCountry_code_iso2(String country_code_iso2) {
         this.country_code_iso2 = country_code_iso2;
     }
     public String getCountry_code_iso2() {
         return country_code_iso2;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setCity_level(int city_level) {
         this.city_level = city_level;
     }
     public int getCity_level() {
         return city_level;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setTown(String town) {
         this.town = town;
     }
     public String getTown() {
         return town;
     }

    public void setTown_code(String town_code) {
         this.town_code = town_code;
     }
     public String getTown_code() {
         return town_code;
     }

    public void setAdcode(String adcode) {
         this.adcode = adcode;
     }
     public String getAdcode() {
         return adcode;
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

    public void setDirection(String direction) {
         this.direction = direction;
     }
     public String getDirection() {
         return direction;
     }

    public void setDistance(String distance) {
         this.distance = distance;
     }
     public String getDistance() {
         return distance;
     }

}
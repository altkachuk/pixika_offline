package moshimoshi.cyplay.com.doublenavigation.model.business;

import org.parceler.Parcel;

import moshimoshi.cyplay.com.doublenavigation.utils.StringUtils;
import ninja.cyplay.com.apilibrary.models.ModelField;

@Parcel
public class Mail {

    @ModelField(eventLogging = true)
    String address1;

    @ModelField(eventLogging = true)
    String address2;

    @ModelField(eventLogging = true)
    String address3;

    @ModelField(eventLogging = true)
    String address4;

    @ModelField(eventLogging = true)
    String zipcode;

    @ModelField(eventLogging = true)
    String city;

    @ModelField(eventLogging = true)
    String region;

    @ModelField(eventLogging = true)
    String country;

    @ModelField(eventLogging = true)
    String country_ISO_3166_1_alpha_2;

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getAddress4() {
        return address4;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormattedAddress(){
        String result = "";
        if (!StringUtils.isEmpty(address1)){
            result = result + (result.equals("") ? address1 : "\n"+address1);
        }
        if (!StringUtils.isEmpty(address2)){
            result = result + (result.equals("") ? address2 : "\n"+address2);
        }
        if (!StringUtils.isEmpty(address3)){
            result = result + (result.equals("") ? address3 : "\n"+address3);
        }
        if (!StringUtils.isEmpty(address4)){
            result = result + (result.equals("") ? address4 : "\n"+address4);
        }
        String zipCodeCity= (StringUtils.isEmpty(zipcode) ? "":zipcode) + (StringUtils.isEmpty(city) ? "": " " + city);
        if (!StringUtils.isEmpty(zipCodeCity)){
            result = result + (result.equals("") ? zipCodeCity : "\n"+zipCodeCity);
        }
        return result;
    }

    public String getOneLineAddress(){
        String result = "";
        if (!StringUtils.isEmpty(address1)){
            result = result + (result.equals("") ? address1 : " "+address1);
        }
        if (!StringUtils.isEmpty(address2)){
            result = result + (result.equals("") ? address2 : " "+address2);
        }
        if (!StringUtils.isEmpty(address3)){
            result = result + (result.equals("") ? address3 : " "+address3);
        }
        if (!StringUtils.isEmpty(address4)){
            result = result + (result.equals("") ? address4 : " "+address4);
        }
        String zipCodeCity= (StringUtils.isEmpty(zipcode) ? "":zipcode) + (StringUtils.isEmpty(city) ? "": " " + city);
        if (!StringUtils.isEmpty(zipCodeCity)){
            result = result + (result.equals("") ? zipCodeCity : " "+zipCodeCity);
        }
        return result;
    }

    public String getCountryISO2Code(){
        return country_ISO_3166_1_alpha_2;
    }

}

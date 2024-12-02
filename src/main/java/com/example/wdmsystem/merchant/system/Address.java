package com.example.wdmsystem.merchant.system;

public class Address {

    public String address1;
    public String address2;
    public String city;
    public String country;
    public String countryCode;
    public String zipCode;

    public Address(String address1, String address2, String city, String country, String countryCode, String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.zipCode = zipCode;
    }

}

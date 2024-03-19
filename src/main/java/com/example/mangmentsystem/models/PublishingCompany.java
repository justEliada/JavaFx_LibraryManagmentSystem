package com.example.mangmentsystem.models;

public class PublishingCompany {
    private int id;
    private String companyName;
    private String address;
    private String street;

    public PublishingCompany(int id, String companyName, String address, String street) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Publishing{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", address='" + address + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}

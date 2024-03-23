package com.example.theshoppy;

public class Invoice {
    private String customerName;
    private String province;
    private String computerType;
    private String brand;
    private String additional;
    private String peripherals;
    private String totalCost;

    //constructor
    public Invoice(){}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getComputerType() {
        return computerType;
    }

    public void setComputerType(String computerType) {
        this.computerType = computerType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getPeripherals() {
        return peripherals;
    }

    public void setPeripherals(String peripherals) {
        this.peripherals = peripherals;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}

package com.example.CourierApp.data;

import com.example.CourierApp.entity.Company;

import java.util.List;

/**
 * Created by apple on 15/4/5.
 */
public class CompanyData {
    private String status;
    private List<Company> companys;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Company> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
    }
}

package com.example.CourierApp.data;

/**
 * Created by apple on 15/4/6.
 */
public class EmsInfo {
    private String sign_no;
    private String phone;
    private String name;
    private String company;
    private String companypic;
    private String add_time;
    private String sign_time;
    private String store_no;//»õ¼ÜºÅ

    public String getStore_no() {
        return store_no;
    }

    public void setStore_no(String store_no) {
        this.store_no = store_no;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getCompanypic() {
        return companypic;
    }

    public void setCompanypic(String companypic) {
        this.companypic = companypic;
    }

    public String getSign_no() {
        return sign_no;
    }

    public void setSign_no(String sign_no) {
        this.sign_no = sign_no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

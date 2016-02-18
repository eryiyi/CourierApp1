package com.example.CourierApp.data;

import java.util.List;

/**
 * Created by apple on 15/4/6.
 */
public class EmsInfoDATA {
    private String status;
    private List<EmsInfo> datalist;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EmsInfo> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<EmsInfo> datalist) {
        this.datalist = datalist;
    }
}

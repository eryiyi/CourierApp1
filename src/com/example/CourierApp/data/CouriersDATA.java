package com.example.CourierApp.data;

import com.example.CourierApp.entity.Courier;

import java.util.List;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class CouriersDATA extends Data {
    private List<Courier> data;

    public List<Courier> getData() {
        return data;
    }

    public void setData(List<Courier> data) {
        this.data = data;
    }
}

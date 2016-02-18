package com.example.CourierApp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.android.volley.RequestQueue;
import com.example.CourierApp.ui.FragmentActivity;
import com.example.CourierApp.util.StringUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaseFragment extends Fragment {
    private FragmentActivity activity;
    private String accessToken;
    private String uid;
    public SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("university_manage", Context.MODE_PRIVATE);
    }
}

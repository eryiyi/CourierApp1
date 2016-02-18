package com.example.CourierApp.util;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * Created by apple on 15/4/3.
 */
public class CommonUtil {
    private volatile static RequestQueue requestQueue;
    private volatile static Gson gson;

    public static RequestQueue getRequestQueue(Context context){
        if (requestQueue == null){
            synchronized (CommonUtil.class){
                if (requestQueue == null){
                    requestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return requestQueue;
    }

    public static Gson getGson(){
        if (gson == null){
            synchronized (CommonUtil.class){
                if (gson == null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

}

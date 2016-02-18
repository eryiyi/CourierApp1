package com.example.CourierApp.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import com.android.volley.toolbox.ImageLoader;
import com.example.CourierApp.CourierAppApplication;
import com.example.CourierApp.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2015/3/28.
 */
public class BaseActivity  extends FragmentActivity {
    protected CourierAppApplication mKXApplication;
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;
    /**
     * 屏幕的宽度和高度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    public Context mContext;
    public SharedPreferences sp;
    public LayoutInflater inflater;

    public ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mKXApplication = (CourierAppApplication) getApplication();
        /**
         * 获取屏幕宽度和高度
         */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;


        mContext = getApplicationContext();
        sp = getSharedPreferences("courierApp_manager", Context.MODE_PRIVATE);
        inflater = LayoutInflater.from(mContext);

        CourierAppApplication.getTack().addActivity(this);
    }

    protected Context getContext() {
        return mContext;
    }

    //存储sharepreference
    public void save(String key, Object value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, CommonUtil.getGson().toJson(value)).commit();
    }

    public SharedPreferences getSp() {
        if (sp == null) {
            sp = getSharedPreferences("university_manage", Context.MODE_PRIVATE);
        }
        return sp;
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
    private class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

}

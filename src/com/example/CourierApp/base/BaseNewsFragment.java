package com.example.CourierApp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.CourierApp.CourierAppApplication;
import com.example.CourierApp.ui.FragmentActivity;
import com.google.gson.Gson;


public class BaseNewsFragment extends Fragment {
    private FragmentActivity activity;
    public SharedPreferences sp;
    private Context fContext;
    public ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FragmentActivity) this.getActivity();
        sp = getActivity().getSharedPreferences("university_manage", Context.MODE_PRIVATE);
        CourierAppApplication.getTack().addActivity(getActivity());
        CourierAppApplication.getTack().addActivity(getActivity());
        fContext = activity.mContext;
    }

    public Context getfContext() {
        return fContext;
    }
    public class BitmapCache implements ImageLoader.ImageCache {

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

package com.example.CourierApp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.CourierApp.base.ActivityTack;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/3/28.
 */
public class CourierAppApplication  extends Application {
    private static CourierAppApplication instance;
    private static ActivityTack tack = ActivityTack.getInstanse();

    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//头像图片
    public static ExecutorService lxThread = Executors.newFixedThreadPool(20);

    private Gson gson = new Gson();
    public static String adurl;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    public static Bitmap bitmap;
    private RequestQueue mRequestQueue;

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */


    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        initImageLoader(getApplicationContext());
        instance = this;

    }

    public CourierAppApplication() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.photo_failed)
                .showImageForEmptyUri(R.drawable.photo_failed)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.photo_failed)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();                                       // 创建配置过得DisplayImageOption对象

        txOptions = new DisplayImageOptions.Builder()//头像
                .showImageOnLoading(R.drawable.head)
                .showImageForEmptyUri(R.drawable.head)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.head)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型头像
                .build();
    }

    // 单例模式中获取唯一的MyApplication实例
    public static CourierAppApplication getInstance() {
        if (null == instance) {
            instance = new CourierAppApplication();
        }
        return instance;
    }

    public static ActivityTack getTack() {
        return tack;
    }

    /**
     * 初始化图片加载组件ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public Gson getGson() {
        return gson;
    }

}


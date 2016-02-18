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
    public static DisplayImageOptions txOptions;//ͷ��ͼƬ
    public static ExecutorService lxThread = Executors.newFixedThreadPool(20);

    private Gson gson = new Gson();
    public static String adurl;
    ImageLoader imageLoader = ImageLoader.getInstance();//ͼƬ������
    public static Bitmap bitmap;
    private RequestQueue mRequestQueue;

    /**
     * ��ǰ�û�nickname,Ϊ��ƻ�����Ͳ���userid�����ǳ�
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
                .showImageForEmptyUri(R.drawable.photo_failed)    // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.photo_failed)        // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
                .cacheInMemory(true)                           // �������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)                             // �������ص�ͼƬ�Ƿ񻺴����ڴ濨��
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //ͼƬ�Ľ�������
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();                                       // �������ù���DisplayImageOption����

        txOptions = new DisplayImageOptions.Builder()//ͷ��
                .showImageOnLoading(R.drawable.head)
                .showImageForEmptyUri(R.drawable.head)    // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.head)        // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
                .cacheInMemory(true)                           // �������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)                             // �������ص�ͼƬ�Ƿ񻺴����ڴ濨��
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //ͼƬ�Ľ�������ͷ��
                .build();
    }

    // ����ģʽ�л�ȡΨһ��MyApplicationʵ��
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
     * ��ʼ��ͼƬ�������ImageLoader
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


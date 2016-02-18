package com.example.CourierApp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.CourierApp.R;
import com.example.CourierApp.adapter.CourierAdapter;
import com.example.CourierApp.base.BaseNewsFragment;
import com.example.CourierApp.base.InternetURL;
import com.example.CourierApp.data.CouriersDATA;
import com.example.CourierApp.data.EmsInfo;
import com.example.CourierApp.data.EmsInfoDATA;
import com.example.CourierApp.entity.Courier;
import com.example.CourierApp.library.PullToRefreshBase;
import com.example.CourierApp.library.PullToRefreshListView;
import com.example.CourierApp.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFragment extends BaseNewsFragment {
	Activity activity;
	ArrayList<Courier> newsList = new ArrayList<Courier>();
    List<EmsInfo> emsInfoList = new ArrayList<EmsInfo>();
    PullToRefreshListView mListView;
    CourierAdapter mAdapter;
	String typeId;
    String typeName;

	ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
    private TextView item_textview;
    private static boolean IS_REFRESH = true;
    private int pageIndex = 1;
    private String flag;

    public ItemFragment() {
    }

    public ItemFragment(String flag) {
        this.flag = flag;
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
        registerBoradcastReceiver();
        typeName = args != null ? args.getString(Constants.NEWS_TYPEID_NAME) : "";
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_item_fragment, null);
        initView(view);
        item_textview.setText(typeName);
        initData(1);
		return view;
	}

    private void initView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);
        item_textview = (TextView)view.findViewById(R.id.item_textview);
        detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
        mAdapter = new CourierAdapter(emsInfoList, activity);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData(pageIndex);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void initData(final int pageIndex){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.EMS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            EmsInfoDATA data = CommonUtil.getGson().fromJson(s, EmsInfoDATA.class);
                            if ("000".equals(data.getStatus())){
                                if (IS_REFRESH) {
                                    emsInfoList.clear();
                                }
                                emsInfoList.addAll(data.getDatalist());
                                mAdapter.notifyDataSetChanged();
                            }
                            Log.i("Success", s);
                        }else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        mListView.onRefreshComplete();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_id", new SPUtil(getActivity()).getValue(Constants.USER_ID, "user_id"));
                if ("全部".equals(flag)){
                    params.put("status", "0");
                }else if ("未发".equals(flag)){
                    params.put("status", "1");
                }else if("已发".equals(flag)){
                    params.put("status", "2");
                }else if ("超期".equals(flag)){
                    params.put("status", "3");
                }else if("已退".equals(flag)){
                    params.put("status", "4");
                }
                params.put("type", "1");
                params.put("pagesize", "10");
                params.put("page", pageIndex+"");
                params.put("code", new MD5Util().getMD5ofStr("xiaoxiong008"));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        CommonUtil.getRequestQueue(getActivity()).add(request);
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if(action.equals(Constants.SEND_NEWS_DELETE_SUCCESS)){
//                initData();
//            }
        }

    };
    //注册广播
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
//        myIntentFilter.addAction(Constants.SEND_NEWS_DELETE_SUCCESS);//设置下拉按钮的广播事件
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

}

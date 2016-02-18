package com.example.CourierApp.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.example.CourierApp.base.BaseFragment;
import com.example.CourierApp.base.InternetURL;
import com.example.CourierApp.data.CompanyData;
import com.example.CourierApp.data.SucessData;
import com.example.CourierApp.entity.Company;
import com.example.CourierApp.ui.MipcaActivityCapture;
import com.example.CourierApp.ui.VoiceToWord;
import com.example.CourierApp.util.*;
import com.example.CourierApp.widget.CustomerSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发现
 */
public class SendFragment extends BaseFragment implements View.OnClickListener{
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageView send_mobile;//语音识别
    private EditText mResultText;//手机号
    private EditText send_number;//快递单号
//    private EditText send_company;//快递公司
    private EditText send_address;//快递目的地
    private CustomerSpinner companySpinner;
    ArrayAdapter<String> adapter;
    private ArrayList<String> companyList = new ArrayList<String>();
    private List<Company> companyListCompany = new ArrayList<Company>();//查询所有的快递公司
    private String company;//快递公司
    private ImageView send_cam;//相机
//    private ImageView send_incompany;//进入公司
//    private ImageView send_inaddress;//进入目的地选择

    private TextView send_jxsave;//继续保存
    private Button send_save;//保存

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send, null);
        initView(view);
        getCompany();
        return view;
    }
    public void initView(View view){

        mResultText = ((EditText) view.findViewById(R.id.iat_text));
        send_number = (EditText) view.findViewById(R.id.send_number);
//        send_company = (EditText) view.findViewById(R.id.send_company);
//        send_address = (EditText) view.findViewById(R.id.send_address);

        send_cam = (ImageView) view.findViewById(R.id.send_cam);
//        send_incompany = (ImageView) view.findViewById(R.id.send_incompany);
//        send_inaddress = (ImageView) view.findViewById(R.id.send_inaddress);
        send_mobile = (ImageView) view.findViewById(R.id.send_mobile);

        send_jxsave = (TextView) view.findViewById(R.id.send_jxsave);
        send_save = (Button) view.findViewById(R.id.send_save);

        send_mobile.setOnClickListener(this);
        send_save.setOnClickListener(this);
        send_jxsave.setOnClickListener(this);
//        send_incompany.setOnClickListener(this);
//        send_inaddress.setOnClickListener(this);
        send_cam.setOnClickListener(this);

        companySpinner = (CustomerSpinner) view.findViewById(R.id.send_spinner);
        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //点击的哪一个
                company = companyList.get(position);
                Log.i("Tag", "点击的是" + company);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        companyList.add("请选择快递公司");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, companyList);
        companySpinner.setList(companyList);
        companySpinner.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send_mobile://获得语音电话号码
                mResultText.setText(null);// 清空显示内容
                VoiceToWord voice = new VoiceToWord(getActivity(),"5518eb67");
                voice.GetWordFromVoice();
                break;
            case R.id.send_save://保存
                save();
                break;
            case R.id.send_jxsave://继续保存
                send_number.setText("");
                mResultText.setText("");
                break;
//            case R.id.send_inaddress://选择地址
//                break;
            case R.id.send_cam://相机
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    send_number.setText(bundle.getString("result"));
                }
                break;
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Constants.MOBILE_RECORD)){
                String mobileRecord = intent.getExtras().getString(Constants.MOBILE_CONTENT);
                mResultText.setText(mobileRecord);
            }
        }

    };
    //注册广播
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.MOBILE_RECORD);//设置语音识别按钮的广播事件
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private void getCompany(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.COMPANY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        CompanyData data = CommonUtil.getGson().fromJson(s, CompanyData.class);
                        if ("000".equals(data.getStatus())){
                            companyListCompany = data.getCompanys();
                            for (int i=0; i<companyListCompany.size(); i++ ) {
                                companyList.add(companyListCompany.get(i).getName());
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getActivity(), "校验码不正确", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("s", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("error", "error");
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("code", new MD5Util().getMD5ofStr("xiaoxiong008"));
                return params;
            }
        };
        CommonUtil.getRequestQueue(getActivity()).add(request);
    }

    //保存
    private void save(){
        String number = send_number.getText().toString();
        if (StringUtil.isNullOrEmpty(number)){
            Toast.makeText(getActivity(), "请输入快递单号", Toast.LENGTH_SHORT).show();
            return;
        }
        if(number.length()>20){
            Toast.makeText(getActivity(), R.string.receiver_error_onetwo, Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(company) || "请选择快递公司".equals(company)){
            Toast.makeText(getActivity(), R.string.receiver_error_two, Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isNullOrEmpty(mResultText.getText().toString())){
            Toast.makeText(getActivity(), R.string.receiver_error_three, Toast.LENGTH_SHORT).show();
            return;
        }
        if(mResultText.getText().toString().length()!=11){
            Toast.makeText(getActivity(), R.string.receiver_error_four, Toast.LENGTH_SHORT).show();
            return;
        }
        Resources res = getActivity().getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        send_save.setClickable(false);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("SUccess", s);
                        SucessData data = CommonUtil.getGson().fromJson(s, SucessData.class);
                        if (data.getStatus().equals("000")){
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                            send_number.setText("");
                            mResultText.setText("");
                        }
                        if (data.getStatus().equals("021")){
                            Toast.makeText(getActivity(), "该订单已存在", Toast.LENGTH_SHORT).show();
                        }
                        if (data.getStatus().equals("091")){
                            Toast.makeText(getActivity(), "校验码不正确", Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        send_save.setClickable(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("Error", "error");
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        send_save.setClickable(true);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", new SPUtil(getActivity()).getValue(Constants.USER_ID, "user_id"));
                params.put("sign_no", send_number.getText().toString());
                params.put("phone", mResultText.getText().toString());
                params.put("company", company);
                params.put("type", "2");
                params.put("code", new MD5Util().getMD5ofStr("xiaoxiong008"));
                return params;
            }
        };
        CommonUtil.getRequestQueue(getActivity()).add(request);
    }
}

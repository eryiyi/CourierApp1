package com.example.CourierApp.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.CourierApp.entity.Person;
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
public class ReceiverFragment extends BaseFragment implements View.OnClickListener{
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageView send_mobile;//语音识别
    private EditText mResultText;//手机号
    private EditText receive_number;//单号
    private EditText receive_company;//公司
    private EditText receive_name;//xingmign
    private ImageView receive_camrer;//相机
    private ImageView receive_in;//选择公司
    private Button receive_save;//保存
    private TextView receive_andsave;//继续保存
    private CustomerSpinner companySpinner;
    ArrayAdapter<String> adapter;

    private ProgressDialog progressDialog;

    private String number;//单号
    private String company;//快递公司
    private String mobile;//手机号

    private ArrayList<String> companyList = new ArrayList<String>();
    private List<Company> companyListCompany = new ArrayList<Company>();//查询所有的快递公司

    List<Person> lists = new ArrayList<Person>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receive, null);
        initView(view);
        getCompany();
        return view;
    }
    public void initView(View view){
        send_mobile = (ImageView) view.findViewById(R.id.send_mobile);
        mResultText = ((EditText) view.findViewById(R.id.iat_text));
        mResultText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //判断有无此手机号的记录
               Person person = DBHelper.getInstance(getActivity()).findByPhone(mResultText.getText().toString());
                if (person != null){
                    receive_name.setText(person.getName());
                }
            }
        });
        receive_number = ((EditText) view.findViewById(R.id.receive_number));
        receive_name = ((EditText) view.findViewById(R.id.receive_name));
        receive_camrer = ((ImageView) view.findViewById(R.id.receive_camrer));
        receive_save = ((Button) view.findViewById(R.id.receive_save));
        receive_andsave = ((TextView) view.findViewById(R.id.receive_andsave));
        companySpinner = (CustomerSpinner) view.findViewById(R.id.company_spinner);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company = companyList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        companyList.add("请选择快递公司");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, companyList);
        companySpinner.setList(companyList);
        companySpinner.setAdapter(adapter);

        send_mobile.setOnClickListener(this);
        receive_camrer.setOnClickListener(this);
        receive_save.setOnClickListener(this);
        receive_andsave.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send_mobile:
                mResultText.setText(null);// 清空显示内容
                VoiceToWord voice = new VoiceToWord(getActivity(),"5518eb67");
                voice.GetWordFromVoice();
                break;
            case R.id.receive_camrer://相机
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.receive_save://保存
                number = receive_number.getText().toString();
                mobile = mResultText.getText().toString();
                if(StringUtil.isNullOrEmpty(number)){
                    Toast.makeText(getActivity(), R.string.receiver_error_one, Toast.LENGTH_SHORT).show();
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
                if(StringUtil.isNullOrEmpty(mobile)){
                    Toast.makeText(getActivity(), R.string.receiver_error_three, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobile.length()!=11){
                    Toast.makeText(getActivity(), R.string.receiver_error_four, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(receive_name.getText().toString())){
                    Toast.makeText(getActivity(), R.string.receiver_error_five, Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getActivity().getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                receive_save.setClickable(false);
                save();
                break;
            case R.id.receive_andsave://继续保存
                receive_number.setText("");
                mResultText.setText("");
                receive_name.setText("");
                break;
        }
    }
    //保存
    private void save(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("SUccess", s);
                        SucessData data = CommonUtil.getGson().fromJson(s, SucessData.class);
                        if (data.getStatus().equals("000")){
                            Toast.makeText(getActivity(), "添加成功,货架号："+data.getStore_no(), Toast.LENGTH_LONG).show();
                            //添加到本地数据库
                            Person person = new Person();
                            person.setName(receive_name.getText().toString());
                            person.setPhone(mResultText.getText().toString());
                            DBHelper.getInstance(getActivity()).addPerson(person);
                            receive_number.setText("");
                            mResultText.setText("");
                            receive_name.setText("");
                        }
                        if (data.getStatus().equals("021")){
                            Toast.makeText(getActivity(), "该订单已存在", Toast.LENGTH_LONG).show();
                        }
                        if (data.getStatus().equals("091")){
                            Toast.makeText(getActivity(), "校验码不正确", Toast.LENGTH_SHORT).show();
                        }
                        if (data.getStatus().equals("011")){
                            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                        if (data.getStatus().equals("031")){
                            Toast.makeText(getActivity(), "用户不存在", Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        receive_save.setClickable(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Log.i("Error", "error");
                        receive_save.setClickable(true);
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
                params.put("sign_no", number);
                params.put("phone", mobile);
                params.put("company", company);
                params.put("name", receive_name.getText().toString());
                params.put("type", "1");
                params.put("code", new MD5Util().getMD5ofStr("xiaoxiong008"));
                return params;
            }
        };
        CommonUtil.getRequestQueue(getActivity()).add(request);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == getActivity().RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    receive_number.setText(bundle.getString("result"));
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
//                            String[] companys = data.getCompanys().split(",");
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

}

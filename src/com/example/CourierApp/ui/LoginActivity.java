package com.example.CourierApp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.CourierApp.R;
import com.example.CourierApp.base.BaseActivity;
import com.example.CourierApp.base.InternetURL;
import com.example.CourierApp.data.LoginData;
import com.example.CourierApp.util.*;
import com.umeng.update.UmengUpdateAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/28.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private TextView login_activity_login;
    private EditText loginname;
    private EditText login_password;

    private String username;

    private String password;

    private ProgressDialog progressDialog;
    boolean isMobileNet, isWifiNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.login_layout);
        initView();
        if (loginname != null) {
            username = CommonUtil.getGson().fromJson(getSp().getString("username", ""), String.class);
            loginname.setText(username);
        }
        if (login_password!= null) {
            password = CommonUtil.getGson().fromJson(getSp().getString("password", ""), String.class);
            login_password.setText(password);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (loginname != null) {
            loginname.setText(CommonUtil.getGson().fromJson(getSp().getString("username", ""), String.class));
        }
        if (login_password!= null) {
            login_password.setText(CommonUtil.getGson().fromJson(getSp().getString("password", ""), String.class));
        }
    }
    private void initView() {
        login_activity_login = (TextView) this.findViewById(R.id.login_activity_login);
        login_activity_login.setOnClickListener(this);
        loginname = (EditText) this.findViewById(R.id.loginname);
        login_password = (EditText) this.findViewById(R.id.login_password);
    }

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet){
                Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId())
        {
            case R.id.login_activity_login:
                username = loginname.getText().toString();
                password = login_password.getText().toString();
                if(StringUtil.isNullOrEmpty(username)){
                    Toast.makeText(mContext, R.string.login_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(password)){
                    Toast.makeText(mContext, R.string.login_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                login(username, password);
                break;
        }
    }

    private void login(final String username, final String password){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_USER_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        LoginData data = CommonUtil.getGson().fromJson(s, LoginData.class);
                        //状态码为000登陆成功
                        if ("000".equals(data.getStatus())){
                            save("username", username);
                            save("password", password);
                            Intent login = new Intent(LoginActivity.this, FragmentActivity.class);
                            startActivity(login);
                            new SPUtil(mContext).setValue(Constants.USER_ID, data.getUser_id());
                        }
                        if ("011".equals(data.getStatus())){
                            Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Log.i("Error", "请求失败");
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
                params.put("user_name", username);
                params.put("user_pass", password);
                params.put("code", new MD5Util().getMD5ofStr("xiaoxiong008"));
                return params;
            }
        };
        CommonUtil.getRequestQueue(mContext).add(request);
    }

}

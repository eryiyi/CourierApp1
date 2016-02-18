package com.example.CourierApp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.CourierApp.R;
import com.example.CourierApp.base.ActivityTack;
import com.example.CourierApp.base.BaseActivity;
import com.example.CourierApp.fragment.HomeFragment;
import com.example.CourierApp.fragment.ReceiverFragment;
import com.example.CourierApp.fragment.SearchFragment;
import com.example.CourierApp.fragment.SendFragment;

public class FragmentActivity extends BaseActivity implements View.OnClickListener {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private ReceiverFragment receiverFragment;
    private SendFragment sendFragment;
    private SearchFragment searchFragment;
    private HomeFragment homeFragment;

    private RelativeLayout foot_home;
    private RelativeLayout foot_paopao;
    private RelativeLayout foot_find;
    private RelativeLayout foot_mine;


    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        fm = getSupportFragmentManager();
        initView();
        switchFragment(R.id.foot_home);
    }

    private void initView() {
        foot_home = (RelativeLayout) this.findViewById(R.id.foot_home);
        foot_paopao = (RelativeLayout) this.findViewById(R.id.foot_paopao);
        foot_find = (RelativeLayout) this.findViewById(R.id.foot_find);
        foot_mine = (RelativeLayout) this.findViewById(R.id.foot_mine);
        foot_home.setOnClickListener(this);
        foot_mine.setOnClickListener(this);
        foot_find.setOnClickListener(this);
        foot_paopao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switchFragment(v.getId());
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_home:
                if (receiverFragment == null) {
                    receiverFragment = new ReceiverFragment();
                    fragmentTransaction.add(R.id.content_frame, receiverFragment);
                } else {
                    fragmentTransaction.show(receiverFragment);
                }
                break;
            case R.id.foot_paopao:
                if (sendFragment == null) {
                    sendFragment = new SendFragment();
                    fragmentTransaction.add(R.id.content_frame, sendFragment);
                } else {
                    fragmentTransaction.show(sendFragment);
                }

                break;
            case R.id.foot_find:
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    fragmentTransaction.add(R.id.content_frame, searchFragment);
                } else {
                    fragmentTransaction.show(searchFragment);
                }
                break;
            case R.id.foot_mine:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_frame, homeFragment);
                } else {
                    fragmentTransaction.show(homeFragment);
                }

                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (searchFragment != null) {
            ft.hide(searchFragment);
        }
        if (sendFragment != null) {
            ft.hide(sendFragment);
        }
        if (receiverFragment != null) {
            ft.hide(receiverFragment);
        }
    }


    //再摁退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode){
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime){
                Toast.makeText(FragmentActivity.this, R.string.quite, Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                ActivityTack.getInstanse().exit(getContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
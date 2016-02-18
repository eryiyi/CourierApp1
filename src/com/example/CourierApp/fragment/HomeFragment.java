package com.example.CourierApp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.CourierApp.R;
import com.example.CourierApp.adapter.NewsFragmentPagerAdapter;
import com.example.CourierApp.base.BaseFragment;
import com.example.CourierApp.util.BaseTools;
import com.example.CourierApp.util.Constants;
import com.example.CourierApp.widget.ColumnHorizontalScrollView;

import java.util.ArrayList;


/**
 * 发现
 */
public class HomeFragment extends BaseFragment implements  View.OnClickListener{
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ImageView button_more_columns;
    private int columnSelectIndex = 0;
    public ImageView shade_left;
    public ImageView shade_right;
    private int mScreenWidth = 0;
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> newsClassify = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsClassify.add("全部");
        newsClassify.add("未发");
        newsClassify.add("已发");
        newsClassify.add("超期");
        newsClassify.add("已退");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        mScreenWidth = BaseTools.getWindowsWidth(getActivity());
        mItemWidth = mScreenWidth / 6;
        initView(view);
        initTabColumn();
        initFragment();
        return view;
    }

    private void initView(View view) {
        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)view.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
    }
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  5;
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 12;
            params.rightMargin = 12;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setTextAppearance(getActivity(), R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.button_switch);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(8, 8, 8, 8);
            columnTextView.setId(i);
            columnTextView.setText(newsClassify.get(i));
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
//                    Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i ,params);
        }
    }

    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    private void initFragment() {
        int count =  newsClassify.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString(Constants.NEWS_TYPEID_NAME, newsClassify.get(i));
            ItemFragment newfragment = new ItemFragment(newsClassify.get(i));
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }
    /**
     *  ViewPager�������
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }
}

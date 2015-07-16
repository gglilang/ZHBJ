package com.lang.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lang.zhbj.MainActivity;
import com.lang.zhbj.R;
import com.lang.zhbj.base.BaseMenuDetailPager;
import com.lang.zhbj.base.TabDetailPager;
import com.lang.zhbj.domain.NewsData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详细页-新闻
 * Created by Lang on 2015/7/13.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ViewPager vp_menu_detail;

    private ArrayList<TabDetailPager> mPagerList;

    ArrayList<NewsData.NewsTabData> mNewsTabData;   // 页签数据
    private TabPageIndicator indicator;

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsData.NewsTabData> children) {
        super(mActivity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        vp_menu_detail = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        ViewUtils.inject(this, view);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        // 设置ViewPager的监听事件时，如果已经判定Indicator，则必须监听Indicator
        indicator.setOnPageChangeListener(this);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        mPagerList = new ArrayList<>();

        for(int i = 0; i < mNewsTabData.size(); i++){
            mPagerList.add(new TabDetailPager(mActivity, mNewsTabData.get(i)));
        }

        vp_menu_detail.setAdapter(new MenuDetailAdapter());

        // indicator必须在ViewPager设置Adapter后才能设置
        indicator.setViewPager(vp_menu_detail);
    }



    class MenuDetailAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.btn_next)
    public void nextPager(View view){
        int currentItem = vp_menu_detail.getCurrentItem();
        vp_menu_detail.setCurrentItem(++currentItem);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(position == 0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

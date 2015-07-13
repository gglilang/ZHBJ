package com.lang.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lang.zhbj.R;
import com.lang.zhbj.base.BaseMenuDetailPager;
import com.lang.zhbj.base.TabDetailPager;
import com.lang.zhbj.domain.NewsData;

import java.util.ArrayList;

/**
 * 菜单详细页-新闻
 * Created by Lang on 2015/7/13.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager vp_menu_detail;

    private ArrayList<TabDetailPager> mPagerList;

    ArrayList<NewsData.NewsTabData> mNewsTabData;   // 页签数据

    public NewsMenuDetailPager(Activity mActivity, ArrayList<NewsData.NewsTabData> children) {
        super(mActivity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        vp_menu_detail = (ViewPager) view.findViewById(R.id.vp_menu_detail);
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
    }

    class MenuDetailAdapter extends PagerAdapter{

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
}

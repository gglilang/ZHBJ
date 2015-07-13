package com.lang.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lang.zhbj.MainActivity;
import com.lang.zhbj.base.BaseMenuDetailPager;
import com.lang.zhbj.base.BasePager;
import com.lang.zhbj.base.menudetail.InteractMenuDetailPager;
import com.lang.zhbj.base.menudetail.NewsMenuDetailPager;
import com.lang.zhbj.base.menudetail.PhotoMenuDetailPager;
import com.lang.zhbj.base.menudetail.TopicMenuDetailPager;
import com.lang.zhbj.domain.NewsData;
import com.lang.zhbj.fragment.LeftMenuFragment;
import com.lang.zhbj.global.GlobalContacts;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 新闻中心实现
 * Created by Lang on 2015/7/12.
 */
public class NewsCenterPager extends BasePager {
    private NewsData mNewsData;

    public NewsCenterPager(Activity mActivity) {
        super(mActivity);
    }

    private ArrayList<BaseMenuDetailPager> mPager;  // 四个菜单详细页

    @Override
    public void initData() {
        super.initData();
        setSlidingMenuEnable(true);
        getDataFromServer();

    }



    /**
     * 从服务器获取地址
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();

        // 使用xutils发送请求
        utils.send(HttpRequest.HttpMethod.GET, GlobalContacts.CATEGORIES_URL, new RequestCallBack<String>() {
            // 访问成功
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果：" + result);

                parseData(result);
            }

            // 访问失败
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();

        mNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果：" + mNewsData);

        // 刷新侧边栏的数据
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        // 准备4个菜单详细页
        mPager = new ArrayList<>();
        mPager.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        mPager.add(new TopicMenuDetailPager(mActivity));
        mPager.add(new PhotoMenuDetailPager(mActivity));
        mPager.add(new InteractMenuDetailPager(mActivity));

        setCurrentMenuDetailPager(0);

    }

    /**
     * 设置当前菜单详细页
     * @param position
     */
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager = mPager.get(position);
        fl_content.removeAllViews();    // 获取当前要显示的菜单详细页
        fl_content.addView(pager.mRootView);    // 将菜单详细页的布局设置给帧布局

        String title = mNewsData.data.get(position).title;  // 设置标题
        tv_title.setText(title);

        pager.initData();
    }
}

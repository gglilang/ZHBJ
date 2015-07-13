package com.lang.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏菜单点击后的视图的基类
 * Created by Lang on 2015/7/13.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;

    public View mRootView;  // 根布局对象

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initViews();
    }

    /**
     * 初始化界面
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData(){

    }
}

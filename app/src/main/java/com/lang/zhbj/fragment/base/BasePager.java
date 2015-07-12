package com.lang.zhbj.fragment.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lang.zhbj.R;

/**
 * Created by Lang on 2015/7/12.
 */
public class BasePager {

    public Activity mActivity;
    public View mRootView;  // 布局对象

    public TextView tv_title;   // 标题对象
    public FrameLayout fl_content;  // 内容
    public ImageButton btn_menu;    // 菜单按钮

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews(){
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        fl_content = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btn_menu = (ImageButton) mRootView.findViewById(R.id.btn_menu);

    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
}

package com.lang.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lang.zhbj.MainActivity;
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

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        mainUi.getSlidingMenu().toggle();
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }

    /**
     * 设置是否开启侧边栏
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            btn_menu.setVisibility(View.VISIBLE);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            btn_menu.setVisibility(View.INVISIBLE);
        }
    }
}

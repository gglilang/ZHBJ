package com.lang.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lang on 2015/7/13.
 */
public class TopNewsViewPager extends ViewPager {
    private int startX;
    private int startY;

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopNewsViewPager(Context context) {
        super(context);
    }

    /**
     * 事件分发，请求父控件及祖宗控件是否要拦截事件
     * 1.右划，而且是第一个页面，需要父亲控件拦截
     * 2.左划，而且是最后一个页面，需要父控件拦截
     * 3.上下滑动，需要父控件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 请求父控不要件拦截事件,为了保证ACTION_MOVE事件能够触发
                getParent().requestDisallowInterceptTouchEvent(true);

                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                // 手指左右滑动
                if(Math.abs(endX - startX) > Math.abs(endY - startY)){
                    if(startX > endX){  // 左划
                        // 滑到viewpager尽头
                        if(getCurrentItem() == getAdapter().getCount() - 1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {    // 右划
                        // 滑到viewpager的开始
                        if(getCurrentItem() == 0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else { // 手指上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

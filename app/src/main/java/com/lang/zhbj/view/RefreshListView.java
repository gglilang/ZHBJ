package com.lang.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lang.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lang on 2015/7/16.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener {

    private static final int STATE_PULL_REFRESH = 0;    // 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;    // 松开刷新
    private static final int STATE_REFRESHING = 2;    // 正在刷新

    // 当前刷新状态
    private int mCurrentState = STATE_PULL_REFRESH;

    // 记录按下的起始位置
    private int startY = -1;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private TextView tv_title;
    private TextView tv_time;
    private ImageView iv_arr;
    private ProgressBar pb_progress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View mFootView;
    private int mFooterViewHeight;

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }


    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        initArrowAnim();

        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        iv_arr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pb_progress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);

        // 隐藏下拉刷新
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        tv_time.setText("最后刷新时间：" + getCurrentTime());


    }

    /**
     * 初始化脚布局
     */
    private void initFooterView(){
        mFootView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFootView);

        mFootView.measure(0, 0);
        mFooterViewHeight = mFootView.getMeasuredHeight();

        mFootView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }

                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                int endY = (int) ev.getRawY();
                int dy = endY - startY; // 移动偏移量

                // 只有下拉并且当前是第一个item，才允许刷新
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    int padding = dy - mHeaderViewHeight;
                    // 设置当前padding
                    mHeaderView.setPadding(0, padding, 0, 0);
                    // 状态改为松开刷新
                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();

                        // 状态改为下拉刷新
                    } else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;

                if (mCurrentState == STATE_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    mHeaderView.setPadding(0, 0, 0, 0);
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    refreshState();
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                tv_title.setText("下拉刷新");
                iv_arr.setVisibility(VISIBLE);
                pb_progress.setVisibility(INVISIBLE);
                iv_arr.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tv_title.setText("松开刷新");
                iv_arr.setVisibility(VISIBLE);
                pb_progress.setVisibility(INVISIBLE);
                iv_arr.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tv_title.setText("正在刷新");
                iv_arr.clearAnimation();
                iv_arr.setVisibility(INVISIBLE);
                pb_progress.setVisibility(VISIBLE);
                if(mListener != null){
                    mListener.onRefresh();
                }
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向上动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }

    // 下拉刷新接口
    public interface OnRefreshListener{
        void onRefresh();

        void onLoadMore();
    }

    public void onRefreshComplete(boolean success){
        if(isLoadingMore){
            mFootView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLoadingMore = false;
        } else {


            if (success) {
                mCurrentState = STATE_PULL_REFRESH;
                tv_title.setText("下拉刷新");
                iv_arr.setVisibility(VISIBLE);
                pb_progress.setVisibility(INVISIBLE);

                mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                tv_time.setText("最后刷新时间：" + getCurrentTime());
            }
        }
    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private boolean isLoadingMore;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING){
            if(getLastVisiblePosition() == getCount() - 1 && !isLoadingMore){
                System.out.println("到底了。。。");
                mFootView.setPadding(0, 0, 0, 0);

                setSelection(getCount());   // 改变listview显示位置
                isLoadingMore = true;
                mListener.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    OnItemClickListener mItemClickCListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickCListener = listener;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mItemClickCListener != null){
            mItemClickCListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
        }
    }


}

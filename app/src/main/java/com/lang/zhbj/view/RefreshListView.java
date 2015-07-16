package com.lang.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.lang.zhbj.R;

/**
 * Created by Lang on 2015/7/16.
 */
public class RefreshListView extends ListView{
    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }



    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();

    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();

    }

    /**
     * 初始化布局
     */
    private void initHeaderView() {
        View mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);
    }
}

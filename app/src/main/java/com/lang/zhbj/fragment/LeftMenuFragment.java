package com.lang.zhbj.fragment;

import android.view.View;

import com.lang.zhbj.R;

/**
 * Created by Lang on 2015/7/11.
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }
}

package com.lang.zhbj.fragment;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lang.zhbj.R;

/**
 * Created by Lang on 2015/7/11.
 */
public class ContentFragment extends BaseFragment {

    private RadioGroup rb_group;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        return view;
    }
}

package com.lang.zhbj.fragment;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lang.zhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Lang on 2015/7/11.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rg_group;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view);   // 注入view和事件
        return view;
    }

    @Override
    public void initData() {
        rg_group.check(R.id.rb_home);   // 默认勾选页
    }
}

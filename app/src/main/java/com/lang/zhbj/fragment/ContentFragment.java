package com.lang.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lang.zhbj.R;
import com.lang.zhbj.fragment.base.BasePager;
import com.lang.zhbj.fragment.base.impl.GovAffairsPager;
import com.lang.zhbj.fragment.base.impl.HomePager;
import com.lang.zhbj.fragment.base.impl.NewsCenterPager;
import com.lang.zhbj.fragment.base.impl.SettingPager;
import com.lang.zhbj.fragment.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lang on 2015/7/11.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_group)
    private RadioGroup rg_group;

    @ViewInject(R.id.vp_content)
    private ViewPager vp_content;

    private List<BasePager> mPagerList;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view);   // 注入view和事件

        return view;
    }

    @Override
    public void initData() {
        rg_group.check(R.id.rb_home);   // 默认勾选页

        // 初始5个子页面
        mPagerList = new ArrayList<>();
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        vp_content.setAdapter(new ContentAdapter());
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPagerList.get(position).mRootView);
            mPagerList.get(position).initData();
            return mPagerList.get(position).mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

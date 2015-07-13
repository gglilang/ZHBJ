package com.lang.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lang.zhbj.R;
import com.lang.zhbj.base.BasePager;
import com.lang.zhbj.base.impl.GovAffairsPager;
import com.lang.zhbj.base.impl.HomePager;
import com.lang.zhbj.base.impl.NewsCenterPager;
import com.lang.zhbj.base.impl.SettingPager;
import com.lang.zhbj.base.impl.SmartServicePager;
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

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0, false);    // 禁止滑动动画
                        break;
                    case R.id.rb_newscenter:
                        vp_content.setCurrentItem(1, false);    // 禁止滑动动画
                        break;
                    case R.id.rb_smartservice:
                        vp_content.setCurrentItem(2, false);    // 禁止滑动动画
                        break;
                    case R.id.rb_govaffairs:
                        vp_content.setCurrentItem(3, false);    // 禁止滑动动画
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4, false);    // 禁止滑动动画
                        break;
                }
            }
        });

        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 页面被选中
            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerList.get(0).initData();
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
//            mPagerList.get(position).initData();  // 不能在这里初始化，viewPager会预加载数据
            return mPagerList.get(position).mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagerList.get(1);
    }
}

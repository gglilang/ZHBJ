package com.lang.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lang.zhbj.utils.DensityUtil;
import com.lang.zhbj.utils.PrefUtil;

import java.util.ArrayList;


public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private final static int[] mImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_point_group;
    private View view_red_point;
    private Button btn_start;
    private int guidePointWidth;    // 小圆点之间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        view_red_point = findViewById(R.id.view_red_point);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtil.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        initViews();

        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageListener());
    }

    /**
     * 初始化界面
     */
    private void initViews(){
        mImageViewList = new ArrayList<>();

        for(int i = 0; i < mImageIds.length; i++){
            // 添加引导页的图片
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);

            // 添加引导页的小圆点
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this,10));
            if(i > 0){
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }
            point.setLayoutParams(params);

            ll_point_group.addView(point);

            ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    guidePointWidth = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
                    ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }

    }

    /**
     * ViewPager的适配器
     */
    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * viewPage改变的监听事件，用来移动红色小圆点
     */
    private class GuidePageListener implements ViewPager.OnPageChangeListener {
        // 页面滑动时
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int len = (int) (guidePointWidth * positionOffset) + position * guidePointWidth;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_red_point.getLayoutParams();
            params.leftMargin = len;
            view_red_point.setLayoutParams(params);
        }

        // 页面被选中
        @Override
        public void onPageSelected(int position) {
            if(position == mImageViewList.size() - 1){
                btn_start.setVisibility(View.VISIBLE);
            } else {
                btn_start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

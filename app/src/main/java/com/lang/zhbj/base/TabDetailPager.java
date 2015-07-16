package com.lang.zhbj.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lang.zhbj.R;
import com.lang.zhbj.domain.NewsData;
import com.lang.zhbj.domain.TabData;
import com.lang.zhbj.global.GlobalContacts;
import com.lang.zhbj.view.RefreshListView;
import com.lang.zhbj.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 页签详细页
 * Created by Lang on 2015/7/13.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    NewsData.NewsTabData mTabData;

    private String mUrl;
    private TabData mTabDetailData;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.vp_news)
    private TopNewsViewPager vp_news;

    // 头条新闻列表
    private ArrayList<TabData.TopNewsData> topnewsList;

    // 头条新闻指示器
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    // 新闻列表
    @ViewInject(R.id.lv_list)
    private RefreshListView lv_list;
    private ArrayList<TabData.TabNewsData> mNewsList;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData newsTabData) {
        super(mActivity);
        mTabData = newsTabData;
        mUrl = GlobalContacts.SERVER_URL + newsTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        lv_list.addHeaderView(headerView);
//        vp_news.setOnPageChangeListener(this);

        // 隐藏下拉刷新
        headerView.measure(0, 0);
        int measuredHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0,measuredHeight, 0, 0);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果：" + result);

                parseData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("解析结果" + mTabDetailData);

        topnewsList = mTabDetailData.data.topnews;

        mNewsList = mTabDetailData.data.news;

        // 设置一开始的头条新闻标题
        tv_title.setText(topnewsList.get(0).title);

        if(topnewsList != null) {
            vp_news.setAdapter(new TopNewsAdapter());
            // 绑定viewpager
            mIndicator.setViewPager(vp_news);
            mIndicator.setSnap(true);   // 快照显示
            mIndicator.setOnPageChangeListener(this);
            mIndicator.onPageSelected(0);
        }

        if(mNewsList != null) {
            // 填充新闻列表数据
            NewsAdapter mNewsAdapter = new NewsAdapter();
            lv_list.setAdapter(mNewsAdapter);
        }
    }


    class TopNewsAdapter extends PagerAdapter {
        private final BitmapUtils bitmapUtils;

        public TopNewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setImageResource(R.mipmap.news_pic_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); // 基于控件大小填充图片
            bitmapUtils.display(imageView, topnewsList.get(position).topimage); // 传递ImageView对象和地址
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 新闻列表适配器
     */
    class NewsAdapter extends BaseAdapter{
        private final BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);
            viewHolder.tv_title.setText(item.title);
            viewHolder.tv_data.setText(item.pubdate);
            utils.display(viewHolder.iv_pic, item.listimage);

            return convertView;
        }
    }

    private static class ViewHolder{
        ImageView iv_pic;
        TextView tv_title;
        TextView tv_data;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 设置头条新闻的标题
        tv_title.setText(topnewsList.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

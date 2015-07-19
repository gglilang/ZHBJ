package com.lang.zhbj.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lang.zhbj.NewDetailActivity;
import com.lang.zhbj.R;
import com.lang.zhbj.domain.NewsData;
import com.lang.zhbj.domain.TabData;
import com.lang.zhbj.global.GlobalContacts;
import com.lang.zhbj.utils.CacheUtils;
import com.lang.zhbj.utils.PrefUtil;
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

    private String mMoreUrl;    // 更多页面的地址
    private NewsAdapter mNewsAdapter;   // 新闻列表适配器

    private Handler mHandler;   // 轮播条handler

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

        lv_list.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if(mMoreUrl != null){
                    getMoreDataFromServer();
                } else {
                    lv_list.onRefreshComplete(false);
                }
            }
        });

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("点击了：" + position);
                String read_ids = PrefUtil.getString(mActivity, "read_ids", "");
                String ids = mNewsList.get(position).id;
                if (!read_ids.contains(ids)) {
                    read_ids = read_ids + ids + ",";
                    PrefUtil.setString(mActivity, "read_ids", read_ids);
                    // 实现页面局部刷新
                    changeReadState(view);
                }

                // 跳转到新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }


    /**
     * 改变已读新闻的状态
     * @param view
     */
    private void changeReadState(View view){
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setTextColor(Color.GRAY);
    }

    @Override
    public void initData() {
        super.initData();

        String cache = CacheUtils.getCache(mActivity, mUrl);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache, false);
        }
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

                parseData(result, false);
                lv_list.onRefreshComplete(true);

                // 设置页面缓存
                CacheUtils.setCache(mActivity, mUrl, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                lv_list.onRefreshComplete(false);
            }
        });
    }

    /**
     * 从服务器获取下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果：" + result);

                parseData(result, true);
                lv_list.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                lv_list.onRefreshComplete(false  );
            }
        });
    }

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("解析结果" + mTabDetailData);

        String more = mTabDetailData.data.more;
        if(!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalContacts.SERVER_URL + more;
        }else {
            mMoreUrl = null;
        }

        if(!isMore){
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
                vp_news.setCurrentItem(0);
            }

            if(mNewsList != null) {
                // 填充新闻列表数据
                mNewsAdapter = new NewsAdapter();
                lv_list.setAdapter(mNewsAdapter);
            }

            if(mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        int currentItem = vp_news.getCurrentItem();

                        if(currentItem >= topnewsList.size() - 1){
                            currentItem = 0;
                        }else {
                            currentItem++;
                        }
                        vp_news.setCurrentItem(currentItem);
                        // 循环轮播条
                        sendEmptyMessageDelayed(0, 3000);
                    }
                };
                // 延时3秒启动轮播条
                mHandler.sendEmptyMessageDelayed(0, 3000);
            }

        } else {
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();
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

            String read_ids = PrefUtil.getString(mActivity, "read_ids", "");
            if(read_ids.contains(getItem(position).id)){
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

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

package com.lang.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lang.zhbj.R;
import com.lang.zhbj.base.BaseMenuDetailPager;
import com.lang.zhbj.domain.PhotosData;
import com.lang.zhbj.global.GlobalContacts;
import com.lang.zhbj.utils.CacheUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 菜单详细页-组图
 * Created by Lang on 2015/7/13.
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    private ListView lv_photo;
    private GridView gv_photo;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotoAdapter mAdapter;
    private ImageButton btn_photo;

    public PhotoMenuDetailPager(Activity mActivity, ImageButton btn_photo) {
        super(mActivity);
        this.btn_photo = btn_photo;

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
            }
        });

    }

    private boolean isListDisplay = true;   // 是否是列表展示
    /**
     * 改变展现方式
     */
    private void changeDisplay() {
        if(isListDisplay){
            btn_photo.setImageResource(R.mipmap.icon_pic_list_type);
            lv_photo.setVisibility(View.GONE);
            gv_photo.setVisibility(View.VISIBLE);
        } else {
            btn_photo.setImageResource(R.mipmap.icon_pic_grid_type);
            lv_photo.setVisibility(View.VISIBLE);
            gv_photo.setVisibility(View.GONE);
        }
        isListDisplay = !isListDisplay;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        lv_photo = (ListView) view.findViewById(R.id.lv_photo);
        gv_photo = (GridView) view.findViewById(R.id.gv_photo);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        String cache = CacheUtils.getCache(mActivity, GlobalContacts.PHOTOS_URL);
        if(!TextUtils.isEmpty(cache)){

        }

        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalContacts.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果：" + result);

                parseData(result);

                // 设置缓存
                CacheUtils.setCache(mActivity, GlobalContacts.PHOTOS_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        PhotosData photosData = gson.fromJson(result, PhotosData.class);
        mPhotoList = photosData.data.news;  // 获取组图列表集合

        if(mPhotoList != null) {
            mAdapter = new PhotoAdapter();

            lv_photo.setAdapter(mAdapter);
            gv_photo.setAdapter(mAdapter);
        }

    }

    class PhotoAdapter extends BaseAdapter{

        private BitmapUtils bitmapUtils;

        public PhotoAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotosData.PhotoInfo getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder = new ViewHolder();
                holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotosData.PhotoInfo item = getItem(position);
            holder.tv_title.setText(item.title);
            bitmapUtils.display(holder.iv_pic, item.listimage);
            return convertView;
        }
    }

    private static class ViewHolder{
        ImageView iv_pic;
        TextView tv_title;
    }
}

package com.lang.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lang.zhbj.MainActivity;
import com.lang.zhbj.R;
import com.lang.zhbj.domain.NewsData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Lang on 2015/7/11.
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lv_list;   // 侧边栏菜单列表
    private ArrayList<NewsData.NewsMenuData> mMenuList;

    private int mCurrentPos;    // 当前被点击的菜单项
    private MenuAdapter mAdapter;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();
                setCurrentMenuDetailPager(position);

                toggleSlidingMenu();    // 隐藏侧边栏
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        mainUi.getSlidingMenu().toggle();
    }

    /**
     * 设置当前菜单详细页
     * @param position
     */
    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        ContentFragment fragment = mainUi.getContentFragment();
        fragment.getNewsCenterPager().setCurrentMenuDetailPager(position);
    }

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            NewsData.NewsMenuData newsMenuData = mMenuList.get(position);
            tv_title.setText(newsMenuData.title);

            if(mCurrentPos == position) {   // 判断当前绘制的view是否被选中
                // 显示红色
                tv_title.setEnabled(true);
            } else {
                // 显示白色
                tv_title.setEnabled(false);
            }
            return view;
        }
    }

    public void setMenuData(NewsData data){
        System.out.println("拿到侧边栏数据" + data);
        mMenuList = data.data;
        mAdapter = new MenuAdapter();
        lv_list.setAdapter(mAdapter);
    }
}

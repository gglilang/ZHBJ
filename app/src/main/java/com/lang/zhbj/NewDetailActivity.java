package com.lang.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Lang on 2015/7/17.
 */
public class NewDetailActivity extends Activity implements View.OnClickListener {

    private ImageButton btn_back;
    private ImageButton btn_size;
    private ImageButton btn_share;
    private WebView wv_web;
    private ProgressBar pb_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_size = (ImageButton) findViewById(R.id.btn_size);
        btn_share = (ImageButton) findViewById(R.id.btn_share);
        wv_web = (WebView) findViewById(R.id.wv_web);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);

        btn_back.setOnClickListener(this);
        btn_size.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        String url = getIntent().getStringExtra("url");
        WebSettings settings = wv_web.getSettings();
        settings.setJavaScriptEnabled(true);    // 设置支持js
        settings.setBuiltInZoomControls(true);  // 显示缩放按钮
        settings.setUseWideViewPort(true);  // 支持双击缩放

        wv_web.setWebViewClient(new WebViewClient() {
            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_progress.setVisibility(View.VISIBLE);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_progress.setVisibility(View.GONE);
            }

            // 所有跳转的链接都会在此方法中回调
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wv_web.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        wv_web.setWebChromeClient(new WebChromeClient() {
            // 进度条发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("加载中：" + newProgress);
            }

            // 获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("网页标题：" + title);
            }
        });

        wv_web.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }
    }

    private int mCurrentChooseItem = 2;
    private int mTempChooseItem = 2;

    /**
     * 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        builder.setSingleChoiceItems(items, mCurrentChooseItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempChooseItem = which;
            }
        });

        final WebSettings settings = wv_web.getSettings();
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mTempChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentChooseItem = mTempChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}

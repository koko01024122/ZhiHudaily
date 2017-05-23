package com.example.administrator.zhihudaily.UI.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.zhihudaily.R;
import com.example.administrator.zhihudaily.UI.Adapter.InfoListAdapter;
import com.example.administrator.zhihudaily.UI.Bean.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.bingoogolapple.bgabanner.BGABanner;

public class HomeActivity extends BaseActivity {
    private RecyclerView mInfoList;//用于显示的列表

    private ArrayList<Item> mDatas;//用于储存数据

    private InfoListAdapter adapter;//适配器

    private int otherdate=0;//从今日算起，倒数第几天 eg:昨天 就是1 前天就是 2

    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();
        initView();
        initBanner();
    }

    private void initView() {

        setTitle("首页", 1);
        mInfoList= (RecyclerView) findViewById(R.id.infolist);
        mInfoList.setLayoutManager(new LinearLayoutManager(this));
        adapter=new InfoListAdapter(mDatas,HomeActivity.this);
        mInfoList.setAdapter(adapter);

    }

    private void  initDate(){
        mDatas=new ArrayList<>();
        getInfoFromNet();
    }

    private void getInfoFromNet(){
        //获取网络数据
        mQueue = Volley.newRequestQueue(this);
        String url=null;
        if (otherdate==0){
            url="http://news-at.zhihu.com/api/4/news/latest";
        }else {
           url= "http://news.at.zhihu.com/api/4/news/before/" + getDate();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray list = null;
                    try {
                        list = response.getJSONArray("stories");
                        //获取返回数据的内容
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);

                        JSONArray images = item.getJSONArray("images");
                        Item listItem = new Item();
                        //创建list中的每一个对象，并设置数据
                        listItem.setTitle(item.getString("title"));
                        listItem.setImgurl(images.getString(0));
                        listItem.setDate(getDate());
                        listItem.setId(item.getString("id"));
                        mDatas.add(listItem);
                    }
                    adapter.notifyDataSetChanged();//通知适配器 刷新数据啦 啊喂
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("碰到了一点问题");
            }
        });
        mQueue.add(jsonObjectRequest);
    }



    private String getDate(){
            //获取当前需要加载的数据的日期
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -otherdate-1);//otherdate天前的日子

            String date = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
            //将日期转化为20170520这样的格式
            return date;

    }

    private ArrayList<Item> bannerList;//banner控件

    private ArrayList<String> titles;//存放banner中的标题

    private ArrayList<String> images;//存放banner中的图片

    private ArrayList<String> ids;//存放每一项的id

    private void initBanner() {
        //初始化banner
        titles=new ArrayList<>();
        ids=new ArrayList<>();
        images=new ArrayList<>();

        bannerList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://news-at.zhihu.com/api/4/news/latest", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //解析banner中的数据
                    JSONArray topinfos = response.getJSONArray("top_stories");
                    Log.d("TAG", "onResponse: "+topinfos);
                    for (int i = 0; i < topinfos.length(); i++) {
                        JSONObject item = topinfos.getJSONObject(i);
                        Item item1 = new Item();
                        item1.setImgurl(item.getString("image"));
                        item1.setTitle(item.getString("title"));
                        item1.setId(item.getString("id"));
                        bannerList.add(item1);
                        titles.add(item1.getTitle());
                        images.add(item1.getImgurl());
                        ids.add(item1.getId());
                    }


                    setHeader(mInfoList, images, titles, ids);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);


    }

    private void setHeader(RecyclerView view, ArrayList<String> urls, ArrayList<String> titles, final ArrayList<String> ids) {
        View header = LayoutInflater.from(this).inflate(R.layout.headview, view, false);
        //找到banner所在的布局
        BGABanner banner = (BGABanner) header.findViewById(R.id.banner);
        //绑定banner
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {


            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(HomeActivity.this)
                        .load(model)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
             //此处可设置banner子项的点击事件

            }
        });
        banner.setData(urls, titles);
        adapter.setHeadView(header);//向适配器中添加banner
    }
}

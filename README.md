# ZhiHudaily
---
title: 手把手教你仿一个知乎日报Android客户端（三）主页面设计


---

各位朋友，从本篇文章和开始，手把手教你仿一个Android客户端就要正式开始探究怎样实现我们前面的那些需求了。在此开发我们将会使用git作为版本控制工具，并且将代码托管到[github][1]，好啦，废话少说，咱们开工。


# 一、创建项目

怎么创建项目大家应该都知道哈，我就不赘述了，在这里我的项目名为 “ZhiHudaily”,简单的翻译下就是知乎日报。
支持最低的SDK版本为 API 19也就是android 4.4
紧接着创建一个HomeActivity（MainActivity改名来的）,顾名思义就是今天我们的主角— 当当当当 —Home页

# 二、配置CVS
具体的配置内容这里就不讲啦，各位可以参考下[这篇文章][2]




# 三、正式开始

依照我个人的习惯呢，在正式开始编写业务代码前有几件事情要做

1.编写BaseActivity
2.自己写一个Toolbar
3.更换默认主题

首先创建一个BaseActivit,记得在AndroidManifest.xml中注册哦

BaseActivity中主要是对Toolba的初始化以及对toolbar的左上角菜单按钮以及back按钮
## 重要的 BaseActivity代码
``` java

public class BaseActivity extends AppCompatActivity{
    public Toolbar toolbar;
    /**
     *
     * @param title 标题栏标题
     * @param type  标题类型，1为带菜单栏的标题栏，2为带back键的标题栏
     */
    public void setTitle(String title,int type){

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);//标题字体颜色
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);//设置为actionbar
        switch (type){
            case 1:
                toolbar.setNavigationIcon(R.drawable.menu);
                break;
            case 2:
                toolbar.setNavigationIcon(R.drawable.back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        }


    }


}

```
如果你愿意的话，甚至可以把Toast直接放到BaseActivity中单独写作一个方法，当要用到的时候，直接一行 showToast("");就搞定了


## 简单的 Toolbar的代码
``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
<android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:background="@color/colorPrimary"
    android:layout_height="?attr/actionBarSize"/>
</LinearLayout>
```

我们创建好的项目在默认状态下会使用DarkActionBar(layout上面的一个蓝条)，因为我们要用自己定义的toolbar，所以我们
## 修改style.xml
``` xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
```
改为
``` xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
```

此时我们可以对home页进行编辑，首先插入事先写好的toolbar，然后在activity中进行初始化以及设定

## 枯燥的 layout代码
```xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.administrator.zhihudaily.HomeActivity">
<include layout="@layout/toolbar"/>


</RelativeLayout>

```
## 枯燥的 HomeActivity代码
```java
public class HomeActivity extends BaseActivity {
//记得，所有Activity都继承自BaseActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

	//初始化布局
    private void initView(){
	
        setTitle("首页",1);//设置标题内容，该方法继承自父类，所以再写一次
    }
}

```
然后来看看样子

![首页](http://i4.buimg.com/591309/027b39ccc2280314t.jpg)



# 添加开源库
这个项目中我们将会用到BGABanner库，以及v7包中的cardview,和Recycleview，以及图片加载库Gilde，网络访问库 Volley，Volley版本太多，所以我随意找了一个
在build.gradle中添加以下依赖
>compile 'com.android.support:recyclerview-v7:25.3.1'
    compile 'com.android.support:cardview-v7:25.3.1'
    compile 'cn.bingoogolapple:bga-banner:2.1.7@aar'
	    compile 'com.github.bumptech.glide:glide:3.7.0'
		    compile 'eu.the4thfloor.volley:com.android.volley:2015.05.28'


# 设计item
## 枯燥的 info_item.xml代码
``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/item"
    android:background="#fff"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <TextView
        android:id="@+id/item_headtitle"
        android:layout_width="match_parent"
        android:background="#fff"
        android:layout_height="30dp"
        android:visibility="gone"
        android:layout_marginTop="10dp"
        android:gravity="center|left"
        android:paddingLeft="10dp"
        android:text="头部"/>
    <android.support.v7.widget.CardView
        android:layout_marginTop="10dp"
        android:id="@+id/opencard"
        android:background="#fff"

        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <LinearLayout
            android:background="#fff"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <LinearLayout
                android:background="#fff"
                android:layout_weight="3"
                android:layout_width="300dp"
                android:gravity="center|left"
                android:orientation="vertical"
                android:layout_height="130dp">
                <TextView
                    android:id="@+id/item_title"
                    android:background="#fff"
                    android:layout_width="wrap_content"
                    android:padding="10dp"
                    android:textColor="#000"
                    android:textSize="18sp"
                    android:gravity="left"
                    android:text="标题"
                    android:layout_height="wrap_content" />
                <TextView
                    android:id="@+id/data"
                    android:layout_marginTop="10dp"
                    android:layout_marginLeft="10dp"
                    android:text="20170417"
                    android:visibility="gone"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content" />
            </LinearLayout>
            <ImageView
                android:layout_weight="1"
                android:layout_marginRight="5dp"
                android:id="@+id/item_image"
                android:layout_width="120dp"
                android:layout_height="120dp" />
        </LinearLayout>
    </android.support.v7.widget.CardView>
</LinearLayout>
```
此段代码中，包含了item的标题以及item的头部信息。

Recycleview可以看做是ListView的升级版，它性能更好，玩法也更多，既然要用到列表，那我们就需要有一个适配器，以及一个javabean类，用于生成列表中的新闻对象（？）
# ItemBean 
## 枯燥的 Item.class
``` java
package com.example.administrator.volleytest.UI.bean;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getHeadtitle() {
        return headtitle;
    }

    public void setHeadtitle(String headtitle) {
        this.headtitle = headtitle;
    }

    String headtitle;//头部标题
    String id;//id
    String title;//文章标题
    String date;//日期
    String imgurl;//配图地址
}
```

##  懵逼的 InfoListAdapter.class


```java
public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;//用于储存数据
    private Context mContext;//上下文
    InfoViewHolder holder=null; //viewholde，可以提高recycleview的性能



    public  InfoListAdapter(ArrayList<Item> data,Context context) {

        //构造方法，用于接收上下文和展示数据

        this.mData = data;
        this.mContext=context;
    }


    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        holder=new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.info_item,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        //此方法内可以对布局中的控件进行操作

        holder.title.setText(mData.get(position).getTitle());//

        Glide.with(mContext).load(mData.get(position).getImgurl()).into(holder.img);
    }


    @Override
    public int getItemCount() {

        //获取数据长度

        return mData.size();
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        //此部分我也难以用语言来解释，诸位可以搜索下网上各路大牛的详解
        //我也需要学习


        TextView title;//标题
        ImageView img;//显示的图片
        TextView headTitle;//头部标题

        public InfoViewHolder(View itemView) {
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.item_title);
            img= (ImageView) itemView.findViewById(R.id.item_image);
            headTitle= (TextView) itemView.findViewById(R.id.item_headtitle);
        }
    }
}
    
```
适配器准备好后，我们要对HomeActivity和activity_main布局 做一些简单的修改用于测试适配器是否能够正常使用

首先，在activity_main.xml中添加RecycleView的控件，使其填满整个布局
activity_main.xml(部分)
``` xml
<android.support.v7.widget.RecyclerView
    android:id="@+id/infolist"
    android:layout_marginTop="?attr/actionBarSize"
    android:layout_width="match_parent"
    android:layout_height="match_parent"></android.support.v7.widget.RecyclerView>

```
然后在Activity中，我们需要对控件进行绑定，创建适配器以及添加模拟数据
##  将就能看的 HomeActiviy.java
``` java
public class HomeActivity extends BaseActivity {
    private RecyclerView mInfoList;//用于显示的列表

    private ArrayList<Item> mDatas;//用于储存数据

    private InfoListAdapter adapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();
        initView();

    }

    private void initView() {
		//初始化页面
        setTitle("首页", 1);//设置标题
        mInfoList= (RecyclerView) findViewById(R.id.infolist);//绑定RecycleView
        mInfoList.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器，你可以通过这个来决定你是要做一个Listview还是瀑布流
        adapter=new InfoListAdapter(mDatas,HomeActivity.this);//初始化适配器
        mInfoList.setAdapter(adapter);//为ReycleView设置适配器

    }

    private void  initDate(){
	//初始化数据
        mDatas=new ArrayList<>();
		
		//添加模拟数据
        for (int i='A';i<'z';i++){
            Item item=new Item();
            item.setTitle(""+(char)i);
            item.setImgurl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496110531&di=281ed2731cabceee7c851e5b2ca83a85&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F13%2F15%2F64%2F44c58PICsni_1024.jpg");
            mDatas.add(item);
        }


    }
}
```
记得在AndroidManifest.xml中添加网络访问权限
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```
现在我们再看下效果

![Markdown](http://i4.buimg.com/591309/b6978dd5b0d989f1.gif)

大致效果就是这样，不过这个图片什么鬼

接下来就要考虑数据的问题，点开文档，我们直接看第三条  最新消息和第五条 过往消息

>3. 最新消息
>URL: http://news-at.zhihu.com/api/4/news/latest
>响应实例：
  {
      date: "20140523",
      stories: [
          {
              title: "中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）",
              ga_prefix: "052321",
              images: [
                  "http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"
              ],
              type: 0,
              id: 3930445
          },
      ...
      ],
      top_stories: [
          {
              title: "商场和很多人家里，竹制家具越来越多（多图）",
              image: "http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg",
              ga_prefix: "052315",
              type: 0,
              id: 3930883
          },
      ...
      ]
  }
分析：
date : 日期
stories : 当日新闻
title : 新闻标题
images : 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
ga_prefix : 供 Google Analytics 使用
type : 作用未知
id : url 与 share_url 中最后的数字（应为内容的 id）
multipic : 消息是否包含多张图片（仅出现在包含多图的新闻中）
top_stories : 界面顶部 ViewPager 滚动显示的显示内容（子项格式同上）（请注意区分此处的 image 属性与 stories 中的 images 属性）

我只贴出了第三条的数据，没有贴出第五条，因为第三条和第五条除了url后面带的参数不同，其返回形式是相同的
文档的作者已经将数据请求的链接以及返回数据的格式和 解释给出来了，下一步我们来写一个网络请求方法，将数据添加到ArrayList中


我们在HomeActivity中添加一个

## 初步展示的 HomeActivity.class

```java
private int otherdate=0;//从今日算起，倒数第几天 eg:昨天 就是1 前天就是 2

private RequestQueue mQueue;
 private void  initDate(){
 
 
 //将此处之前的for循环插入虚拟数据删除
        mDatas=new ArrayList<>();
        getInfoFromNet();
    }

    private void getInfoFromNet(){
        //获取网络数据
        mQueue = Volley.newRequestQueue(this);
		
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://news.at.zhihu.com/api/4/news/before/" +    getDate(), null, new Response.Listener<JSONObject>() {
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
						//开始解析数据
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
			//如果遇到异常，在这里通知用户
            @Override
            public void onErrorResponse(VolleyError error) {
			
                showToast("碰到了一点问题");
            	//此处的showToast()；是已经在BaseActivity中写好的，可以直接拿来用
			}
        });
        mQueue.add(jsonObjectRequest);//开始任务
    }



    private String getDate(){
            //获取当前需要加载的数据的日期
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -otherdate);//otherdate天前的日子

            String date = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
            //将日期转化为20170520这样的格式
            return date;

    }

```

我们再来看下效果

![Markdown](http://i1.piimg.com/591309/587088b2a1fc8469.gif)

然后和知乎日报对比下

![Markdown](http://i1.piimg.com/591309/551f515b47c5f22b.gif)

可能有人要问了，Banner你吃了嘛？还有下面显示的内容和你这个不一样啊，还有那些今日热闻什么的，你到底会不会呀

嗯，我也注意到了，让我们来一项一项解决它

1.显示数据不准确

经过我的观察，虽然今天是2017年5月23日，但是去用20170523请求到的是昨天的数据，但是如果用20170524请求到的数据是不包含banner也就是“top_stories”字段，那么应该是请求的问题了
我们来对比下新内容和历史内容的请求链接
新的：http://news-at.zhihu.com/api/4/news/latest
历史内容：http://news.at.zhihu.com/api/4/news/before/20170523
嗯，刚才果然粗心大意了，那么代码又要改改。
  ##  更改后的代码 HomeActivity →getInfoFromNet()
``` java

 mQueue = Volley.newRequestQueue(this);
        String url=null;
        if (otherdate==0){
		//如果是今日就用最后的数据
            url="http://news-at.zhihu.com/api/4/news/latest";
        }else {
		//否则就是之前的判断流程
           url= "http://news.at.zhihu.com/api/4/news/before/" + getDate();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, 
		//将此处之前的地址换为url参数
		……
		//为了防止重复加载当前日的数据，我们需要让日子往后挪一天
		
		  c.add(Calendar.DAY_OF_MONTH, -otherdate-1);//otherdate天前的日子
		  	……
		  
```
再来对比下

![我们的日报](http://i1.piimg.com/591309/8276aade83e723bc.gif)


知乎日报

![知乎日报](http://i1.piimg.com/591309/d306c373b54ce8f8.gif)

这样一来，最简单的数据问题解决了

2,Banner去哪儿了
我以前一直以为上面的banner是游离于listview之外的，知道我发现了headview这个东西，现在我们就要向recycleview中添加一个headview

先写一个headview的布局

## headview.xml
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="230dp"
    >
    <cn.bingoogolapple.bgabanner.BGABanner
        android:id="@+id/banner"
        app:banner_pageChangeDuration="1000"
        app:banner_pointAutoPlayAble="true"
        app:banner_tipTextSize="17sp"

        app:banner_pointTopBottomMargin="10dp"
        android:layout_width="match_parent"
        android:layout_height="230dp"/>
</LinearLayout>
```
对InfoListAdapter添加以下代码
## 修改InfoListAdapter

```java
public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;//用于储存数据
    private Context mContext;//上下文
    InfoViewHolder holder=null; //viewholde，可以提高recycleview的性能




    public  InfoListAdapter(ArrayList<Item> data,Context context) {

        //构造方法，用于接收上下文和展示数据

        this.mData = data;
        this.mContext=context;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	//这里进行过改动
        if (headView!=null && viewType==TYPE_HEADER) return new InfoViewHolder(headView);
        holder=new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.info_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        //此方法内可以对布局中的控件进行操作
		//这里进行过改动
        if (getItemViewType(position)==TYPE_HEADER) return;
        final int pos=getRealPosition(holder);

        holder.title.setText(mData.get(pos).getTitle());//

        Glide.with(mContext).load(mData.get(pos).getImgurl()).into(holder.img);
    }


    @Override
    public int getItemCount() {

        //获取数据长度
		//这里进行过改动
        return headView==null? mData.size():mData.size()+1;
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        //此部分我难以用语言来解释，诸位可以搜索下网上各路大牛的详解
        //我也需要学习


        TextView title;//标题
        ImageView img;//显示的图片
        TextView headTitle;//头部标题

        public InfoViewHolder(View itemView) {
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.item_title);
            img= (ImageView) itemView.findViewById(R.id.item_image);
            headTitle= (TextView) itemView.findViewById(R.id.item_headtitle);
        }
    }
	
	
	//下面的是新添加的
	
	
    public static final int TYPE_HEADER = 0;//显示headvuiew
    public static final int TYPE_NORMAL = 1;//显示普通的item
    private View headView;//这家伙就是Banner

    public void setHeadView(View headView){
        this.headView=headView;
        notifyItemInserted(0);
    }

    public View getHeadView(){
        return headView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView==null)
            return TYPE_NORMAL;
        if (position==0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position=holder.getLayoutPosition();
        return headView==null? position:position-1;
    }
}
	
	
	
```

向HomeActivity中添加以下代码
## HomeActivity→initBanner();

```java
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
```
我们再来看一次效果

![Markdown](http://i1.piimg.com/591309/5891f616220771f3.gif)

由于（不知道什么原因有限），这次就和大家交流这些，在下一篇博文中将会把我们在知乎日报上看到的日期头以及加载更多，刷新还有点击查看详细内容等功能实现出来。如果大家有不懂的欢迎在评论中说出来，如果我有哪部分表述的不准确，也希望能够得到指点。

> 愿与诸位同进步

### 谢谢。
  [1]: https://github.com/
  [2]: http://blog.csdn.net/wei18359100306/article/details/45645145

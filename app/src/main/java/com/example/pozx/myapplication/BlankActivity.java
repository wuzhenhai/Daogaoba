package com.example.pozx.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.LogRecord;


public class BlankActivity extends SlidingActivity {

    Button bt_get;
    Button bt_setting;
    TextView title;
    LinearLayout ll_bt_select_azj;
    LinearLayout ll_bt_select_dxjm;
    LinearLayout ll_bt_select_all;
    ListView lv;
    View.OnClickListener getListener = null;
    View.OnClickListener settingListener = null;
    View.OnClickListener showMoreListener = null;
    private String str = "";
    private JSONArray jarray;
    private PopupWindow menu;
    private LayoutInflater inflater;
    private View layout;
    private SQLiteDatabase db;
    private String DDtype;//代祷类型
    private String DDSelectType;//选择的代祷事项类型
    private int showListNum;//初始显示条目数量
    private Button showMoreListItemBtn;
    private MySpAdapter adapter;
    RefreshableView refreshableView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        setBehindContentView(R.layout.activity_login);

        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);

        //初始化菜单
        initMenu();
        //初始化
        init();


        //检测网络是否可用
        if (isNetworkAvailable(BlankActivity.this)) {
            Toast.makeText(getApplicationContext(), "当前有可用网络！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_SHORT).show();
        }


        checkDbData(); //检查数据库daidao表里是否有数据


        setBtnEvent();//设置各种按钮事件
        setRefreshEvent();//下拉菜单刷新事件
    }

    //设置按钮事件
    private void setBtnEvent(){
        //刷新按钮点击事件
        getListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(BlankActivity.this, LoginActivity.class);
                // TODO Auto-generated method stub
                startActivityForResult(it,1);

            }
        };
        bt_get.setOnClickListener(getListener);
        //模拟点击获取资料
        //bt_get.performClick();

        settingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu.isShowing()) {
                    menu.dismiss();
                    listViewAnimRe();
                    return;
                }
                showMenu();
            }
        };

        bt_setting.setOnClickListener(settingListener);


        //切换到爱之家代祷事项
        ll_bt_select_azj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDSelectType="1";
                showListNum=20;
                initListView();

                title.setText("爱之家");
                menu.dismiss();
                listViewAnimRe();

            }
        });

        //切换到弟兄姐妹代祷事项
        ll_bt_select_dxjm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDSelectType = "3";
                showListNum = 20;
                initListView();

                title.setText("弟兄姐妹");
                menu.dismiss();
                listViewAnimRe();
            }
        });

        //切换到全部代祷事项
        ll_bt_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDSelectType="99";
                initListView();
                title.setText("全部代祷事项");
                menu.dismiss();
                listViewAnimRe();
            }
        });

        //显示跟多按钮事件
        showMoreListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMoreListItem();
            }
        };
        showMoreListItemBtn.setOnClickListener(showMoreListener);
    }

    //设置下拉刷新事件
    private void setRefreshEvent(){
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {

            //onRefresh已经在线程中运行
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(1000);

                    HttpPost httpPost = new HttpPost("http://wuzhenhai518.duapp.com/swdg_module/data_pro.php");
                    List<NameValuePair> params = new ArrayList<>();
                    //1取得爱之家，2取得弟兄姐妹，3取得全部
                    params.add(new BasicNameValuePair("type", DDtype));
                    //String strResult;
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            //通过PHP获取mysql上的数据
                            str = EntityUtils.toString(httpResponse.getEntity());
                            str.replaceAll("\n", "");
                            //String s="[{'id':'001','content':'测试数据','date':'2015-10-10'},{'id':'001','content':'测试数据','date':'2015-10-10'}]";
                            JSONArray jsonArray = new JSONArray(str);
                            jarray = jsonArray;
                            str = "Ok";

                            //数据库清空
                            clearFeedTable(db);
                            handler.sendEmptyMessage(0);
                            //tvShow.setText(strResult);
                        } else {
                            //tvShow.setText("Error");
                            str = "Error";
                        }
                    } catch (Exception e) {
                        Log.e("Error: ", "Can't connect!  " + e.toString());
                        //执行完毕后给handler发送一个空消息
                        handler.sendEmptyMessage(1);

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //刷新结束
                refreshableView.finishRefreshing();
                handler.sendEmptyMessage(2);

            }
        }, 0);

    }

    private void checkDbData()
    {
        Cursor cursorCount=db.rawQuery("select * from daidao",null);
        if(cursorCount.getCount()!=0)
        {
            //数据库有数据，就初始化ListView
            initListView();
            Log.i("Logi: ","ListView init....");
        }
        else
        {
            Log.i("Logi: ","db is not have date");
        }
    }


    //线程处理完，发送消息给handler处理接下去的任务
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //ListView lv = (ListView) findViewById(R.id.cards_list);

                    Log.i("Logi", "in handler");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jsonObj = jarray.optJSONObject(i);
                        try {

                            String id = "编号:" + jsonObj.getString("id");
                            String content = jsonObj.getString("content");
                            String date = jsonObj.getString("date");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("Itemid", id);//图像资源的ID
                            map.put("ItemContent", content);
                            map.put("ItemDate", date);
                            listItem.add(map);


                        } catch (Exception e) {
                            Log.e("error: ", e.toString());
                        }
                        title.setText("全部代祷事项");
                    }

                    MySpAdapter listItemAdapter = new MySpAdapter(BlankActivity.this, listItem,//数据源
                            R.layout.list_item_card,//ListItem的XML实现
                            //动态数组与ImageItem对应的子项
                            new String[]{"Itemid", "ItemContent", "ItemDate"},
                            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                            new int[]{R.id.textId, R.id.textContent, R.id.textDate}
                    );
                    saveDataToDataBase();
                    showListNum=20;
                    listItemAdapter.setNum(20);
                    adapter=listItemAdapter;
                    lv.setAdapter(adapter);
                    setItemEvent(lv);
                    break;
                case 1:

                    Toast.makeText(getApplicationContext(), "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                    ;
                    break;
                case 2:
                    title.setText("全部代祷事项");
                    break;
            }
        }


    };


    //初始化
    public void init(){
        DDtype="3";//POST到服务器的编号，3是获取全部数据
        DDSelectType="3";//3是弟兄姐妹，1是爱之家
        showListNum=20;

        ll_bt_select_azj=(LinearLayout)layout.findViewById(R.id.ll_bt_azj);
        ll_bt_select_dxjm=(LinearLayout)layout.findViewById(R.id.ll_bt_dxjm);
        ll_bt_select_all=(LinearLayout)layout.findViewById(R.id.ll_bt_all);
        title=(TextView)findViewById(R.id.textView2);

        showMoreListItemBtn = new Button(this);
        showMoreListItemBtn.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        showMoreListItemBtn.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        showMoreListItemBtn.setText("显示更多");
        showMoreListItemBtn.setTop(0);
        showMoreListItemBtn.setTextColor(Color.WHITE);
        showMoreListItemBtn.setGravity(Gravity.CENTER);
        showMoreListItemBtn.setBackgroundResource(R.drawable.more_btn);



        //初始化数据库
        DBHelper database=new DBHelper(this);
        db= database.getReadableDatabase();


        refreshableView=(RefreshableView)findViewById(R.id.refreshable_view);//下拉刷新
        refreshableView.setPopWindow(menu,inflater);

        bt_get = (Button) findViewById(R.id.bt_get);
        bt_setting = (Button) findViewById(R.id.bt_setting);

        lv=(ListView)findViewById(R.id.cards_list);

    }



    //检查网络是否可用
    public boolean isNetworkAvailable(SlidingActivity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //实例化PopupWindow创建菜单
    private void initMenu() {

        //获取LayoutInflater实例
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //获取弹出菜单的布局
        layout = inflater.inflate(R.layout.popwindow, null);
        //设置popupWindow的布局
        menu = new PopupWindow(layout, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        menu.setAnimationStyle(R.style.AnimationFade);

    }

    //显示菜单
    public void showMenu() {
        //设置位置
        View myayout = inflater.inflate(R.layout.activity_blank, null);
        menu.showAtLocation(myayout, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 148); //设置在屏幕中的显示位置
        Animation list_anim=new AnimationUtils().loadAnimation(this,R.anim.listview_anim);
        lv.startAnimation(list_anim);
    }

    //动画效果恢复
    private void listViewAnimRe(){
        Animation list_anim_re=new AnimationUtils().loadAnimation(this,R.anim.listview_anim_re);
        lv.startAnimation(list_anim_re);
    }

    //从数据库获取数据，不通过网络
    private void initListView(){

        Cursor cursor=db.query("daidao",new String[]{"cid,content,date,type"},null,null,null,null,null,null);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        listItem.clear();
        while(cursor.moveToNext())
        {
            String type=cursor.getString(3);
            //初始状态显示所有条目
            if(!DDSelectType.equals("99")) {
                if (type.equals(DDSelectType)) {
                    initListViewFromDB(cursor,listItem);
                }
            }
            else
            {
                //添加一个项目
                initListViewFromDB(cursor,listItem);
            }


        }

       adapter=new MySpAdapter(BlankActivity.this, listItem,//数据源
                R.layout.list_item_card,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"Itemid", "ItemContent", "ItemDate"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.textId, R.id.textContent, R.id.textDate});

        if(adapter.getAllCount()>showListNum) {
            //设置显示的数量
            adapter.setNum(showListNum);
            if(lv.getFooterViewsCount()>=1)
                lv.removeFooterView(showMoreListItemBtn);
            lv.addFooterView(showMoreListItemBtn);
        }
        else
        {
            adapter.setNum(adapter.getAllCount());
            lv.removeFooterView(showMoreListItemBtn);
        }

        //adapter.setGetAllCount(true);//显示所有条目：true全部显示，false不全部显示

        lv.setAdapter(adapter);
        setItemEvent(lv);
    }

    //点击获得更多按钮添加ListView项目
    private void getMoreListItem()
    {
        Log.i("getMoreListItem:","clicked");
        //int temp=showListNum;
        showListNum+=10;

        if(adapter.getAllCount()>showListNum) {
            //设置显示的数量
            adapter.setNum(showListNum);
            Log.i("showListNum:", showListNum+"");
        }
        else
        {
            adapter.setNum(adapter.getAllCount());
        }

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "点击的Item位置："+position, Toast.LENGTH_SHORT).show();
//            }
//        });

        setItemEvent(lv);

        //adapter.setGetAllCount(true);//显示所有条目
        //lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lv.setSelection(temp-1);
    }

    private void setItemEvent(ListView lv)
    {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView ttv = (TextView) view.findViewById(R.id.textId);
                    Toast.makeText(getApplicationContext(), "点击的Item位置：" + position + "---" + ttv.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;

            }
        });
    }

    //从数据库中获取数据添加到listItem里面
    private void initListViewFromDB(Cursor cursor,ArrayList<HashMap<String, Object>> listItem){
        String id = cursor.getString(0);
        String content = cursor.getString(1);
        String date = cursor.getString(2);


        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Itemid", "编号:"+id);
        map.put("ItemContent", content);
        map.put("ItemDate", date);
        listItem.add(map);

    }


    //清空数据库
    public void clearFeedTable(SQLiteDatabase db){
        String sql = "DELETE FROM " + "daidao" +";";
        db.execSQL(sql);
        revertSeq(db);
    }
    private void revertSeq(SQLiteDatabase db) {
        String sql = "update sqlite_sequence set seq=0 where name='"+"daidao"+"'";
        db.execSQL(sql);
    }

    //费时处理Listview线程
    private void saveDataToDataBase(){
          new Thread() {

             public void run() {
                 super.run();
                 for (int i = 0; i < jarray.length(); i++) {
                     JSONObject jsonObj = jarray.optJSONObject(i);
                     try {

                         //String id = "编号:" + jsonObj.getString("id");
                         String content = jsonObj.getString("content");
                         String date = jsonObj.getString("date");
                         String type = jsonObj.getString("type");

                         //数据存入数据库
                         ContentValues cv = new ContentValues();
                         int cid = Integer.parseInt(jsonObj.getString("id"));
                         cv.put("cid", cid);
                         cv.put("content", content);
                         cv.put("date", date);
                         cv.put("type",type);

                         db.insert("daidao", null, cv);


                     } catch (Exception e) {
                         Log.e("error: ", e.toString());
                     }
                 }
              }
             }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (menu.isShowing()) {
            menu.dismiss();
        }
        return true;
    }
}


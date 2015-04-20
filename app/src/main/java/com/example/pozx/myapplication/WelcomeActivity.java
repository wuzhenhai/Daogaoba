package com.example.pozx.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends ActionBarActivity {
    private Button reset;
    private int[] ids = { R.drawable.bt_add,
            R.drawable.bt_sub, R.drawable.bt_mult,
            R.drawable.bt_div };
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private List<View> guides = new ArrayList<View>();
    private ViewPager pager;
    private ImageView curDot;

    // 位移量
    private int offset;
    // 记录当前的位置
    private int curPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences("firstOpenApp",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        reset=(Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.contains("firstOpen"))
                {
                    editor.clear();
                    editor.commit();
                }
            }
        });



        if(sharedPreferences.contains("firstOpen"))
        {
            int isFirst=sharedPreferences.getInt("firstOpen",0);
            editor.putInt("firstOpen",isFirst++);
            editor.commit();
            Toast.makeText(getApplicationContext(), "不是首次登录", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(WelcomeActivity.this, BlankActivity.class);
            // TODO Auto-generated method stub
            //startActivityForResult(it, 1);
        }
        else {
            editor.putInt("firstOpen", 1);
            editor.commit();
            Toast.makeText(getApplicationContext(), "首次登录", Toast.LENGTH_SHORT).show();
            initWelActivity();
        }

    }

    private void initWelActivity(){
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            guides.add(iv);
        }
        curDot = (ImageView) findViewById(R.id.cur_dot);
        curDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        offset = curDot.getWidth();
                        return true;
                    }
                });

        WecommPagerAdapter adapter = new WecommPagerAdapter(guides);
        pager = (ViewPager) findViewById(R.id.showFirstOpen);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                moveCursorTo(arg0);
                if (arg0 == ids.length - 1) {// 到最后一张了
                    //skipActivity(2);
                }
                curPos = arg0;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }

        });
    }

    /**
     * 移动指针到相邻的位置
     *
     * @param position
     *            指针的索引值
     * */
    private void moveCursorTo(int position) {
        TranslateAnimation anim = new TranslateAnimation(offset * curPos,
                offset * position, 0, 0);
        anim.setDuration(300);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
    }

    /**
     * 延迟多少秒进入主界面
     * @param min 秒
     */
    private void skipActivity(int min) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,
                        BlankActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 1000*min);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
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
}

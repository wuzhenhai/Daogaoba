package com.example.pozx.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends ActionBarActivity {

    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //
        Button hellobtn = (Button)findViewById(R.id.hellobutton);
        hellobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent it = new Intent(MainActivity.this, Calculator.class);
                    // TODO Auto-generated method stub
                      startActivityForResult(it,1);


            }
        });

        Button multbtn = (Button)findViewById(R.id.button_mult);
        multbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, WelcomeActivity.class);
                // TODO Auto-generated method stub
                startActivityForResult(it,1);

            }
        });

        Button talbtn = (Button)findViewById(R.id.tab_bt);
        talbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, BlankActivity.class);
                // TODO Auto-generated method stub
                startActivityForResult(it,1);

            }
        });
        //设置监听按钮点击事件
//        hellobtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //得到textview实例
//                TextView hellotv = (TextView) findViewById(R.id.hellotext);
//                //弹出Toast提示按钮被点击了
//                showTextToast("Clicked");
//                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//                //读取strings.xml定义的interact_message信息并写到textview上
//                hellotv.setText(R.string.bt_message);
//            }
//        });


    }
    //防止多次点击后提示时间延长
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg+"Click again");
        }
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //布局添加设置按钮
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //super.onCreateOptionsMenu(menu);
        //代码添加设置按钮
//        menu.add(Menu.NONE,  Menu.FIRST+1 , 0, "设置").setIcon(R.drawable.setting2);
//        menu.add(Menu.NONE,  Menu.FIRST+2 , 1, "颜色").setIcon(R.drawable.setting2);
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
         //代码添加的按钮事件监听
//        switch(item.getItemId()) //得到被点击的item的itemId
//        {
//            case  Menu.FIRST+1 ://对应的ID就是在add方法中所设定的Id
//                Log.i("menu","click");
//                break;
//            case  Menu.FIRST+2 :
//                break;
//            default:break;
//        }



        //代布局添加的按钮事件监听
       int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("menu","1——click");
            return true;
        }
        if (id == R.id.action_settings2) {
            Log.i("menu","2——click");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

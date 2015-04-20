package com.example.pozx.myapplication;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;


public class Calculator extends Activity {


    ImageButton btn_num0;
    ImageButton btn_num1;
    ImageButton btn_num2;
    ImageButton btn_num3;
    ImageButton btn_num4;
    ImageButton btn_num5;
    ImageButton btn_num6;
    ImageButton btn_num7;
    ImageButton btn_num8;
    ImageButton btn_num9;
    ImageButton btn_add;
    ImageButton btn_eq;
    ImageButton btn_sub;
    ImageButton btn_dot;
    ImageButton btn_mult;
    ImageButton btn_div;
    ImageButton btn_clr;
    TextView tv;
    String str="";
    int type=0;
    Double num_pre=0.0;
    Double num_now=0.0;
    Double result=0.0;
    Boolean eqIsBeClick=false;


    View.OnClickListener listener0=null;
    View.OnClickListener listener1=null;
    View.OnClickListener listener2=null;
    View.OnClickListener listener3=null;
    View.OnClickListener listener4=null;
    View.OnClickListener listener5=null;
    View.OnClickListener listener6=null;
    View.OnClickListener listener7=null;
    View.OnClickListener listener8=null;
    View.OnClickListener listener9=null;
    View.OnClickListener listener_clr=null;
    View.OnClickListener listener_add=null;
    View.OnClickListener listener_eq=null;
    View.OnClickListener listener_sub=null;
    View.OnClickListener listener_dot=null;
    View.OnClickListener listener_mult=null;
    View.OnClickListener listener_div=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        btn_num0=(ImageButton)findViewById(R.id.im_bt0);
        btn_num1=(ImageButton)findViewById(R.id.im_bt1);
        btn_num2=(ImageButton)findViewById(R.id.im_bt2);
        btn_num3=(ImageButton)findViewById(R.id.im_bt3);
        btn_num4=(ImageButton)findViewById(R.id.im_bt4);
        btn_num5=(ImageButton)findViewById(R.id.im_bt5);
        btn_num6=(ImageButton)findViewById(R.id.im_bt6);
        btn_num7=(ImageButton)findViewById(R.id.im_bt7);
        btn_num8=(ImageButton)findViewById(R.id.im_bt8);
        btn_num9=(ImageButton)findViewById(R.id.im_bt9);
        btn_add=(ImageButton)findViewById(R.id.im_bt_add);
        btn_eq=(ImageButton)findViewById(R.id.im_bt_eq);
        btn_sub=(ImageButton)findViewById(R.id.im_bt_sub);
        btn_dot=(ImageButton)findViewById(R.id.im_bt_dot);
        btn_mult=(ImageButton)findViewById(R.id.im_bt_mult);
        btn_div=(ImageButton)findViewById(R.id.im_bt_div);
        btn_clr=(ImageButton)findViewById(R.id.im_bt_c);
        tv=(TextView)findViewById(R.id.textView);



        //数字键0
        listener0=new View.OnClickListener(){
           @Override
           public void onClick(View v){

               if(eqIsBeClick)
               {
                   tv.setText("0");
                   eqIsBeClick=false;
               }
               str=(String)tv.getText();
               if(str.equals("0"))
               {
                   str="0";
               }
               else {
                   str = str + "0";
               }
               tv.setText(str);
           }
        };
        btn_num0.setOnClickListener(listener0);

        //数字键1
        listener1=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="1";
                }
                else {
                    str = str + "1";
                }
                tv.setText(str);
            }
        };
        btn_num1.setOnClickListener(listener1);

        //数字键2
        listener2=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="2";
                }
                else {
                    str = str + "2";
                }
                tv.setText(str);
            }
        };
        btn_num2.setOnClickListener(listener2);

        //数字键3
        listener3=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="3";
                }
                else {
                    str = str + "3";
                }
                tv.setText(str);
            }
        };
        btn_num3.setOnClickListener(listener3);

        //数字键4
        listener4=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="4";
                }
                else {
                    str = str + "4";
                }
                tv.setText(str);
            }
        };
        btn_num4.setOnClickListener(listener4);

        //数字键5
        listener5=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="5";
                }
                else {
                    str = str + "5";
                }
                tv.setText(str);
            }
        };
        btn_num5.setOnClickListener(listener5);

        //数字键6
        listener6=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="6";
                }
                else {
                    str = str + "6";
                }
                tv.setText(str);
            }
        };
        btn_num6.setOnClickListener(listener6);

        //数字键7
        listener7=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="7";
                }
                else {
                    str = str + "7";
                }
                tv.setText(str);
            }
        };
        btn_num7.setOnClickListener(listener7);

        //数字键8
        listener8=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="8";
                }
                else {
                    str = str + "8";
                }
                tv.setText(str);
            }
        };
        btn_num8.setOnClickListener(listener8);

        //数字键9
        listener9=new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(eqIsBeClick)
                {
                    tv.setText("0");
                    eqIsBeClick=false;
                }
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    str="9";
                }
                else {
                    str = str + "9";
                }
                tv.setText(str);
            }
        };
        btn_num9.setOnClickListener(listener9);

        //清除键
        listener_clr=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str="0";
                tv.setText(str);
                claerAll();
            }
        };
        btn_clr.setOnClickListener(listener_clr);

        //dot
        listener_dot=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str=(String)tv.getText();
                if(str.contains("."))
                    return;
                if(str.equals("0"))
                {
                    str="0.";
                }
                else {
                    str = str + ".";
                }
                tv.setText(str);
            }
        };
        btn_dot.setOnClickListener(listener_dot);

        //加
        listener_add=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str=(String)tv.getText();
                if(str.equals("0"))
                {
//                    Log.i("add:","return");
                    return;
                }
//                else
//                {
//                    if(type!=0)
//                    {
//                        clickEq(btn_eq);
//                        eqIsBeClick=false;
//                    }
//                }
                type=1;
                num_pre=Double.parseDouble((String)tv.getText());
                tv.setText("0");
            }
        };
        btn_add.setOnClickListener(listener_add);

        //减法
        listener_sub=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    return;
                }
//                else
//                {
//                    if(type!=0)
//                    {
//                        clickEq(btn_eq);
//                        eqIsBeClick=false;
//                    }
//                }
                type=2;
                num_pre=Double.parseDouble((String)tv.getText());
                tv.setText("0");
            }
        };
        btn_sub.setOnClickListener(listener_sub);

        //乘法
        listener_mult=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                    return;
                }
//                else
//                {
//                    if(type!=0)
//                    {
//                        clickEq(btn_eq);
//                        eqIsBeClick=false;
//                    }
//                }
                type=3;
                num_pre=Double.parseDouble((String)tv.getText());
                tv.setText("0");
            }
        };
        btn_mult.setOnClickListener(listener_mult);

        //除法
        listener_div=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                str=(String)tv.getText();
                if(str.equals("0"))
                {
                        return;
                }
//                else
//                {
//                    if(type!=0)
//                    {
//                        clickEq(btn_eq);
//                        eqIsBeClick=false;
//                    }
//                }
                type=4;
                num_pre=Double.parseDouble((String)tv.getText());
                tv.setText("0");
            }
        };
        btn_div.setOnClickListener(listener_div);

        //等于键
        listener_eq=new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!eqIsBeClick) {
                    num_now = Double.parseDouble((String) tv.getText());
                }


                switch (type)
                {
                    case 1:result=add(num_pre,num_now);break;
                    case 2:result=sub(num_pre,num_now);break;
                    case 3:result=mult(num_pre, num_now);break;
                    case 4:result=div(num_pre, num_now);break;
                    default:break;
                }
                num_pre=result;

                tv.setText(String.valueOf(result));
                eqIsBeClick=true;
                type=0;

            }
        };
        btn_eq.setOnClickListener(listener_eq);
    }

    //清除所有按键状态
    public void claerAll(){
        eqIsBeClick=false;
        num_pre=0.0;
        num_now=0.0;
        result=0.0;
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    //触发一次点击事件
//    public static void clickEq(ImageButton bt) {
//      bt.performClick();
//    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mult(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        if(v2==0)
        {
            return 0.0;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 7, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
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

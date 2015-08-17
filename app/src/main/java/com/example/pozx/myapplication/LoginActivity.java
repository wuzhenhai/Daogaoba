package com.example.pozx.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private Button login_btn;
    private View.OnClickListener loginListener=null;
    private EditText pwd_et;
    private EditText un_et;
//    private TextView show_md5;
    private String str;
    private String username;
    private String password;
    private String userinfo="";
    private Toast toast = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(checkLogin())
        {}
        else
        {}

        login_btn=(Button)findViewById(R.id.signin_button);
        pwd_et=(EditText)findViewById(R.id.password_edit);
        un_et=(EditText)findViewById(R.id.username_edit);
//        show_md5=(TextView)findViewById(R.id.showMd5);
        username="";
        password="";

        //检测网络是否可用
        if (isNetworkAvailable(LoginActivity.this)) {
            showTextToast("当前有可用网络");
        } else {
            showTextToast("当前没有可用网络");
        }

        loginListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isNetworkAvailable(LoginActivity.this))
                {
                    showTextToast("网络不可用，请检查网络");
                    return;
                }
                str = getMD5Str(pwd_et.getText().toString());
                password = str;
                username = un_et.getText().toString();

                if(username.equals("")||password.equals(""))
                {
                    showTextToast("用户名或密码不能为空");
                    return;
                }

          new Thread(){
                    @Override
                    public void run() {
                        //去验证登录信息，并且获取个人信息
                        HttpPost httpPost = new HttpPost("http://wuzhenhai518.duapp.com/swdg_module/checkLoginUser.php");
                        List<NameValuePair> params = new ArrayList<>();
                        Message msg = handler.obtainMessage();

//                        if(username.equals("")||password.equals(""))
//                        {
//                            handler.sendEmptyMessage(2);
//                            return;
//                        }
                        params.add(new BasicNameValuePair("username", username));
                        params.add(new BasicNameValuePair("password", password));
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                //通过PHP获取mysql上的数据
                                str = EntityUtils.toString(httpResponse.getEntity());
                                msg.what=0;
                                msg.obj=str;

                                handler.sendMessage(msg);
                                //tvShow.setText(strResult);
                            } else {
                                //tvShow.setText("Error");
                                //str = "Error";
                                handler.sendEmptyMessage(3);
                            }
                        } catch (Exception e) {
                            Log.e("Error: ", "Can't connect!  " + e.toString());
                            //执行完毕后给handler发送一个空消息
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
                //MD5加密输出
//                if(!pwd_et.getText().toString().equals("")) {
//                    str = getMD5Str(pwd_et.getText().toString());
//                    password=str;
//                    username=un_et.getText().toString();
//                }
//                show_md5.setText("MD5:"+str);
            }
        };

        login_btn.setOnClickListener(loginListener);

    }

    //防止多次点击后提示时间延长
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showTextToast(msg.obj.toString());
                    if(msg.obj.toString().contains("登录成功"))
                    {
                        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("username",username);
                        ed.putString("password",password);
                        ed.putString("userinfo",msg.obj.toString());
                        ed.commit();

                        Intent it = new Intent(LoginActivity.this, UserDetailActivity.class);
                        // TODO Auto-generated method stub
                        startActivityForResult(it, 1);
                        finish();//关闭当前登录界面，防止后退键回到登录界面
                    }
                    //然后处理登入后的事件

                    break;
                case 1:
                    showTextToast("网络连接失败");
                    break;
                case 2:
                    showTextToast("用户名或密码不能为空");
                    break;
                case 3:
                    showTextToast("服务器获取资料失败");
            }
        }


    };


    private String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        //16位加密，从第9位到25位
        //return md5StrBuff.substring(8, 24).toString().toUpperCase();
        return md5StrBuff.toString();
    }


    //检查网络是否可用
    public boolean isNetworkAvailable(ActionBarActivity activity) {
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


    //自动登陆测试
    private boolean checkLogin()
    {
        SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
        userinfo= sp.getString("userinfo", "");
        username=sp.getString("username", "");
        password=sp.getString("password", "");
        if(username!=null&&password!=null)
        {
            //测试登陆
            if(true)
               return true;
            else
           {
               //用户名密码错误
               return false;
           }
        }
        else
        {
            //采取正常登陆模式
            return false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

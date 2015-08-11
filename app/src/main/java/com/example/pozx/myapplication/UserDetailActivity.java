package com.example.pozx.myapplication;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class UserDetailActivity extends ActionBarActivity {

    private TextView usernameTv;
    private String userinfo="";
    private UploadManager uploadManager;
    private volatile String key;
    private volatile ResponseInfo info;
    private volatile JSONObject resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        usernameTv = (TextView)findViewById(R.id.username_tv);
        SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
        userinfo= sp.getString("userinfo", "");

        //String s="[{'id':'001','content':'测试数据','date':'2015-10-10'},{'id':'001','content':'测试数据','date':'2015-10-10'}]";

       try
       {
           JSONArray uinfo= new JSONArray(userinfo);
           for (int i = 0; i < uinfo.length(); i++) {
               JSONObject jsonObj = uinfo.optJSONObject(i);
               usernameTv.setText(jsonObj.get("username").toString());
           }
       }
        catch(Exception e)
        {
            Log.i("UserInfo_Error：", e.toString());
        }




    }

    //简单上传
    public void upLoad()
    {
        String data = "";
        String key ="";
        String token = "";
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", info.toString());
                    }
                }, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
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

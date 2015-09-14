package com.example.pozx.myapplication;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pozx.myapplication.model.Tools;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDetailActivity extends ActionBarActivity {

    private TextView usernameTv;
    private TextView imagepathTv;
    private TextView showtokenTv;
    private String userinfo="";
    private Button getTokenBt;
    private String username="";
    private UploadManager uploadManager;
    private volatile String key;
    private volatile ResponseInfo info;
    private volatile JSONObject resp;
    private ImageView headimgIv;
    private String imagepath="";
    private String uptoken;

    /*组件*/
    private RelativeLayout switchAvatar;

    private String[] items = new String[] { "选择本地图片", "拍照" };
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int SELECT_PIC_KITKAT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        usernameTv = (TextView)findViewById(R.id.username_tv);
        headimgIv =(ImageView)findViewById(R.id.image_head);
        imagepathTv = (TextView)findViewById(R.id.image_Path);
        showtokenTv = (TextView)findViewById(R.id.token_tv);

        getTokenBt =(Button)findViewById(R.id.get_token_bt);

        SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
        userinfo= sp.getString("userinfo", "");

        UploadManager uploadManager = new UploadManager();



        headimgIv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                new Thread() {
//                    @Override
//                    public void run() {
//
//                    }
//                }.start();
                showDialog();

            }
        });

        getTokenBt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //getToken();
                if(imagepath!="")
                getToken();
            }
        });

       try
       {
           JSONArray uinfo= new JSONArray(userinfo);
           for (int i = 0; i < uinfo.length(); i++) {
               JSONObject jsonObj = uinfo.optJSONObject(i);
               usernameTv.setText(jsonObj.get("username").toString());
               username=jsonObj.get("username").toString();
           }
       }
        catch(Exception e)
        {
            Log.i("UserInfo_Error:", e.toString());
        }
    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Log.i("showDialog:","here");
                                Intent intentFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intentFromGallery,SELECT_PIC_KITKAT);
                                    Log.i("SELECT--showDialog:", "here");
                                } else {
                                    startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
                                    Log.i("IMAGE--showDialog:", "here");
                                }
                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (Tools.hasSdcard()) {

                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }

                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    imagepath=data.getData().getPath();
                    imagepathTv.setText(imagepath);
                    Log.i("IMAGE_REQUEST_CODE:","here");
                    break;
                case SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    imagepath=data.getData().getPath();
                    imagepathTv.setText(imagepath);
                    Log.i("SELECT_PIC_KITKAT:", "here");
                    break;
                case CAMERA_REQUEST_CODE:
                    Log.i("CAMERA_REQUEST_CODE:","here");
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                , IMAGE_FILE_NAME);
                        imagepath=tempFile.getAbsolutePath();
                        imagepathTv.setText(imagepath);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(UserDetailActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
            super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url=getPath(UserDetailActivity.this,uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void getImageToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            headimgIv.setImageDrawable(drawable);

        }
    }



    @SuppressLint("NewApi")
    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//头像上传功能开发中。。。（2015-8-26）
//还差图像绝对地址的获取 （2015-9-12）
    public void upLoad(String str)
    {


        String data = str;
        String ext = "";
        String[] tempStr=str.split("\\.");
        ext = tempStr[tempStr.length-1];

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        String key =username+"_"+ts+"_headimage."+ext;
        String token="";

        try
        {
            token = getJsonToken(uptoken);
        }
        catch(JSONException e)
        {
            Log.i("Token-getJsonToken:",e.toString());
        }
        //Log.i("token",uptoken);

        UploadManager uploadManager = new UploadManager();
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", info.toString());
                    }
                }, null);
    }

    private String getJsonToken(String str) throws JSONException{

            JSONObject jsonObj=new JSONObject(str);
            String token=jsonObj.getString("token");
            return  token;
    }

    private void getToken()
    {
        new Thread() {
                    @Override
                    public void run() {
                        HttpPost httpPost = new HttpPost("http://pozxblog.duapp.com/index.php/home/index/getToken");
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("username", username));
                        Message msg = handler.obtainMessage();
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                //通过PHP获取mysql上的数据
                                uptoken = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                                msg.what=0;
                                msg.obj=uptoken;

                                handler.sendMessage(msg);
                                //tvShow.setText(strResult);
                            } else {
                                //tvShow.setText("Error");
                                //str = "Error";
                                handler.sendEmptyMessage(1);
                            }
                        } catch (Exception e) {
                            Log.e("Error: ", "Can't connect!  " + e.toString());
                            //执行完毕后给handler发送一个空消息
                            handler.sendEmptyMessage(2);
                        }
                    }

                }.start();

    }



    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showtokenTv.setText(msg.obj.toString());
                    upLoad(imagepath);
                    break;
                case 1:
                    showtokenTv.setText("接口访问失败");
                    break;
                case 2:
                    showtokenTv.setText("网络请求失败");
                    break;
                default:break;
            }
        }


    };

//去除空格，换行符，回车等
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
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

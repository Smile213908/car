package com.example.hotel.ui;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotel.R;

import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.util.Map;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private Button btn;
    public static String token, customerName, customerLogo,account,password;
    private EditText account1,password1;
    private Double code;
    public static final MediaType JSON
            =MediaType.parse("application/json; charset=utf-8");
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int POST = 2 ;
    private OkHttpClient client = new OkHttpClient();
    private static final int GET = 1;
    private String result;
    private Handler handlerlogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    code = (Double) jsonMap.get("code");
                    if(code == 200.0){
                        token = data.get("token").toString();   //token
                        Map userInfo = (Map) data.get("userInfo");
                        customerName = userInfo.get("customerName").toString();  //客户名称
                        customerLogo = userInfo.get("customerLogo").toString();  //客户logo
                        Intent intent=new Intent(LoginActivity.this,CheckActivity.class);
                        startActivity(intent);
                    }
                    Toast.makeText(getApplicationContext(),jsonMap.get("msg").toString(), Toast.LENGTH_SHORT).show();

                    break;
            }

        }
    };

    //重写oncreate方法
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_login);

        //获取控件
        btn = findViewById(R.id.btn_login_submit);
        account1 = findViewById(R.id.ed_account);
        password1 =findViewById(R.id.ed_password);
        //设置控件事件
        setViewEventListener();
    }


    //设置控件事件y
    private void setViewEventListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                account =account1.getText().toString();
                account = "北汽芜湖分厂";
//                account = "润模001";
//                password = password1.getText().toString();
                password = "123456";
                postLogin();
//                //延时等待响应
//                try {
//                    Thread.sleep(8000);
//                } catch (InterruptedException e) {
//                    return;
//                }
//                if(bool1){
//                    Intent intent=new Intent(LoginActivity.this,CheckActivity.class);
//                    startActivity(intent);
//                    bool1=false;
//                }else {
//                    Toast.makeText(getApplicationContext(),"帐号信息错误", Toast.LENGTH_SHORT).show();
//                }



            }

        });

    }
    private void postLogin() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/user/login", "{\n" +
                            "    \"account\":\""+account+"\",\n" +
                            "    \"password\":\""+password+"\"\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerlogin.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    private String post(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}

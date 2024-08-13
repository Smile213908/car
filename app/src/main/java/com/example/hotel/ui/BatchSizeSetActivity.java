package com.example.hotel.ui;
import static android.content.ContentValues.TAG;
import static com.example.hotel.ui.LoginActivity.JSON;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BatchSizeSetActivity extends AppCompatActivity {
    private Button btnAccept;
    private Button btnRefuse;
    private String beforeText;
    protected EditText edUp;
    protected EditText edDown;
    protected EditText edCorrect;
    private String result;
    protected SharedPreferences error_data;
    protected String upperTolerance;
    protected String lowerTolerance;
    protected String correctValue;
    protected Double code;
    private static final int POST = 2;
    private OkHttpClient client = new OkHttpClient();
    private Handler handlerbatchupdate = new Handler() {
        @SuppressLint("HandlerLeak")
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
                    Toast.makeText(getApplicationContext(),jsonMap.get("msg").toString(), Toast.LENGTH_SHORT).show();



                    break;
            }
        }
    };


    //重写OnCreate方法
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置注册界面 （跳转）到layout界面
        setContentView(R.layout.activity_batchsize_set);





        //获取控件
        btnAccept = findViewById(R.id.btn_accept);
        btnRefuse = findViewById(R.id.btn_refuse);
        edUp = findViewById(R.id.ed_up);
        edDown = findViewById(R.id.ed_down);
        edCorrect = findViewById(R.id.ed_correct);


        //设置控件事件
        setViewEventListener();
        error_data = getSharedPreferences("error_data", MODE_PRIVATE);

    }


    //设置控件事件y
    private void setViewEventListener() {
        btnAccept.setOnClickListener(v -> {



            //事件函数内容
            ErrorRangeActivity err = new ErrorRangeActivity();
             upperTolerance = edUp.getText().toString();
             lowerTolerance = edDown.getText().toString();
             correctValue = edCorrect.getText().toString();

            if(!upperTolerance.equals("")&&!lowerTolerance.equals("")&&!correctValue.equals("")) {

                String up = upperTolerance.trim();
                String down = lowerTolerance.trim();
                String edc  = correctValue.trim();
                int posup = up.indexOf(".");
                int posdown = down.indexOf(".");
                int posedc = edc.indexOf(".");
                if (posup != 0 && posdown!=0 && posedc!=0) {
                    //存储数据
                    SharedPreferences.Editor editor = error_data.edit();//获取编辑器对象


                    String[][] arr3 = {
                            {"f1.1", "f2.1", "f3.1", "f4.1", "f5.1", "f6.1", "f7.1", "f8.1", "f9.1", "f10.1"},
                            {"f1.2", "f2.2", "f3.2", "f4.2", "f5.2", "f6.2", "f7.2", "f8.2", "f9.2", "f10.2"},
                            {"f1.3", "f2.3", "f3.3", "f4.3", "f5.3", "f6.3", "f7.3", "f8.3", "f9.3", "f10.3"}
                    };
                    //如果输入的是.05形式把他保存为0.05


                    String[] arr2 = {upperTolerance, lowerTolerance, correctValue};

                    for (int i = 0; i < arr3.length; i++) {
                        for (int j = 0; j < arr3[i].length; j++) {
                            editor.putFloat(arr3[i][j], Float.parseFloat(arr2[i]));
                        }
                    }

                    editor.apply();//
                    postBatchupdate();

                    setResult(Activity.RESULT_OK, new Intent());

                    Intent intent = new Intent();
                    intent.setClass(BatchSizeSetActivity.this, ErrorRangeActivity.class);
                    startActivity(intent);
                    finish();



                } else {


                    Toast.makeText(BatchSizeSetActivity.this, "小数点之前增加“0", Toast.LENGTH_LONG).show();
                }

            }
        else {
                Toast.makeText(BatchSizeSetActivity.this, "无需填写请点击取消", Toast.LENGTH_LONG).show();
//                Log.d(TAG, "setViewEventListener: 请完善人员编号及测试编号！");
            }
        });

        btnRefuse.setOnClickListener(v -> {

            finish();

        });
    }
    private void postBatchupdate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/batchUpdate", "{\n" +
                            "    \"insToolId\":"+CheckActivity.insToolId+",\n" +
                            "    \"upperTolerance\":"+upperTolerance+",\n" +
                            "    \"lowerTolerance\":"+lowerTolerance+",\n" +
                            "    \"correctValue\":"+correctValue+"\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerbatchupdate.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private String post(String url, String json) throws IOException {
//        tv.setText(token);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("authorization", LoginActivity.token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

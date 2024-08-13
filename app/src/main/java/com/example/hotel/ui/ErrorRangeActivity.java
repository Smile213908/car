package com.example.hotel.ui;
import static com.example.hotel.ui.LoginActivity.JSON;
import static com.example.hotel.ui.LoginActivity.customerLogo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ErrorRangeActivity extends AppCompatActivity {
    public final int REQUEST_CODE_A = 1;
    private Button btnBatchSet;
    private Button btnDataSave;
    private Button btnDataRefresh, btnForward, btnBackward;
    private ImageView ivReturn;
    protected SharedPreferences error_data;
    protected TextView ed_f1_0,ed_f2_0,ed_f3_0,ed_f4_0,ed_f5_0,ed_f6_0,ed_f7_0,
            ed_f8_0,ed_f9_0,ed_f10_0;
    protected EditText ed_f1_1;
    protected EditText ed_f1_2;
    protected EditText ed_f1_3;
    protected EditText ed_f2_1;
    protected EditText ed_f2_2;
    protected EditText ed_f2_3;
    protected EditText ed_f3_1;
    protected EditText ed_f3_2;
    protected EditText ed_f3_3;
    protected EditText ed_f4_1;
    protected EditText ed_f4_2;
    protected EditText ed_f4_3;
    protected EditText ed_f5_1;
    protected EditText ed_f5_2;
    protected EditText ed_f5_3;
    protected EditText ed_f6_1;
    protected EditText ed_f6_2;
    protected EditText ed_f6_3;
    protected EditText ed_f7_1;
    protected EditText ed_f7_2;
    protected EditText ed_f7_3;
    protected EditText ed_f8_1;
    protected EditText ed_f8_2;
    protected EditText ed_f8_3;
    protected EditText ed_f9_1;
    protected EditText ed_f9_2;
    protected EditText ed_f9_3;
    protected EditText ed_f10_1;
    protected EditText ed_f10_2;
    protected EditText ed_f10_3;
    private String[][] arr;
    private String[][] arr_d1;
    private String[] pointCode_d;
    private String[] pointIds;

    private OkHttpClient client = new OkHttpClient();
    private static final int POST = 2;
    private Integer pageNum, pageSize;
    private String pointCode, result;
    public static Double total;
    private Double id, code;
    private Double pointId;
    private Double upperTolerance;
    private Double lowerTolerance;
    private Double correctValue;
    private String update_str;
    public static TextView textView;
    public ImageView imageView;
    private Handler handlerpoint = new Handler() {
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
                    total = (Double) data.get("total");

//                    total = 3.0;
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("list");  //列表

                    arr_d1 = new String[listtotal.size()][3];
                    pointCode_d =new String[listtotal.size()];
                    pointIds = new String[listtotal.size()];
                    for (Integer i = 0; i < listtotal.size(); i++) {
                        Map list = (Map) listtotal.get(i);
                        pointId = (Double) list.get("pointId");  //pointId
                        pointIds[i] = pointId.toString();
                        pointCode = (String) list.get("pointCode");

                        upperTolerance = (Double) list.get("upperTolerance");
                        lowerTolerance = (Double) list.get("lowerTolerance");
                        correctValue   = (Double) list.get("correctValue");
                        arr_d1[i][0]=upperTolerance.toString();
                        arr_d1[i][1]=lowerTolerance.toString();
                        arr_d1[i][2]=correctValue.toString();
                        pointCode_d[i]=pointCode;
                    }
                    download();
                    load();

                    break;
            }
        }
    };

    private Handler handlerupdate = new Handler() {
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_error_range);



        textView=findViewById(R.id.textView);
        textView.setText(LoginActivity.customerName);
        imageView=findViewById(R.id.imageView);
        downloadImage(LoginActivity.customerLogo,imageView);
        pageNum = 1;  //页码
        pageSize = 10;  //分页大小
        upperTolerance=0.6;
        lowerTolerance = -0.3;
        correctValue=1.0;

        //获取控件
        btnBatchSet = findViewById(R.id.btn_batch_set);
        btnDataRefresh = findViewById(R.id.btn_data_refresh);
        btnDataSave = findViewById(R.id.btn_data_save);
//        btnDataGet = findViewById(R.id.btn_data_get);
        btnForward = findViewById(R.id.forward);
        btnBackward = findViewById(R.id.backward);
        ivReturn = findViewById(R.id.iv_back);
        ed_f1_1 = findViewById(R.id.ed_f1_1);
        ed_f1_2 = findViewById(R.id.ed_f1_2);
        ed_f1_3 = findViewById(R.id.ed_f1_3);
        ed_f2_1 = findViewById(R.id.ed_f2_1);
        ed_f2_2 = findViewById(R.id.ed_f2_2);
        ed_f2_3 = findViewById(R.id.ed_f2_3);
        ed_f3_1 = findViewById(R.id.ed_f3_1);
        ed_f3_2 = findViewById(R.id.ed_f3_2);
        ed_f3_3 = findViewById(R.id.ed_f3_3);
        ed_f4_1 = findViewById(R.id.ed_f4_1);
        ed_f4_2 = findViewById(R.id.ed_f4_2);
        ed_f4_3 = findViewById(R.id.ed_f4_3);
        ed_f5_1 = findViewById(R.id.ed_f5_1);
        ed_f5_2 = findViewById(R.id.ed_f5_2);
        ed_f5_3 = findViewById(R.id.ed_f5_3);
        ed_f6_1 = findViewById(R.id.ed_f6_1);
        ed_f6_2 = findViewById(R.id.ed_f6_2);
        ed_f6_3 = findViewById(R.id.ed_f6_3);
        ed_f7_1 = findViewById(R.id.ed_f7_1);
        ed_f7_2 = findViewById(R.id.ed_f7_2);
        ed_f7_3 = findViewById(R.id.ed_f7_3);
        ed_f8_1 = findViewById(R.id.ed_f8_1);
        ed_f8_2 = findViewById(R.id.ed_f8_2);
        ed_f8_3 = findViewById(R.id.ed_f8_3);
        ed_f9_1 = findViewById(R.id.ed_f9_1);
        ed_f9_2 = findViewById(R.id.ed_f9_2);
        ed_f9_3 = findViewById(R.id.ed_f9_3);
        ed_f10_1 = findViewById(R.id.ed_f10_1);
        ed_f10_2 = findViewById(R.id.ed_f10_2);
        ed_f10_3 = findViewById(R.id.ed_f10_3);
        ed_f1_0 = findViewById(R.id.F1);
        ed_f2_0 = findViewById(R.id.F2);
        ed_f3_0 = findViewById(R.id.F3);
        ed_f4_0 = findViewById(R.id.F4);
        ed_f5_0 = findViewById(R.id.F5);
        ed_f6_0 = findViewById(R.id.F6);
        ed_f7_0 = findViewById(R.id.F7);
        ed_f8_0 = findViewById(R.id.F8);
        ed_f9_0 = findViewById(R.id.F9);
        ed_f10_0 = findViewById(R.id.F10);

        //设置控件事件
        setViewEventListener();

        error_data = getSharedPreferences("error_data", MODE_PRIVATE);
        postPoint(pageNum, pageSize);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


//

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_A:
                if (resultCode == Activity.RESULT_OK){
                    load();
                }else break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    protected void load(){
        float f1_1 = error_data.getFloat("f1.1", 1000f);
        float f1_2 = error_data.getFloat("f1.2", 1000f);
        float f1_3 = error_data.getFloat("f1.3", 1000f);
        float f2_1 = error_data.getFloat("f2.1", 1000f);
        float f2_2 = error_data.getFloat("f2.2", 1000f);
        float f2_3 = error_data.getFloat("f2.3", 1000f);
        float f3_1 = error_data.getFloat("f3.1", 1000f);
        float f3_2 = error_data.getFloat("f3.2", 1000f);
        float f3_3 = error_data.getFloat("f3.3", 1000f);
        float f4_1 = error_data.getFloat("f4.1", 1000f);
        float f4_2 = error_data.getFloat("f4.2", 1000f);
        float f4_3 = error_data.getFloat("f4.3", 1000f);
        float f5_1 = error_data.getFloat("f5.1", 1000f);
        float f5_2 = error_data.getFloat("f5.2", 1000f);
        float f5_3 = error_data.getFloat("f5.3", 1000f);
        float f6_1 = error_data.getFloat("f6.1", 1000f);
        float f6_2 = error_data.getFloat("f6.2", 1000f);
        float f6_3 = error_data.getFloat("f6.3", 1000f);
        float f7_1 = error_data.getFloat("f7.1", 1000f);
        float f7_2 = error_data.getFloat("f7.2", 1000f);
        float f7_3 = error_data.getFloat("f7.3", 1000f);
        float f8_1 = error_data.getFloat("f8.1", 1000f);
        float f8_2 = error_data.getFloat("f8.2", 1000f);
        float f8_3 = error_data.getFloat("f8.3", 1000f);
        float f9_1 = error_data.getFloat("f9.1", 1000f);
        float f9_2 = error_data.getFloat("f9.2", 1000f);
        float f9_3 = error_data.getFloat("f9.3", 1000f);
        float f10_1 = error_data.getFloat("f10.1", 1000f);
        float f10_2 = error_data.getFloat("f10.2", 1000f);
        float f10_3 = error_data.getFloat("f10.3", 1000f);
        String f1_0=error_data.getString("f1.0","F1");
        String f2_0=error_data.getString("f2.0","F2");
        String f3_0=error_data.getString("f3.0","F3");
        String f4_0=error_data.getString("f4.0","F4");
        String f5_0=error_data.getString("f5.0","F5");
        String f6_0=error_data.getString("f6.0","F6");
        String f7_0=error_data.getString("f7.0","F7");
        String f8_0=error_data.getString("f8.0","F8");
        String f9_0=error_data.getString("f9.0","F9");
        String f10_0=error_data.getString("f10.0","F10");

        float[][] arr = {{f1_1, f1_2, f1_3}, {f2_1, f2_2, f2_3}, {f3_1, f3_2, f3_3}, {f4_1, f4_2, f4_3},
                {f5_1, f5_2, f5_3}, {f6_1, f6_2, f6_3}, {f7_1, f7_2, f7_3}, {f8_1, f8_2, f8_3}, {f9_1, f9_2, f9_3},
                {f10_1, f10_2, f10_3}};
        String[] arr8 ={f1_0,f2_0,f3_0,f4_0,f5_0,f6_0,f7_0,f8_0,f9_0,f10_0};
        EditText[][] arr2 = {{ed_f1_1, ed_f1_2, ed_f1_3},  {ed_f2_1, ed_f2_2, ed_f2_3},
                {ed_f3_1, ed_f3_2, ed_f3_3},  {ed_f4_1, ed_f4_2, ed_f4_3},
                {ed_f5_1, ed_f5_2, ed_f5_3},  {ed_f6_1, ed_f6_2, ed_f6_3},
                {ed_f7_1, ed_f7_2, ed_f7_3},  {ed_f8_1, ed_f8_2, ed_f8_3},
                {ed_f9_1, ed_f9_2, ed_f9_3},  {ed_f10_1, ed_f10_2, ed_f10_3}
        };
        TextView[] arrtv ={ed_f1_0,ed_f2_0,ed_f3_0,ed_f4_0,
                ed_f5_0,ed_f6_0,ed_f7_0,ed_f8_0,ed_f9_0,ed_f10_0};
        for(int i = 0;i<10;i++) {
            if (i < arr_d1.length) {
                for (int j = 0; j < arr_d1[i].length; j++) {
                    if (arr[i][j] != 1000f) {
                        arr2[i][j].setText(String.valueOf(arr[i][j]));
                        arr2[i][j].setVisibility(View.VISIBLE);
                    }
                    arrtv[i].setText(arr8[i]);
                    arrtv[i].setVisibility(View.VISIBLE);
                }
            }else{
                for (int j = 0; j < 3; j++) {
//                      ed_f8_1.setVisibility(View.GONE);
                    arr2[i][j].setVisibility(View.INVISIBLE);
                }
                arrtv[i].setVisibility(View.INVISIBLE);
            }
        }

//        for(int i = 0;i<10;i++){
//            for(int j = 0;j<arr[i].length;j++){
//                if (arr[i][j] != 1000f) {
//                    arr2[i][j].setText(String.valueOf(arr[i][j]));
//                }
//
//            }
//            arrtv[i].setText(arr8[i]);
//        }

    }
    private void postPoint(Integer pageNum, Integer pageSize) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/list", "{\n" +
                            "    \"pageNum\":"+pageNum+",\n" +
                            "    \"pageSize\":"+pageSize+",\n" +
                            "    \"insToolId\": "+CheckActivity.insToolId+""+
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerpoint.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    private void download(){
        //存储数据
        SharedPreferences.Editor editor = error_data.edit();//获取编辑器对象


        String[][] arr_d2 = {{"f1.1","f1.2","f1.3","f1.0"},{"f2.1","f2.2","f2.3","f2.0"},{"f3.1","f3.2","f3.3","f3.0"},
                {"f4.1","f4.2","f4.3","f4.0"},{"f5.1","f5.2","f5.3","f5.0"},{"f6.1","f6.2","f6.3","f6.0"},
                {"f7.1","f7.2","f7.3","f7.0"},{"f8.1","f8.2","f8.3","f8.0"},{"f9.1","f9.2","f9.3","f9.0"},
                {"f10.1","f10.2","f10.3","f10.0"}};


        for(int i = 0;i<10;i++){
            if(i<arr_d1.length){
                for(int j = 0;j<arr_d1[i].length;j++){
                    editor.putFloat(arr_d2[i][j], Float.parseFloat(arr_d1[i][j]));
                }
                editor.putString(arr_d2[i][3], pointCode_d[i]);
            }else {
                for(int j = 0;j<3;j++){
                    editor.putFloat(arr_d2[i][j], 0);
                }
                editor.putString(arr_d2[i][3], "");

            }

        }


        editor.apply();//数据提交

    }


    //设置控件事件y
    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容

            Intent intent = new Intent();
            intent.setClass(ErrorRangeActivity.this,MeasureActivity.class);
            startActivity(intent);
            finish();

        });
        btnBatchSet.setOnClickListener( v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(ErrorRangeActivity.this,BatchSizeSetActivity.class);
            startActivity(intent);

        });
        btnDataRefresh.setOnClickListener(v -> {
            //获取数据重载
            load();
        });

        btnForward.setOnClickListener(v -> {
            //上一页
            pageNum -= 1;
            if(pageNum<=0){
                pageNum = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();

            }
            postPoint(pageNum, pageSize);
        });
        btnBackward.setOnClickListener(v -> {
            //下一页
            if(total-pageNum*10>0){
                pageNum += 1;
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();

            }
            postPoint(pageNum, pageSize);
        });

        btnDataSave.setOnClickListener(v -> {
            //数据保存
            String f1_1 = ed_f1_1.getText().toString();
            String f1_2 = ed_f1_2.getText().toString();
            String f1_3 = ed_f1_3.getText().toString();
            String f2_1 = ed_f2_1.getText().toString();
            String f2_2 = ed_f2_2.getText().toString();
            String f2_3 = ed_f2_3.getText().toString();
            String f3_1 = ed_f3_1.getText().toString();
            String f3_2 = ed_f3_2.getText().toString();
            String f3_3 = ed_f3_3.getText().toString();
            String f4_1 = ed_f4_1.getText().toString();
            String f4_2 = ed_f4_2.getText().toString();
            String f4_3 = ed_f4_3.getText().toString();
            String f5_1 = ed_f5_1.getText().toString();
            String f5_2 = ed_f5_2.getText().toString();
            String f5_3 = ed_f5_3.getText().toString();
            String f6_1 = ed_f6_1.getText().toString();
            String f6_2 = ed_f6_2.getText().toString();
            String f6_3 = ed_f6_3.getText().toString();
            String f7_1 = ed_f7_1.getText().toString();
            String f7_2 = ed_f7_2.getText().toString();
            String f7_3 = ed_f7_3.getText().toString();
            String f8_1 = ed_f8_1.getText().toString();
            String f8_2 = ed_f8_2.getText().toString();
            String f8_3 = ed_f8_3.getText().toString();
            String f9_1 = ed_f9_1.getText().toString();
            String f9_2 = ed_f9_2.getText().toString();
            String f9_3 = ed_f9_3.getText().toString();
            String f10_1 = ed_f10_1.getText().toString();
            String f10_2 = ed_f10_2.getText().toString();
            String f10_3 = ed_f10_3.getText().toString();
            //存储数据
            SharedPreferences.Editor editor = error_data.edit();//获取编辑器对象

            arr = new String[][]{{f1_1, f1_2, f1_3}, {f2_1, f2_2, f2_3}, {f3_1, f3_2, f3_3}, {f4_1, f4_2, f4_3},
                    {f5_1, f5_2, f5_3}, {f6_1, f6_2, f6_3}, {f7_1, f7_2, f7_3}, {f8_1, f8_2, f8_3}, {f9_1, f9_2, f9_3},
                    {f10_1, f10_2, f10_3}};

            String[][] arr3 = {{"f1.1","f1.2","f1.3"},{"f2.1","f2.2","f2.3"},{"f3.1","f3.2","f3.3"},
                    {"f4.1","f4.2","f4.3"},{"f5.1","f5.2","f5.3"},{"f6.1","f6.2","f6.3"},
                    {"f7.1","f7.2","f7.3"},{"f8.1","f8.2","f8.3"},{"f9.1","f9.2","f9.3"},
                    {"f10.1","f10.2","f10.3"}};


            for(int i = 0;i<arr.length;i++){
                for(int j = 0;j<arr[i].length;j++){
                    if (!arr[i][j].equals("")) {
                        editor.putFloat(arr3[i][j], Float.parseFloat(arr[i][j]));
                    }
                }

            }

            editor.apply();//数据提交

            postUpdate();
//            Toast.makeText(getApplicationContext(),"保存成功", Toast.LENGTH_SHORT).show();

        });

//        btnDataGet.setOnClickListener(v -> {
//            //事件函数内容
//            postPoint(pageNum, pageSize);
//
//        });
    }
    private void postUpdate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    update_str = "[\n";
                    for(int i = 0;i<arr_d1.length;i++){ //arr_d1.length

                        update_str+= "   {\n" +
                                     "    \"pointId\":"+Float.parseFloat(pointIds[i])+",\n" +
                                     "    \"insToolId\":"+CheckActivity.insToolId+",\n" +
                                     "    \"upperTolerance\":"+Float.parseFloat(arr[i][0])+",\n" +
                                     "    \"lowerTolerance\":"+Float.parseFloat(arr[i][1])+",\n" +
                                     "    \"correctValue\":"+Float.parseFloat(arr[i][2])+"\n" +
                                     "    }";
                        if(i<arr_d1.length-1){
                            update_str+=",";
                        }

                    }
                    update_str+="]";

                    result = post("http://81.69.170.198:8002/point/update", update_str);


                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerupdate.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private Bitmap bitmap;
    private void downloadImage(String imageUrl, ImageView img) {
//        tv.setText(PATH);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();
        try {
            Call call = client.newCall(request); // 使用client去请求

            call.enqueue(new Callback() { // 回调方法，>>> 可以获得请求结果信息
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "onFailure: 失败");
                    showUI(null, img);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
//                    tv.setText("11"+response.code());
                    if (200 == response.code()) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        showUI(bitmap, img);
                    } else {
                        showUI(null, img);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showUI(null, img);
        }
    }
    private void showUI(final Bitmap bitmap, ImageView img) {
        this.bitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {

                    // 故意放慢两秒，模仿网络差的效果
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 设置从网上下载的图片
                            img.setImageBitmap(bitmap);

                        }
                    }, 20);
                } else { //失败

                    Toast.makeText(getApplicationContext(), "下载失败,请检查原因", Toast.LENGTH_LONG).show();

                }
            }
        });
    }



}

package com.example.hotel.ui;

import static android.content.ContentValues.TAG;
import static com.example.hotel.ui.CheckActivity.insToolName;
import static com.example.hotel.ui.CheckActivity.pointNumber;
import static com.example.hotel.ui.CheckActivity.point_num;
import static com.example.hotel.ui.LoginActivity.customerLogo;
import static com.example.hotel.util.port.JSON;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckAjustActivity extends AppCompatActivity {
    private ImageView ivReturn;
    public ImageView imageView,mZoomImageview;
    private Button    btnAdjust;
    private Button    btnSave;
    private Button    btnforward;
    private Button    btnbackward;
    private ZoomImageView mZoomImageView;
    private OkHttpClient client = new OkHttpClient();
    private String result, str;
    public static String[] pointId_total,pointCode_total;
    private static final int POST = 2;
    private Double code, n, total;
    //存储数据
    public static SharedPreferences points_data;
    public static Float[][] pointSave;
    private Handler handlersave = new Handler() {
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
    private Handler handlerpointtotal = new Handler() {
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
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("list");  //列表

                    pointCode_total =new String[listtotal.size()];
                    pointId_total = new String[listtotal.size()];
                    for (Integer i = 0; i < listtotal.size(); i++) {
                        Map list = (Map) listtotal.get(i);
                        Double pointId = (Double) list.get("pointId");  //pointId
                        pointId_total[i] = pointId.toString();
                        String pointCode = (String) list.get("pointCode");
                        pointCode_total[i]=pointCode;
                    }
                    break;
            }
        }
    };
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
                    Log.d(TAG, "total"+total);
                    postPointtotal(total);
                    break;
            }
        }
    };
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_check_adjust);

        ivReturn = findViewById(R.id.iv_back);
        btnAdjust = findViewById(R.id.btn_point_adjust1);
        btnSave = findViewById(R.id.btn_point_save);
        btnforward = findViewById(R.id.point_forward);
        btnbackward = findViewById(R.id.point_backward);
        mZoomImageView = findViewById(R.id.my_image_view_adjust);
        imageView=findViewById(R.id.imageView);
        downloadImage(LoginActivity.customerLogo,imageView);
        mZoomImageView=findViewById(R.id.my_image_view_adjust);
        downloadImage(CheckActivity.insToolimage,mZoomImageView);

        postPoint(1,6);


        points_data = getSharedPreferences("error_data", MODE_PRIVATE);


        //设置控件事件
        setViewEventListener();
    }



    //存储数据
    public boolean savePoints(){
        if(mZoomImageView.isDragWidgetMode()){
            int i=0;
//            SharedPreferences points_data = this.points_data;
            //获取编辑器对象
//            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = points_data.edit();
            Set<Integer> integers = mZoomImageView.mDrawableMap.keySet();
            pointSave = new Float[integers.size()][3];
//            Log.d(TAG, "savePoints: pointSave.length"+pointSave.length);

//            for (Integer ketSet:integers) {
//                float cx=mZoomImageView.mDrawableMap.get(ketSet).getCoordinateX();
//                float cy=mZoomImageView.mDrawableMap.get(ketSet).getCoordinateY();
//                Log.d(TAG, "savePoints: cx"+cx);
//                pointSave[i][0] = cx;
//                pointSave[i][1] = cy;
            for (Integer ketSet:integers) {
                float cx=mZoomImageView.mDrawableMap.get(ketSet).getCoordinateX(mZoomImageView.getImageSize());
                float cy=mZoomImageView.mDrawableMap.get(ketSet).getCoordinateY(mZoomImageView.getImageSize());
                pointSave[i][0] = cx/mZoomImageView.getImageSize().x;
                pointSave[i][1] = cy/mZoomImageView.getImageSize().y;
//                editor.putFloat("Z"+i +".x", cx);
//                editor.putFloat("Z"+i+".y", cy);
                i++;

            }
//            Log.d(TAG, "handleMessage: imageSize.x+Chek"+ZoomImageView.imageSize.x);
//            Log.d(TAG, "handleMessage: imageSize.x+cx"+ pointSave[1][0]);
//            Log.d(TAG, "handleMessage: imageSize.x+cx"+ pointSave[1][0]);
//            editor.apply();//数据提交
            return true;
        }
        return false;
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

    private void postPointtotal(Double pageSize) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/list", "{\n" +
                            "    \"pageNum\":"+1+",\n" +
                            "    \"pageSize\":"+pageSize+",\n" +
                            "    \"insToolId\": "+CheckActivity.insToolId+""+
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerpointtotal.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }



    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            Intent intent=new Intent(this,MeasureActivity.class);
            startActivity(intent);
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.num = 1;
            ZoomImageView.size_data = true ;
        });
        int vlog[]=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        btnAdjust.setOnClickListener(v -> {
            //事件函数内容
            if (vlog[0] == 0)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnAdjust.setBackground(getDrawable(R.drawable.btn_ok));
                }
                vlog[0] = 1;
            }
            mZoomImageView.setDragMode(true);
        });

        btnSave.setOnClickListener(v -> {
            //事件函数内容
            mZoomImageView.setDragMode(true);
            savePoints();
            postSave();
            if (vlog[0] == 1)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnAdjust.setBackground(getDrawable(R.drawable.btn));
                }
                vlog[0]=0;
            }
            mZoomImageView.setDragMode(false);

        });
        btnforward.setOnClickListener(v -> {
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.num -=1;
            ZoomImageView.size_data = true ;
            if(ZoomImageView.num==0){
                ZoomImageView.num = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, CheckAjustActivity.class);
            startActivity(intent);
        });
        btnbackward.setOnClickListener(v -> {
//            Log.d(TAG, "您读到"+point_num);

            if(ZoomImageView.num < point_num){
                ZoomImageView.mDrawableMap.clear();
                ZoomImageView.size_data = true ;
                ZoomImageView.num += 1;
                Intent intent = new Intent(this, CheckAjustActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void postSave() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    str = "{\n" +
                            "    \"insToolId\":"+CheckActivity.insToolId+",\n" +

                            "    \"pointInfoList\":[\n";

                    for(int i=0; i<pointSave.length; i++){
                        str +=  "   {\n" +
                                "    \"pointId\":"+pointId_total[i]+",\n" +
                                "    \"propLeft\":"+pointSave[i][0]+",\n" +
                                "    \"propTop\":"+pointSave[i][1]+"\n" +
                                "    }";
                        if(i<pointSave.length-1){
                            str+=",";
                        }

                    }
                    str+="] \n" +
                            "}";

//                str="{\n" +
//                        "    \"insToolId\":"+CheckActivity.insToolId+",\n" +
//
//                        "    \"pointInfoList\":[\n"+
//                        "   {\n" +
//                        "    \"pointId\":"+pointId_total[0]+",\n" +
//                        "    \"propLeft\":"+pointSave[0][0]+",\n" +
//                        "    \"propTop\":"+pointSave[0][1]+"\n" +
//                        "        }]\n" +
//                        "}";

                    result = post("http://81.69.170.198:8002/point/savePropLocation", str);

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlersave.sendMessage(msg);
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

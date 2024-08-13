package com.example.hotel.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChaoticMeasureActivity extends AppCompatActivity {

    private Button btnWriteMessage;
    private Button btnConnectionSetting;
    private Button btnStartMeasure;
    private TextView tv_alert;
    private ImageView ivReturn;
    private TextView TextView1;
    public static LinearLayout llZ10;
    public ImageView imageView,chaimageview;
    public static ZoomImageView ziv;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_chaotic_measure);

        imageView =findViewById(R.id.imageView );
        downloadImage(LoginActivity.customerLogo,imageView);
//
        chaimageview =findViewById(R.id.my_image_view5);
        downloadImage(CheckActivity.insToolimage,chaimageview);

        //浮动
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        View rootView = LayoutInflater.from(ChaoticMeasureActivity.this).inflate(
                R.layout.activity_fram, null);
        frame.addView(rootView);

        //获取控件
        btnWriteMessage = findViewById(R.id.btn_write_inf);
        btnConnectionSetting = findViewById(R.id.btn_connection_setting);
        btnStartMeasure = findViewById(R.id.btn_start_measure);
        ivReturn = findViewById(R.id.iv_back);
        TextView1=findViewById(R.id.textView);
        TextView1.setText(LoginActivity.customerName);
        llZ10 =findViewById(R.id.ll_Z10);


//        ziv =findViewById(R.id.my_image_view5);
//        ziv.invalidate();
        //设置控件事件
        setViewEventListener();
//        TextView1.setText(LoginActivity.customerName);
    }
    //设置控件事件y
    @Override
    protected void onResume() {
        super.onResume();
        if (MeasureActivity.logB==1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnConnectionSetting.setBackground(getDrawable(R.drawable.btn_ok));
            }
        }

    }
    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            finish();
        });
        btnWriteMessage.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(ChaoticMeasureActivity.this, WriteMessageActivity.class);
            startActivity(intent);
        });

        btnConnectionSetting.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(ChaoticMeasureActivity.this, BluetoothConnectActivity.class);
            startActivity(intent);
        });
        if (MeasureActivity.logB==1){
            Log.d(String.valueOf(ChaoticMeasureActivity.this), "setViewEventListener: 12sadj");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnConnectionSetting.setBackground(getDrawable(R.drawable.btn_ok));
            }
        }

        btnStartMeasure.setOnClickListener(v -> {
            //事件函数内容
            if(WriteMessageActivity.checkUser==null||WriteMessageActivity.checkCode==null ){

                Toast.makeText(this, "请填写资料！", Toast.LENGTH_LONG).show();


            }else {
                Intent intent = new Intent();
                intent.setClass(ChaoticMeasureActivity.this, StartMeasureActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
                startActivity(intent);
            }

        });


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


package com.example.hotel.ui;

import static com.example.hotel.ui.LoginActivity.customerLogo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeasureActivity extends AppCompatActivity {
    private Button btnPointSet;
    private Button btnPointAdjust;
    private Button btnOrderedMeasure;
    private Button btnDisorderedMeasure;
    private Button btnOutcome;
    private Button btnUploadData;
    private ImageView ivReturn;
    private TextView textView1;
    public ImageView imageView;
    public static TextView textView;
    public static Integer logB;
//    public static Integer flag;

    //重写oncreate方法
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_measure);

        textView=findViewById(R.id.textView);
        textView.setText(LoginActivity.customerName);
        imageView=findViewById(R.id.imageView);
        downloadImage(LoginActivity.customerLogo,imageView);
        logB = 0;


        //获取控件
        btnPointSet = findViewById(R.id.btn_point_set);
        btnPointAdjust = findViewById(R.id.btn_point_adjust);
        btnOrderedMeasure = findViewById(R.id.btn_ordered);
        btnDisorderedMeasure = findViewById(R.id.btn_disordered);
        btnOutcome = findViewById(R.id.btn_outcome_review);
        btnUploadData = findViewById(R.id.btn_upload_data);
        ivReturn = findViewById(R.id.iv_back);
        textView1= findViewById(R.id.textView1);

        //设置控件事件
        setViewEventListener();
//        textView1.setText(LoginActivity.customerName);
    }

    //设置控件事件y
    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,CheckActivity.class);
            startActivity(intent);
//            finish();
        });
        btnPointSet.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,ErrorRangeActivity.class);
            startActivity(intent);
        });

        btnPointAdjust.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,CheckAjustActivity.class);
            startActivity(intent);
        });

        btnDisorderedMeasure.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,ChaoticMeasureActivity.class);
            startActivity(intent);
        });

        btnOrderedMeasure.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,proceedingMeasurement.class);
            startActivity(intent);
        });

        btnOutcome.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,ErrorRangeActivity.class);
            startActivity(intent);
        });

        btnUploadData.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(MeasureActivity.this,ErrorRangeActivity.class);
            startActivity(intent);
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

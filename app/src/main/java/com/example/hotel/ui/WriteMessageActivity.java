package com.example.hotel.ui;

import static android.content.ContentValues.TAG;
import static com.example.hotel.ui.LoginActivity.JSON;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotel.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WriteMessageActivity extends AppCompatActivity {
    private Button btnConfirm;

    private ImageView ivReturn;
    protected EditText ed_checkBatchNumber,ed_checkUser,ed_checkCode;
    public static String checkBatchNumber,checkUser,checkCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_write_message);
        ed_checkBatchNumber=findViewById(R.id.checkBatchNumber);
        ed_checkCode = findViewById(R.id.checkCode);
        ed_checkUser =findViewById(R.id.checkUser);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivReturn = findViewById(R.id.iv_back);
        setViewEventListener();
    }

    private void setViewEventListener() {

        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(WriteMessageActivity.this,proceedingMeasurement.class);
            startActivity(intent);
            finish();
        });

        btnConfirm.setOnClickListener(v -> {
            //事件函数内容
            checkBatchNumber=ed_checkBatchNumber.getText().toString();
            checkUser=ed_checkUser.getText().toString();
            checkCode=ed_checkCode.getText().toString();
            if(!checkUser.equals("")&&!checkCode.equals("")){
                finish();
            }else {
                Toast.makeText(WriteMessageActivity.this, "请完善人员编号及测试编号！", Toast.LENGTH_LONG).show();
                Log.d(TAG, "setViewEventListener: 请完善人员编号及测试编号！");
            }
        });
    }

    /**
     * Activity从后台重新回到前台时被调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        ed_checkUser.setText(checkUser);
        ed_checkCode.setText(checkCode);
        ed_checkBatchNumber.setText(checkBatchNumber);
    }


}

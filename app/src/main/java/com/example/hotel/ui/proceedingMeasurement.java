package com.example.hotel.ui;

import static android.content.ContentValues.TAG;
import static com.example.hotel.ui.BluetoothConnectActivity.a;
import static com.example.hotel.ui.BluetoothConnectActivity.nsz;
import static com.example.hotel.ui.BluetoothConnectActivity.receive;
import static com.example.hotel.ui.CheckActivity.pointDetailin;
import static com.example.hotel.ui.CheckActivity.point_num;
import static com.example.hotel.ui.LoginActivity.JSON;
import static com.example.hotel.ui.LoginActivity.customerLogo;
import static com.example.hotel.ui.StartMeasureActivity.bolean;
import static com.example.hotel.ui.StartMeasureActivity.medium;
import static com.example.hotel.ui.StartMeasureActivity.pointId;
import static com.example.hotel.ui.WriteMessageActivity.checkCode;
import static com.example.hotel.ui.ZoomImageView.bolean1;
import static com.example.hotel.ui.ZoomImageView.num;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
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

import com.example.hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class proceedingMeasurement extends AppCompatActivity {
    private Button btn_next_page;
    private Button btn_previous_page;
    private Button btnOrderedMeasure;
    private Button btnDisorderedMeasure;
    private Button btnConnectionSetting;
    public static List<String> pointId_total = new ArrayList<String>();
    public static String[] pointtype;
    public static String[] pointvalue;
    private Button btn_save_outcome,btn_previous_point,btn_next_point;
    private ImageView ivReturn;
    public static ZoomImageView ziv;
    private String result, checkStr;
    private static Double pointid1;
    private TextView textView1;
    public static Integer logZ;
    public static Integer sum_d=0;


    public static Button btnmeasurement;
    private Button btnWriteMessage;
    public ImageView imageView;
    public ImageView proImageView;
    private Double code, total;
    private Double upperTolerance;
    private Double lowerTolerance;
    public static TextView valTV;
    public static TextView val8,val9,val10,val11;
    public static Button btn_ok1,btn_ng1;
    public static float imgWid,imgHit;
    public static String mark="";
    public  static SoundPool sp;//声明一个SoundPool
    public static int music;
    public static  Boolean flag;
    private static final String TAG = "Proceeding";
    //接口
//    private String result;
    private static final int POST = 2;
    private String[][] arr_d1,arr_id;

    public static String[] pointCode_d,pagenum_d;
    public static String[] pagepointid;
    private String pointCode;
    public static Integer cont;
    private String pointCode_1;
    private Double pointId_1,pagenum1;
    public static String medium="";
    private OkHttpClient client = new OkHttpClient();
    private Handler handlerchecksave = new Handler() {
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
                    Toast.makeText(getApplicationContext(),jsonMap.get("msg").toString(), Toast.LENGTH_SHORT).show();

//                    tv.setText(jsonMap.get("msg").toString());


                    break;
            }
        }
    };
private Handler handlerid = new Handler(){
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
                Double allPageNum = (Double) data.get("allPageNum"); //3
                List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("pointList");  //列表
                arr_id = new String[listtotal.size()][2];
//                pointCode_d = new String[listtotal.size()];
//                pointId_total = new String[(listtotal.size())];//id
//                pagenum_d = new String[listtotal.size()];
                for (Integer i = 0; i < listtotal.size(); i++) {
                    Map list = (Map) listtotal.get(i);
                    pagenum1 = (Double) list.get("pageNum");
                    pointId_1 = (Double) list.get("pointId");
                    arr_id[i][0] = pointId_1.toString();
                    arr_id[i][1] = pagenum1.toString();
                }
                for(int i=1; i<=allPageNum;i++){
                    for (int j=0;j<listtotal.size();j++){
                        if(Float.parseFloat(arr_id[j][1])==i){
                            pointId_total.add(arr_id[j][0]);
                        }
                    }
                }


//                textView1.setText(pointCode_d[13]);
                break;
        }
    }
};
private Handler handlerpoint_p= new Handler() {
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
//                    total = 3.0;
                List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("pointList");  //列表
//                    num = (Double) data.get("size");
                pagepointid = new String[listtotal.size()];
                arr_d1 = new String[listtotal.size()][2];
                pointCode_d =new String[listtotal.size()];
                for (Integer i = 0; i < listtotal.size(); i++) {
                    Map list = (Map) listtotal.get(i);
                    pointid1 = (Double) list.get("pointId");  //pointId
                    pointCode = (String) list.get("pointCode");
                    upperTolerance = (Double) list.get("upperTolerance");
                    lowerTolerance = (Double) list.get("lowerTolerance");
//                        correctValue   = (Double) list.get("correctValue");
                    arr_d1[i][0]=upperTolerance.toString();
                    arr_d1[i][1]=lowerTolerance.toString();
//                        arr_d1[i][2]=correctValue.toString();
                    pointCode_d[i]=pointCode;
//                    pointId_total[i]=pointid1.toString();
                    pagepointid[i]=pointid1.toString();
                }

                break;
        }
    }
};


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceeding_measurement);
        postPoint();

//        postPointtotal(CheckActivity.insNumber);
        flag=true;
//        ZoomImageView.flag=false;
        cont=0;


        imageView=findViewById(R.id.imageView);
        downloadImage(customerLogo,imageView);
        proImageView = findViewById(R.id.my_image_view3);
        downloadImage(CheckActivity.insToolimage,proImageView);

        textView1 = findViewById(R.id.textView);
        textView1.setText(LoginActivity.customerName);
//        postidvalue();
        btnConnectionSetting = findViewById(R.id.btn_connection_setting);
        btnmeasurement = findViewById(R.id.btn_measure);
        btn_save_outcome = findViewById(R.id.btn_save_outcome);
        btnWriteMessage = findViewById(R.id.btn_write_inf);
//        btn_next_page = findViewById(R.id.btn_next_page);
        ivReturn = findViewById(R.id.iv_back);
//        textView1= findViewById(R.id.textView1);
//        btn_previous_page = findViewById(R.id.btn_previous_page);
        val8=findViewById(R.id.val8);
//        val11=findViewById(R.id.val11);
        btn_ok1=findViewById(R.id.btn_ok1);
        btn_ng1=findViewById(R.id.btn_ng1);
        btn_save_outcome= findViewById(R.id.btn_save_outcome);
        btn_previous_point=findViewById(R.id.btn_previous_point);
        btn_next_point=findViewById(R.id.btn_next_point);
        valTV=(TextView) findViewById(R.id.tv);
        ziv =findViewById(R.id.my_image_view3);
        logZ=0;
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.dingdong, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        //设置控件事件
        setViewEventListener();

        Timer timer=new Timer();//等待接口
        timer.schedule(new TimerTask(){
            public void run(){
//                setPointId(1);
//                setPointId_total(1);
                postid();
                System.out.println("退出");
                this.cancel();}},500);//五百毫秒


//        setPointId_total(num);
//        textView1.setText(LoginActivity.customerName);
//        pointDetail = new String[ErrorRangeActivity.total.intValue()][2];
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (MeasureActivity.logB==1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnConnectionSetting.setBackground(getDrawable(R.drawable.btn_ok));
            }
        }
        if (logZ==1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnmeasurement.setBackground(getDrawable(R.drawable.btn_ok));
            }
        }
    }

    private Integer sum_index(Integer num){
        int sum = 0;
        for (int i=1; i<num;i++){
            sum+=Double.parseDouble(CheckActivity.pagepoint[i-1]);
        }
        return sum;
    }

    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            cont=0;
            Intent intent=new Intent(this,MeasureActivity.class);
            startActivity(intent);
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.num = 1;
            ZoomImageView.size_data = true ;
            BluetoothConnectActivity.bt="0";
        });

        btn_save_outcome.setOnClickListener(v -> {
            //事件函数内容
            postChecksave();
            WriteMessageActivity.checkBatchNumber =" ";
            WriteMessageActivity.checkUser = " ";
            WriteMessageActivity.checkCode = " ";
        });
        btnConnectionSetting.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(proceedingMeasurement.this, BluetoothConnectActivity.class);
            startActivity(intent);
        });
        btnWriteMessage.setOnClickListener(v -> {
            //事件函数内容
            Intent intent = new Intent();
            intent.setClass(proceedingMeasurement.this, WriteMessageActivity.class);
            startActivity(intent);
        });
        btnmeasurement.setOnClickListener(v -> {
            BluetoothConnectActivity.bt="1";
            flag=true;
            ZoomImageView.flag=false;
            logZ=1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnmeasurement.setBackground(getDrawable(R.drawable.btn_ok));
            }
        });
        btn_ok1.setOnClickListener(v -> {

            pointDetailin[cont+sum_d][0] =" 1";
            pointDetailin[cont+sum_d][1] ="null";
            val8.setText("OK");
            Log.d(TAG, "setViewEventListener: OOOKKK");
            medium = "";
            proceedingMeasurement.val8.setText("OK");
            valTV.setText(pointCode_d[cont]);
            bolean1="0";
            medium = "K1";
            receive="1";
            proceedingData();
            cont++;
        });
        btn_ng1.setOnClickListener(v -> {
            pointDetailin[cont+sum_d][0] =" 2";
            pointDetailin[cont+sum_d][1] ="null";
            val8.setText("NG");
            medium = "";
            proceedingMeasurement.val8.setText("NG");
            valTV.setText(pointCode_d[cont]);
            bolean1="0";
            medium = "K1";
            receive="1";
            proceedingData();
            cont++;
        });
        btn_previous_point.setOnClickListener(v -> {
            Log.d(TAG, "run: num-1BB"+num);
//            postChecksave(num);
            num -=1;
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.size_data = true ;
            sum_d = sum_index(num);
            if(num<=0){
                num = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();
            }else{
            }
            Intent intent = new Intent(this, proceedingMeasurement.class);
            startActivity(intent);
        });
        btn_next_point.setOnClickListener(v -> {
//            b = proceedingMeasurement.cont+b;
//            Log.d(TAG, "setViewEventListener: "+b);
            Log.d(TAG, "run: num-1AA"+num);
//            postChecksave();
//            setPointId(num);
//            setPointId_total(num);
            if(num < point_num){
                Log.d(TAG, "您读到"+point_num);
                ZoomImageView.mDrawableMap.clear();
                ZoomImageView.size_data = true ;
                num += 1;
                Log.d(TAG, "到"+ num);
                Intent intent = new Intent(this, proceedingMeasurement.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public  static void proceedingData(){
        Log.d(TAG, "proceedingData: "+receive+bolean);
        if(receive=="1"){
            if(bolean=="0") {
                medium = "K1";
//              ZoomImageView.data = "Z" + 2;
                Log.d(TAG, "标签是Z" + a);
                ziv.invalidate();
                sp.play(music, 1, 1, 0, 0, 1);
                mark = "1";
                bolean = "0";
                bolean1 = "0";
                receive = "0";
//                valTV.setText("");

//                StartMeasureActivity.index = a;
                StartMeasureActivity.index = cont;

                Log.d(TAG, "您读到的标签是Z" + StartMeasureActivity.index);
            }
        }

    }



    private void postChecksave() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                checkStr = "{\n" +
                        "    \"insToolId\":"+CheckActivity.insToolId+",\n" +
                        "    \"checkUser\":\""+WriteMessageActivity.checkUser+"\",\n" +
                        "    \"checkCode\":\""+ checkCode+"\",\n" +
                        "    \"checkBatchNumber\":\""+WriteMessageActivity.checkBatchNumber+"\",\n" +
                        "    \"pointDetail\":[";
                for (int i=0; i<CheckActivity.insNumber; i++){
//                        Log.d(TAG, "run: checkvalue!!!!!"+num);
//                        Log.d(TAG, "run: CheckActivity.pagepoint[num-1]"+pointId_total[i]+"\n"+pointDetailin[i][0]+"\n"+pointDetailin[i][1]);
                        checkStr += "   {\n" +
                                "    \"pointId\":"+pointId_total.toArray()[i]+",\n" +  //pointlist
                                "    \"checkResult\":"+Float.parseFloat(pointDetailin[i][0])+",\n" +
                                "    \"checkValue\":"+pointDetailin[i][1]+"\n" +
                                "    },";

                }
//
                String str = checkStr.substring(0, checkStr.length()-1);
                str += "] \n" +
                        "}";
                try {
                    result = post("http://81.69.170.198:8002/point/checkSave", str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("TAG", result);
                Message msg = Message.obtain();
                msg.what = POST;
                msg.obj = result;
                handlerchecksave.sendMessage(msg);

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

    private void postPoint() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/propLocation", "{\n" +
                            "    \"pageNum\":"+ZoomImageView.num+",\n" +
                            "    \"insToolId\": "+CheckActivity.insToolId+""+
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerpoint_p.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private void postid() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/propLocation", "{\n" +
                            "    \"insToolId\": "+CheckActivity.insToolId+""+
                            "}");
                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerid.sendMessage(msg);
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
                            imgWid = img.getWidth();
                            imgHit = img.getHeight();
//                            textView1.setText((int) imgHit+"##"+(int) imgWid);
                        }
                    }, 20);
                } else { //失败

                    Toast.makeText(getApplicationContext(), "下载失败,请检查原因", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


}
package com.example.hotel.ui;
import static com.example.hotel.ui.LoginActivity.JSON;
import static com.example.hotel.ui.LoginActivity.customerLogo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class CheckActivity extends AppCompatActivity {
    //重写oncreate方法
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private String[] imageList;
    public ImageView imageView;
    private LinearLayout Layout1,Layout2,Layout3,Layout4,Layout5,Layout6;
    private LinearLayout[] visiableLayout;
    private TextView tv1, tv2,tv3,tv4,tv5,tv6;
    private final static String TAG = MainActivity.class.getSimpleName();
    private Double code, id;
    public static Double pointNumber,pointPageNumber;
    private Integer size;
    private String[] idList,NumberList,insToolNamelist,pointNumberlist,pointPageNumberlist;
    private ImageView ivReturn;
    public static TextView textView,textView1;
    public static String insToolName, imageUrl;
    private String result,image;
    public static Double total;
    public static Integer pageNum, pageSize;
    private static final int POST = 2;
    public static Float point_num;
    public static String[] pagepoint;
    public static String[][] pointDetail,pointDetailin;
    private View arr_list[][];
    private OkHttpClient client = new OkHttpClient();
    public static float insToolId;
    public static Double insNumber;
    public static String insToolimage;
    private Button btnForward, btnBackward;
    public static String[][] pointCount_list;  //每页点的数量
    public static Double imagepointcount;  //存放该页的点数pointCount
    public static Double imagepagenum;
    private Handler handlerlogout = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    code = (Double) jsonMap.get("code");
                    if(code == 200.0){
                        CheckActivity.this.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                        System.exit(0);

                    }
                    break;
            }
        }
    };
    private Handler handlerinstool = new Handler() {
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
                    total = (Double) data.get("total");   //1
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("list");  //列表list
                    size = listtotal.size();
                    idList = new String[listtotal.size()];
                    NumberList = new String[listtotal.size()];
                    imageList= new String[listtotal.size()];
                    insToolNamelist = new String[listtotal.size()];
//                    pointNumberlist = new String[listtotal.size()];
                    pointPageNumberlist = new String[listtotal.size()];
                    for (Integer i = 0; i < listtotal.size(); i++) {

                        Map list = (Map) listtotal.get(i);
                        id = (Double) list.get("insToolId");  //insToolId
                        image= list.get("imageUrl").toString();  //imageUrl
                        idList[i]=id.toString();  //存放所有的insToolId
                        imageList[i]=image;  //存放所有的imageUrl放在列表里


                        insToolName = (String) list.get("insToolName");  //检具名称

//                        Log.d(TAG, "handleMessage: insToolName"+insToolName);

                        insToolNamelist[i] = insToolName;
                        imageUrl = (String) list.get("imageUrl");   //检具图片，这个也是imageUrl
                        pointNumber = (Double) list.get("pointNumber");  //"pointNumber": 16,点总个数
                        pointPageNumber = (Double) list.get("pointPageNumber");  //"pointPageNumber": 3；点总页数
                        Log.d(TAG, "handleMessage: pointPageNumber"+pointPageNumber);
                        pointPageNumberlist[i] =pointPageNumber.toString();
                        if(pointPageNumber!=0) {  //pointPageNumber=0时，"pointPageCount": null
                            List<Map<String, Object>> pointPageCount = (List<Map<String, Object>>) list.get("pointPageCount");  //列表pointPageCount,不是data，看他最前面最近的一个{}
                            pointCount_list = new String[20][pointPageCount.size()];  //pointPageCount.size()点的页数，等于pointPageNumber
                            for (int j = 0; j < pointPageCount.size(); j++) {  //也可以j<pointPageNumber
                                Map list1 = (Map) pointPageCount.get(j);  //list1: pointPageCount下的每一个{}，i遍历
//                                imagepagenum = (Double) list1.get("pageNum");
//                                pointCount_list[j] =imagepagenum.toString();
                                imagepointcount = (Double) list1.get("pointCount");  //存放该页的点数pointCount
                                pointCount_list[i][j] = imagepointcount.toString();  //[13,2,1]
                            }
                        }
                        NumberList[i]=pointNumber.toString();
                        TextView a =(TextView) arr_list[i][1];
                        ImageView b =(ImageView) arr_list[i][0];
                        a.setText(insToolName);
                        downloadImage(imageUrl, b);

                    }

                    for(int i = 0;i<6;i++) {
                        if (i < size) {
                            visiableLayout[i].setVisibility(View.VISIBLE);
                            }
                        else{
                            visiableLayout[i].setVisibility(View.INVISIBLE);
                        }

                    }

                    break;
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_check);


        textView=findViewById(R.id.textView);
        textView.setText(LoginActivity.customerName);

        imageView= findViewById(R.id.imageView);
        downloadImage(LoginActivity.customerLogo,imageView);


        pageNum = 1;  //页码
        pageSize = 6;  //分页大小
        btnForward = findViewById(R.id.forward);
        btnBackward = findViewById(R.id.backward);

        //获取控件
        Layout1 = findViewById(R.id.li_1);
        Layout2 = findViewById(R.id.li_2);
        Layout3 = findViewById(R.id.li_3);
        Layout4 = findViewById(R.id.li_4);
        Layout5 = findViewById(R.id.li_5);
        Layout6 = findViewById(R.id.li_6);

        visiableLayout = new LinearLayout[]{Layout1, Layout2, Layout3, Layout4, Layout5, Layout6};
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView2);
        img3 = findViewById(R.id.imageView3);
        img4 = findViewById(R.id.imageView4);
        img5 = findViewById(R.id.imageView5);
        img6 = findViewById(R.id.imageView6);
        tv1=findViewById(R.id.text_1);
        tv2=findViewById(R.id.text_2);
        tv3=findViewById(R.id.text_3);
        tv4=findViewById(R.id.text_4);
        tv5=findViewById(R.id.text_5);
        tv6=findViewById(R.id.text_6);
        arr_list= new View[][]{{img1, tv1},{img2, tv2},{img3, tv3},
                               {img4, tv4},{img5, tv5},{img6, tv6}};
        ivReturn = findViewById(R.id.iv_back);
        textView1=findViewById(R.id.textView1);
        //设置控件事件
        setViewEventListener();
//        textView1.setText(LoginActivity.customerName);
//        img3.setImageBitmap(LoginActivity.bitmap);
        postInstool(pageNum, pageSize);


    }

    //设置控件事件y

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销广播接收
        postLogout();
    }
    private void pointdata(Double numm){
        pointDetail = new String[numm.intValue()][2];
        for(int i=0; i<numm.intValue();i++){
            pointDetail[i][0] = "0";
            pointDetail[i][1] = "null";
        }
        pointDetailin = new String[numm.intValue()][2];
        for(int i=0; i<numm.intValue();i++){
            pointDetailin[i][0] = "0";
            pointDetailin[i][1] = "null";
        }
    }
    private void setViewEventListener() {
        ivReturn.setOnClickListener(v -> {
            //事件函数内容
            // 创建提醒对话框的建造器
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 设置对话框的标题文本
            builder.setTitle("是否退出App");
            // 设置对话框的内容文本

            // 设置对话框的否定按钮文本及其点击监听器
            builder.setNegativeButton("是", (dialog, which) -> {
                postLogout();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                if(BluetoothConnectActivity.signal1=="1"){
                    new BluetoothConnectActivity().intance.finish();
                    BluetoothConnectActivity.signal1="0";
                }
                else {
                    finish();
                }
            });
            // 设置对话框的肯定按钮文本及其点击监听器
            builder.setPositiveButton("否", (dialog, which) -> {
            });

            // 根据建造器构建提醒对话框对象
            AlertDialog dialog = builder.create();
            // 显示提醒对话框
            dialog.show();

        });

        img1.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[0]);
            pointdata(insNumber);
            if (insNumber>0) {
                insToolId = Float.parseFloat(idList[0]);

                insToolimage = imageList[0];
                point_num = Float.parseFloat(pointPageNumberlist[0]);
                pagepoint = pointCount_list[0];
//                Log.d(TAG, "setViewEventListener: img1"+pagepoint[0]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });
        img2.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[1]);
            pointdata(insNumber);
            if (insNumber>0) {
                insToolId = Float.parseFloat(idList[1]);
                insToolimage = imageList[1];
                pagepoint = pointCount_list[1];
                point_num = Float.parseFloat(pointPageNumberlist[1]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });

        img3.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[2]);
            pointdata(insNumber);
            if(insNumber>0) {
                insToolId = Float.parseFloat(idList[2]);
                insToolimage = imageList[2];
                pagepoint = pointCount_list[2];
                point_num = Float.parseFloat(pointPageNumberlist[2]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });
        img4.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[3]);
            pointdata(insNumber);
            if(insNumber>0) {
                insToolId = Float.parseFloat(idList[3]);
                insToolimage = imageList[3];
                pagepoint = pointCount_list[3];
                point_num = Float.parseFloat(pointPageNumberlist[3]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });
        img5.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[4]);
            pointdata(insNumber);
            if (insNumber>0) {
                insToolId = Float.parseFloat(idList[4]);
                insToolimage = imageList[4];
                pagepoint = pointCount_list[4];
                point_num = Float.parseFloat(pointPageNumberlist[4]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });
        img6.setOnClickListener(v -> {
            //事件函数内容
            //意图 参数：context，要打开的页面的类.class
            insNumber = Double.parseDouble(NumberList[5]);
            pointdata(insNumber);
            if(insNumber>0) {
                insToolId = Float.parseFloat(idList[5]);
                insToolimage = imageList[5];
                pagepoint = pointCount_list[5];
                point_num = Float.parseFloat(pointPageNumberlist[5]);
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this, MeasureActivity.class);
                //打开activity
                startActivity(intent);
            }else {
                Toast.makeText(this, "该检具缺少配置信息", Toast.LENGTH_SHORT).show();
            }
        });

        btnForward.setOnClickListener(v -> {
            //上一页
            pageNum -= 1;
            if(pageNum<=0){
                pageNum = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();

            }
            postInstool(pageNum, pageSize);
        });
        btnBackward.setOnClickListener(v -> {
            //下一页
            if(total-pageNum*6>0){
                pageNum += 1;
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();

            }
            postInstool(pageNum, pageSize);
        });



    }
    private void postLogout() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/user/logout", "");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerlogout.sendMessage(msg);
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
    private void postInstool(Integer pageNum, Integer pageSize) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/insTool/list", "{\n" +
                            "    \"pageNum\":" + pageNum + ",\n" +
                            "    \"pageSize\":" + pageSize + "\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerinstool.sendMessage(msg);
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
                    Log.d(TAG, "onFailure: 失败");
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
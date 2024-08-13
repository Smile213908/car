package com.example.hotel.ui;
import static com.example.hotel.ui.CheckActivity.point_num;
import static com.example.hotel.ui.LoginActivity.customerLogo;
import static com.example.hotel.ui.ZoomImageView.bolean1;
import static com.example.hotel.ui.ZoomImageView.num;
import static com.example.hotel.util.port.JSON;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hotel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Preconditions;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Symbology;
import android.device.scanner.configuration.Triggering;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StartMeasureActivity extends AppCompatActivity {

    private static final String TAG = "TransferMain";
    private Button btnPreviousPage;
    private Button btnNextPage;
    private Button btn_previous_point;
    private Button btn_next_point;
    private Button btnSaveOutcome1;
    private Button btnTerminateMeasure;
    private Button btnOK;
    private Button btnNG;
    private Button transmit;
    private ImageView ivReturn;
    private NfcAdapter mNFcAdapter;  //设置实例
    private PendingIntent mPendingIntent = null;
    private IntentFilter[] mIntentFilter = null;
    private String[][] mTechList = null;
    public static EditText valTV, valTV2,valTV6,valTV8,valTV9,valTV12;
    public static TextView valTV3,valTV4,valTV5,valTV7,valTV10,valTV11;

    private Context mContext;
    public static Integer inx;
    private TextView textView1;
    private  Double pointId_1;
    private String data="";
    public static String medium="";
    public static String bolean="0";
    public static String mark="";
    public ImageView imageView,starimageView;
    public static int i=0;
    private OkHttpClient client = new OkHttpClient();
    private static final int POST = 2;
    private Double code, total;
    private String result, checkStr;
    public static Boolean flag;
    //    private String tag[] = {"ABC", "123"};
//    private int val[] = {10,20};
//    private TextView mTextView;
    private boolean isFirst = true;
//    private TextView tvSaveAlert;


    private EditText showScanResult;
    private TextView tag;
    private Double num;
    private boolean isScaning = false;
    private SoundPool soundpool = null;
    private int soundid;
    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private String barcodeStr;
    private ActionBar actionBar;
    public static Integer pointId;

    protected ZoomImageView ziv;
    private TextView tv,tv2,tv3,tv4,tv5,tv6;
    private TextView u1,u2,u3,u4,u5,u6,d1,d2,d3,d4,d5,d6;
    public static TextView tv_sum,val_sum;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;

    private SoundPool sp;//声明一个SoundPool
    private int music;
    public EditText[] arr_id;
    //全局LinearLayout
    public static LinearLayout llZ1;
    public static LinearLayout llZ2;
    public static LinearLayout llZ3;
    public static LinearLayout llZ4;
    public static LinearLayout llZ5;
    public static LinearLayout llZ6;
    public static LinearLayout llZ7;
    public static LinearLayout llZ8;
    private String[][] arr;


    private String[][] arr_d1,arr_id1;
    public static String[] pointCode_d;
    public static String[] pagelabel;
    private String label;
    private static Double pointid1;
    private String pointCode;
    private Double upperTolerance;
    private Double lowerTolerance;
    private Double correctValue;
    public static Integer pageNum, pageSize;
    public static List<String> pointId_total = new ArrayList<String>();

    public static int index ;
    private static  int b;
    private TextView[][] arr_str;
    private LinearLayout[] lls;
    public static Integer sum_f=0;


    private int n;

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
                    code = (Double) jsonMap.get("code");
                    Toast.makeText(getApplicationContext(),jsonMap.get("msg").toString(), Toast.LENGTH_SHORT).show();

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
//                    total = 3.0;
                    total = (Double) data.get("total");
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("pointList");  //列表
//                    num = (Double) data.get("size");

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
                    }
//                    download();
//                    load();
//                    label =pagelabel.toString()+pointCode_d.toString();

                    if ( (int)Double.parseDouble(CheckActivity.pagepoint[ZoomImageView.num-1])-(pageNum-1)*pageSize>6){
                         n = 6;
                    }else {
                        n = (int)Double.parseDouble(CheckActivity.pagepoint[ZoomImageView.num-1])-(pageNum-1)*pageSize;
                    }
                    for (int i=0;i<n;i++){
                        txtView_Val(arr_str[i][0],pointCode_d[(pageNum-1)*pageSize+i]);
                        txtView_Val(arr_str[i][1],arr_d1[(pageNum-1)*pageSize+i][0]);
//                        txtView_Val(arr_str[i][2],arr_d1[(pageNum-1)*pageSize+i][1]);
                    }
                    for(int i = 0;i<6;i++) {
                        if (i < n) {
                            lls[i].setVisibility(View.VISIBLE);
                        }
                        else{
                            lls[i].setVisibility(View.INVISIBLE);
                        }

                    }
//                    for(int i = 0;i<6;i++) {
//                        if (i < pagelabel.length) {
//                            Log.d(TAG, "handleMessage: "+pagelabel.length);
//                                txtView_Val(arr_str[i][0],pagelabel[i]);
////                                txtView_Val(arr_str[i][1],arr_d1[i][0]);
////                                txtView_Val(arr_str[i][2],arr_d1[i][1]);
//                            lls[i].setVisibility(View.VISIBLE);
//                        }
//                        else{
//                            lls[i].setVisibility(View.INVISIBLE);
//                        }
//                    }

                    for(i=0;i<n;i++) {
                        if (CheckActivity.pointDetail[(pageNum - 1) * pageSize +sum_f][1].equals("null")) {
                            if (CheckActivity.pointDetail[(pageNum - 1) * pageSize + i+sum_f][0].equals("2")) {
                                arr_id[i].setText("NG");
                            } else if (CheckActivity.pointDetail[(pageNum - 1) * pageSize + i+sum_f][0].equals("1")) {
                                arr_id[i].setText("OK");
                            }
                        } else {
                            arr_id[i].setText(CheckActivity.pointDetail[(pageNum - 1) * pageSize + i+sum_f][1]);
                        }
                    }

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
                    arr_id1 = new String[listtotal.size()][2];
//                pointCode_d = new String[listtotal.size()];
//                pointId_total = new String[(listtotal.size())];//id
//                pagenum_d = new String[listtotal.size()];
                    for (Integer i = 0; i < listtotal.size(); i++) {
                        Map list = (Map) listtotal.get(i);
                        Double pagenum1 = (Double) list.get("pageNum");
                        Double pointId_1 = (Double) list.get("pointId");
                        arr_id1[i][0] = pointId_1.toString();
                        arr_id1[i][1] = pagenum1.toString();
                    }
                    for(int i=1; i<=allPageNum;i++){
                        for (int j=0;j<listtotal.size();j++){
                            if(Float.parseFloat(arr_id1[j][1])==i){
                                pointId_total.add(arr_id1[j][0]);
                            }
                        }
                    }

//                textView1.setText(pointCode_d[13]);
                    break;
            }
        }
    };



    //用PDA扫描到的标签
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            showScanResult.setText("");
            mVibrator.vibrate(100);
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barcode, 0, barcodelen);
//            showScanResult.append(""  +barcodeStr);
            pdascan(barcodeStr,pointCode_d);
        }
    };
    //不同标签对应的触发事件
    private void pdascan(String barcodeStr,String[] pointCode_d) {
        for(i=0;i<pointCode_d.length;i++) {

            if (barcodeStr.equals(pointCode_d[i])) {

                b = i + 1;
                Log.d(TAG, "@@@@@" + b);
                break;
            } else {
                b = 0;
                continue;
            }
        }
        switch (b)
        {
            case 0:
                Log.d(TAG,"该店不在本页！");
                Toast.makeText(mContext, "该点不在本页！", Toast.LENGTH_LONG).show();
                break;
            case 1:
                if (bolean=="0"){
                    medium="Z1";

                    valTV3.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[0+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId = 0;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            case 2:
                if(bolean=="0"){
                    medium="Z2";

                    valTV4.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[1+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId = 1;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            case 3:
                if(bolean=="0"){
                    medium="Z3";

                    valTV5.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[2+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId = 2;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            case 4:
                if(bolean=="0"){
                    medium="Z4";

                    valTV7.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[3+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId =3;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            case 5:
                if(bolean=="0"){
                    medium="Z5";

                    valTV10.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[4+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId = 4;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            case 6:
                if(bolean=="0"){
                    medium="Z6";

                    valTV11.setTextColor(Color.parseColor("#0000FF"));
                    ziv.invalidate();
                    tv_sum.setText(pointCode_d[5+(pageNum-1)*pageSize]);
                    val_sum.setText("");
                    mark="1";
                    bolean="1";
                    pointId = 5;
                    index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + barcodeStr);
        }
    }
    private void setbg_style(LinearLayout ll){
        ll.setBackgroundResource(R.drawable.bg_border_red);
    }
    private void setbg_style_back(LinearLayout ll){
        ll.setBackgroundResource(R.drawable.bg_border);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_measure);
        flag = true;
        bolean="0";
       postid();

        imageView=findViewById(R.id.imageView);
        downloadImage(LoginActivity.customerLogo,imageView);
        starimageView=findViewById(R.id.my_image_view);
        downloadImage(CheckActivity.insToolimage,starimageView);


//        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        View rootView = LayoutInflater.from(StartMeasureActivity.this).inflate(
                R.layout.activity_fram, null);
//        frame.addView(rootView);
        btnPreviousPage = findViewById(R.id.btn_previous_page);
        btnNextPage = findViewById(R.id.btn_next_page);
        btnSaveOutcome1 = findViewById(R.id.btn_save_outcome1);
        btnTerminateMeasure = findViewById(R.id.btn_terminate_measure);
        btnOK = findViewById(R.id.btn_ok);
        btnNG = findViewById(R.id.btn_ng);
        ivReturn = findViewById(R.id.iv_back);
        transmit=findViewById(R.id.btn_clear);
        mContext = StartMeasureActivity.this;
        ziv = findViewById(R.id.my_image_view);
        tv = findViewById(R.id.tv);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        tv6=findViewById(R.id.tv6);
        tv_sum=(TextView)findViewById(R.id.tv_sum);
        val_sum=(TextView)findViewById(R.id.val_sum);
        // 初始化UI

        valTV = (EditText) findViewById(R.id.val);
        valTV2 = (EditText) findViewById(R.id.val2);
        valTV6=(EditText) findViewById(R.id.val3);
        valTV8=(EditText) findViewById(R.id.val4);
        valTV9=(EditText) findViewById(R.id.val5);
        valTV12=(EditText) findViewById(R.id.val6);
        valTV3=(TextView) findViewById(R.id.tv);
        valTV4=(TextView) findViewById(R.id.tv2);
        valTV5=(TextView) findViewById(R.id.tv3);
        valTV7=(TextView) findViewById(R.id.tv4);
        valTV10=(TextView) findViewById(R.id.tv5);
        valTV11=(TextView) findViewById(R.id.tv6);
        textView1=(TextView) findViewById(R.id.textView);
        textView1.setText(LoginActivity.customerName);
        //全局共享layout
        llZ1 =findViewById(R.id.ll_Z1);
        llZ2 =findViewById(R.id.ll_Z2);
        llZ3 =findViewById(R.id.ll_Z3);
        llZ4 =findViewById(R.id.ll_Z4);
        llZ5 =findViewById(R.id.ll_Z5);
        llZ6 =findViewById(R.id.ll_Z6);
        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);
        u5 = findViewById(R.id.u5);
        u6 = findViewById(R.id.u6);
        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);
        d4 = findViewById(R.id.d4);
        d5 = findViewById(R.id.d5);
        d6 = findViewById(R.id.d6);

        btn_previous_point=findViewById(R.id.btn_previous_point);
        btn_next_point=findViewById(R.id.btn_next_point);

        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.dingdong, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

        NfcCheck();
        //初始化NFC
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            intentFilter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        IntentFilter intentFilter1 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter intentFilter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        mIntentFilter = new IntentFilter[] {intentFilter, intentFilter1, intentFilter2};  // intentFilter1可以加在后面
        mTechList = null;  //只适用于NDEF过滤器，这里是Adapter可以不设置
        //设置控件事件
        setViewEventListener();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        actionBar = getSupportActionBar();

        showScanResult = (EditText) findViewById(R.id.scan_result);

        arr_str = new TextView[][]{{valTV3, u1, d1}, {valTV4, u2, d2},
                {valTV5, u3, d3}, {valTV7, u4, d4},
                {valTV10, u5, d5}, {valTV11, u6, d6},

        };
        lls = new LinearLayout[]{llZ1,llZ2, llZ3,llZ4,llZ5,llZ6};
        arr_id = new EditText[]{valTV,valTV2,valTV6,valTV8,valTV9,valTV12};
        pageNum = 1;  //页码
        pageSize = 6;  //分页大小
        postPoint(pageNum,pageSize);


    }



    private Integer setIndex(String[] pointCode_d, TextView tv_sum,Integer pageSize,Integer pageNum){
        for (int i = 0; i < pointCode_d.length; i++)
        {
            if (pointCode_d[i] == tv_sum.getText())
            {
                index = i ;//当数组中的元素与要查找的元素的值一样时,则查到了
                break;//跳出循环
            }
        }
        Log.d(TAG, "您读到的标签是2Z"+index);
        return index;
    }
    private void txtView_Val(TextView tv, String str){
        tv.setText(str);
    };
    //设置控件事件

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
            Intent intent = new Intent();
            intent.setClass(StartMeasureActivity.this, ChaoticMeasureActivity.class);
            //打开activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent);
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.num = 1;
            sum_f = 0 ;
//            ZoomImageView.size_data = true ;
        });
        btn_previous_point.setOnClickListener(v -> {
//            b = b - cont;
            pageNum=1;
            pageSize=6;
            index=0;
            bolean1="0";
            ZoomImageView.mDrawableMap.clear();
            ZoomImageView.num -=1;

            ZoomImageView.size_data = true ;
            if(ZoomImageView.num<=0){
                ZoomImageView.num = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();
            }else{
            }
            sum_f = sum_index(ZoomImageView.num);
            Intent intent = new Intent(this, StartMeasureActivity.class);
            startActivity(intent);
        });
        btn_next_point.setOnClickListener(v -> {
            pageNum=1;
            pageSize=6;
            index=0;
            bolean1="0";
//            label = pointCode_d.toString();
//            pagelabel = label.substring(13,14);
            if(ZoomImageView.num < point_num){
                Log.d(TAG, "您读到"+point_num);
                ZoomImageView.mDrawableMap.clear();
                ZoomImageView.size_data = true ;
                ZoomImageView.num += 1;
//                ziv.invalidate();
//                requestLayout();
                sum_f = sum_index(ZoomImageView.num);
//                ziv.postInvalidate();
                Intent intent = new Intent(this, StartMeasureActivity.class);
                startActivity(intent);
//                startActivity(new Intent(this, StartMeasureActivity.class));
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = new Intent(this, StartMeasureActivity.class);
//            startActivity(intent);
        });
        btnPreviousPage.setOnClickListener(v -> {
            //事件函数内容
            //上一页
            valTV.setText("");
            valTV2.setText("");
            valTV6.setText("");
            valTV8.setText("");
            valTV9.setText("");
            valTV12.setText("");
            pageNum -= 1;
            if(pageNum<=0){
                pageNum = 1;
                Toast.makeText(getApplicationContext(),"第一页", Toast.LENGTH_SHORT).show();
            }
            postPoint(pageNum, pageSize);
            valTV3.setTextColor(Color.parseColor("#000000"));
            valTV4.setTextColor(Color.parseColor("#000000"));
            valTV5.setTextColor(Color.parseColor("#000000"));
            valTV7.setTextColor(Color.parseColor("#000000"));
            valTV10.setTextColor(Color.parseColor("#000000"));
            valTV11.setTextColor(Color.parseColor("#000000"));
            ziv.invalidate();
            tv_sum.setText("");
            val_sum.setText("");
            medium="";
            Toast.makeText(mContext, "请读取标签", Toast.LENGTH_LONG);
            llZ1.setBackgroundResource(R.drawable.bg_border);
            llZ2.setBackgroundResource(R.drawable.bg_border);
            bolean="0";
        });
        btnNextPage.setOnClickListener(v -> {
//            inv();
            //下一页
            valTV.setText("");
            valTV2.setText("");
            valTV6.setText("");
            valTV8.setText("");
            valTV9.setText("");
            valTV12.setText("");
            if(Double.parseDouble(CheckActivity.pagepoint[ZoomImageView.num-1])-pageNum*6>0){
                pageNum += 1;
            }else{
                Toast.makeText(getApplicationContext(),"最后一页", Toast.LENGTH_SHORT).show();
            }
            postPoint(pageNum, pageSize);
            valTV3.setTextColor(Color.parseColor("#000000"));
            valTV4.setTextColor(Color.parseColor("#000000"));
            valTV5.setTextColor(Color.parseColor("#000000"));
            valTV7.setTextColor(Color.parseColor("#000000"));
            valTV10.setTextColor(Color.parseColor("#000000"));
            valTV11.setTextColor(Color.parseColor("#000000"));
            ziv.invalidate();
            tv_sum.setText("");
            val_sum.setText("");
            medium="";
            Toast.makeText(mContext, "请读取标签", Toast.LENGTH_LONG);
            llZ1.setBackgroundResource(R.drawable.bg_border);
            llZ2.setBackgroundResource(R.drawable.bg_border);
            bolean="0";
        });
        //手动点击文本框选择标签
        tv.setOnClickListener(v -> {
            Log.d(TAG, "setViewEventListener: bolean@@"+bolean);
            if(bolean=="0") {
                medium = "Z1";
//                medium = pointCode_d[0];

                Toast.makeText(mContext, "您读到的标签是Z1", Toast.LENGTH_LONG);
                valTV3.setTextColor(Color.parseColor("#FF0000"));

                tv_sum.setText(pointCode_d[0+(pageNum-1)*pageSize]);
//
                ziv.invalidate();

                val_sum.setText("");
                sp.play(music, 1, 1, 0, 0, 1);
//                mark = "1";
                bolean1 = "0";
                bolean = "1";
                pointId = 0;
                index = setIndex(pointCode_d, tv_sum, pageSize, pageNum);
            }
        });
        tv2.setOnClickListener(v -> {
            Log.d(TAG, "setViewEventListener: bolean@!!!@"+bolean1);
            if(bolean=="0"){
                medium="Z2";
//                medium = pointCode_d[1];
                valTV4.setTextColor(Color.parseColor("#0000FF"));
                tv_sum.setText(pointCode_d[1+(pageNum-1)*pageSize]);
                ziv.invalidate();
                mark="1";
                bolean="1";
                val_sum.setText("");
                pointId = 1;
                bolean1 = "0";
                sp.play(music, 1, 1, 0, 0, 1);
                index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
                Log.d(TAG, "setViewEventListener: bolean@@"+bolean1);

            }
        });
        tv3.setOnClickListener(v -> {
            if(bolean=="0"){
//                medium="Z"+ index;
                medium  = "Z3";


                valTV5.setTextColor(Color.parseColor("#0000FF"));
                tv_sum.setText(pointCode_d[2+(pageNum-1)*pageSize]);
                ziv.invalidate();
                val_sum.setText("");
                sp.play(music, 1, 1, 0, 0, 1);
                mark="1";
                bolean="1";
                pointId = 2;
                index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
            }
        });
        tv4.setOnClickListener(v -> {
            if(bolean=="0"){
                medium="Z4";

                valTV7.setTextColor(Color.parseColor("#0000FF"));
                tv_sum.setText(pointCode_d[3+(pageNum-1)*pageSize]);
                val_sum.setText("");
                sp.play(music, 1, 1, 0, 0, 1);
                ziv.invalidate();
                mark="1";
                bolean="1";
                pointId = 3;
                index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
            }
        });
        tv5.setOnClickListener(v -> {
            if(bolean=="0"){
                medium="Z5";


                valTV10.setTextColor(Color.parseColor("#0000FF"));
                tv_sum.setText(pointCode_d[4+(pageNum-1)*pageSize]);
                val_sum.setText("");
                sp.play(music, 1, 1, 0, 0, 1);
                ziv.invalidate();
                mark="1";
                bolean="1";
                pointId = 4;
                index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
            }
        });
        tv6.setOnClickListener(v -> {
            if(bolean=="0"){
                medium="Z6";
                valTV11.setTextColor(Color.parseColor("#0000FF"));
                tv_sum.setText(pointCode_d[5+(pageNum-1)*pageSize]);
                val_sum.setText("");
                sp.play(music, 1, 1, 0, 0, 1);
                ziv.invalidate();
                mark="1";
                bolean="1";
                pointId = 5;
                index = setIndex( pointCode_d,  tv_sum, pageSize, pageNum);
            }
        });


//        btnTerminateMeasure.setOnClickListener(v -> {
//            finish();
//        });
        //取消按钮事件
        transmit.setOnClickListener(v -> {
            valTV3.setTextColor(Color.parseColor("#000000"));
            valTV4.setTextColor(Color.parseColor("#000000"));
            valTV5.setTextColor(Color.parseColor("#000000"));
            valTV7.setTextColor(Color.parseColor("#000000"));
            valTV10.setTextColor(Color.parseColor("#000000"));
            valTV11.setTextColor(Color.parseColor("#000000"));
            ziv.invalidate();
            tv_sum.setText("");
            val_sum.setText("");
            Toast.makeText(mContext, "请读取标签", Toast.LENGTH_LONG);
            llZ1.setBackgroundResource(R.drawable.bg_border);
            llZ2.setBackgroundResource(R.drawable.bg_border);
            bolean="0";
//            mark = "0";
            bolean1 = "1";
        });
        //保存结果按钮事件
        btnSaveOutcome1.setOnClickListener(v -> {
//            if(!mark.equals("")){
//
//                WriteMessageActivity.checkBatchNumber.equals("");
//                WriteMessageActivity.checkCode.equals("");
//                WriteMessageActivity.checkUser.equals("");
//            }else {
//                Toast.makeText(StartMeasureActivity.this, "请点击标签测量！", Toast.LENGTH_LONG).show();
//            }
            postChecksave();
            /*
            // 创建提醒对话框的建造器
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 设置对话框的标题文本
            builder.setTitle("保存检测数据");
            // 设置对话框的内容文本
            builder.setMessage("保存结果,后续是否继续测量?");
            // 设置对话框的否定按钮文本及其点击监听器
            builder.setNegativeButton("退出测量", (dialog, which) -> {
                finish();
            });
            // 设置对话框的肯定按钮文本及其点击监听器
            builder.setPositiveButton("继续测量", (dialog, which) -> {
            });

            // 根据建造器构建提醒对话框对象
            AlertDialog dialog = builder.create();
            // 显示提醒对话框
            dialog.show();*/
        });
        btnOK.setOnClickListener(v -> {
             inx = (pageNum - 1) * pageSize + pointId;
            CheckActivity.pointDetail[inx+sum_f][0] = "1";
            CheckActivity.pointDetail[inx+sum_f][1] = "null";




            if(medium=="Z1") {
                valTV.setText("OK");
                valTV3.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean = "0";
            }
            if(medium=="Z2") {
                valTV2.setText("OK");
                valTV4.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean = "0";
            }
            if(medium=="Z3") {
                valTV6.setText("OK");
                valTV6.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean = "0";
            }
            if(medium=="Z4") {
                valTV8.setText("OK");
                valTV8.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean = "0";
            }
            if(medium=="Z5"){
                valTV9.setText("OK");
                valTV10.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean="0";

            }
            if(medium=="Z6"){
                valTV12.setText("OK");
                valTV11.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("OK");
                bolean="0";
//
            }
            else{
                Log.d(TAG, "请读取标签");
                Toast.makeText(mContext, "请读取标签", Toast.LENGTH_LONG);
            }
        });
        btnNG.setOnClickListener(v -> {
            inx = (pageNum-1)*pageSize+pointId;
            CheckActivity.pointDetail[inx+sum_f][0] = "2";
            CheckActivity.pointDetail[inx+sum_f][1] = "null";


            if(medium=="Z1"){
                valTV.setText("NG");
                valTV3.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";}
            if(medium=="Z2"){
                valTV2.setText("NG");
                valTV4.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";}
            if(medium=="Z3"){
                valTV6.setText("NG");
                valTV6.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";}
            if(medium=="Z4"){
                valTV8.setText("NG");
                valTV8.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";}
            if(medium=="Z5"){
                valTV9.setText("NG");
                valTV10.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";

            }
            if(medium=="Z6"){
                valTV12.setText("NG");
                valTV11.setTextColor(Color.parseColor("#000000"));
                val_sum.setText("NG");
                bolean="0";

            }
            else{
                Log.d(TAG, "请读取标签");
                Toast.makeText(mContext, "请读取标签", Toast.LENGTH_LONG);
            }
        });

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
    private void NfcCheck() {   //NFC功能监测
        mNFcAdapter = NfcAdapter.getDefaultAdapter(this);   //初始化
        if (mNFcAdapter == null) {
            Toast.makeText(mContext, "sorry,你的设备不支持NFC功能", Toast.LENGTH_LONG);
            finish();
        }
        else {  //带有NFC功能
            if (!mNFcAdapter.isEnabled()) {  //功能是否被打开
                Intent setNFC = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(setNFC);  //跳到NFC打开界面
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNFcAdapter != null) {
            mNFcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilter, mTechList);

        }
        initScan();
//        transfer();
        showScanResult.setText("");
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = mScanManager.getParameterString(idbuf);
        if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(SCAN_ACTION);
        }
        registerReceiver(mScanReceiver, filter);

    }
    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode( 0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNFcAdapter != null) {
            mNFcAdapter.disableForegroundDispatch(this);
        }
        if(mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver);
    }

    //读取内容  加在OnResume或OnNewIntent里面
    private void resolveIntent(Intent intent) { //intent获取String
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            NdefMessage[] messages = null;  //信息
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  //标签类型,标签TAG对象
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES); //标签中NDEF消息
            if(rawMsgs != null){ //是否为空
                messages = new NdefMessage[rawMsgs.length];
                for (int i=0; i<rawMsgs.length; i++){
                    messages[i] = (NdefMessage) rawMsgs[i];

                }
            }else {
                byte[] empty = new byte[]{}; // 未知标签数组
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,empty, empty,empty);  //记录，类型、数组、id、内容
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                messages = new NdefMessage[]{msg};
            }
//            titleTV.setText("Scan A TAG!");
            processNDEFMsg(messages);
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

        } else {
        }
    }


    private void processNDEFMsg(NdefMessage[] messages) {
        if (messages == null || messages.length == 0) {
            Toast.makeText(mContext, "Tag为空", Toast.LENGTH_LONG);
            return;
        }
        for (int i = 0; i < messages.length; i++) {
            int length = messages[i].getRecords().length;
            NdefRecord[] records = messages[i].getRecords();
            for (int j = 0; j < length; j++) {
                for (NdefRecord record:records) {
                    parseRTDUriRecode(record);
                }
            }
        }
    }
    //NFC读取标签
    @SuppressLint("RestrictedApi")
    private void parseRTDUriRecode(NdefRecord record){  //解析每一条recode
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)); //检查类型
        byte[] payload = record.getPayload();  //获取Payload内容
        Byte statusByte = record.getPayload()[0];  //第一个字节
        String textEncoding = ((statusByte & 0x80) == 0) ? "UTF-8" : "UTF-16"; //编码方式
        int langLength = statusByte & 0x3f;  //源码长度
        String kangCode = new String(payload, 1, langLength, Charset.forName("UTF-8"));  //源码
        try {
            String payLoadText = new String(payload, langLength + 1, payload.length - langLength - 1, textEncoding);  //真实文本:Z1
            switch (payLoadText){
                case "Z1":
                    if(bolean=="0"){
                        medium="Z1";


                        valTV3.setTextColor(Color.parseColor("#0000FF"));
                        tv_sum.setText(medium);
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                    break;
                case "Z2":
                    if(bolean=="0"){
                        medium="Z2";

                        valTV4.setTextColor(Color.parseColor("#0000FF"));
                        tv_sum.setText(medium);
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                    break;
                case "Z3":
                    if(bolean=="0"){
                        medium="Z3";
                        valTV5.setTextColor(Color.parseColor("#0000FF"));
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                break;
                case "Z4":
                    if(bolean=="0"){
                        medium="Z4";

                        valTV7.setTextColor(Color.parseColor("#0000FF"));
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                    break;
                case "Z5"://修改开头
                    if(bolean=="0"){
                        medium="Z5";

                        valTV10.setTextColor(Color.parseColor("#0000FF"));
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                    break;
                case "Z6":
                    if(bolean=="0"){
                        medium="Z6";

                        valTV11.setTextColor(Color.parseColor("#0000FF"));
                        ziv.invalidate();
                        mark="1";
                        bolean="1";
                    }
                    break;//修改结尾
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
                        "    \"checkCode\":\""+WriteMessageActivity.checkCode+"\",\n" +
                        "    \"checkBatchNumber\":\""+WriteMessageActivity.checkBatchNumber+"\",\n" +
                        "    \"pointDetail\":[";
                for (int i=0; i<CheckActivity.insNumber; i++){

                    Log.d(TAG, "run: checkValue"+CheckActivity.pointDetail[i][1]);
                        checkStr += "   {\n" +
                                "    \"pointId\":"+pointId_total.toArray()[i]+",\n" +
                                "    \"checkResult\":"+Float.parseFloat(CheckActivity.pointDetail[i][0])+",\n" +
                                "    \"checkValue\":"+CheckActivity.pointDetail[i][1]+"\n" +
                                "    },";
                    }

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
    private void postPoint(Integer pageNum, Integer pageSize) {
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
                    handlerpoint.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    private String post(String url, String json) throws IOException {
////        tv.setText(token);

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

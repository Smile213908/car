package com.example.hotel.ui;

import static com.example.hotel.ui.ZoomImageView.num;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Preconditions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.view.Window;
import android.view.WindowManager;

import com.example.hotel.R;
import com.example.hotel.ble.BLEManager;
import com.example.hotel.ble.OnBleConnectListener;
import com.example.hotel.ble.OnDeviceSearchListener;
import com.example.hotel.permission.PermissionListener;
import com.example.hotel.permission.PermissionRequest;
import com.example.hotel.util.TypeConversion;





/**
 * BLE开发
 */
public class BluetoothConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BLEMain";
    public static  BluetoothConnectActivity intance = null;

    //bt_patch(mtu).bin
    public static final String SERVICE_UUID = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";  //蓝牙通讯服务
    public static final String READ_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";  //读特征
    public static final String WRITE_UUID = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";  //写特征

    //动态申请权限
    private String[] requestPermissionArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权限
    private List<String> deniedPermissionList = new ArrayList<>();

    private static final int CONNECT_SUCCESS = 0x01;
    private static final int CONNECT_FAILURE = 0x02;
    private static final int DISCONNECT_SUCCESS = 0x03;
    private static final int RECEIVE_SUCCESS= 0x06;
    private static final int RECEIVE_FAILURE =0x07;
    private static final int START_DISCOVERY = 0x08;
    private static final int STOP_DISCOVERY = 0x09;
    private static final int DISCOVERY_DEVICE = 0x0A;
    private static final int DISCOVERY_OUT_TIME = 0x0B;
    private static final int SELECT_DEVICE = 0x0C;
    private static final int BT_OPENED = 0x0D;
    private static final int BT_CLOSED = 0x0E;

    private Button btSearch;
    private TextView tvCurConState;
    private TextView tvName;
    private TextView tvAddress;
    private Button btConnect;
    private Button btDisconnect;
    private ImageView iv_back;
    private EditText etSendMsg;
    private Button btSend;
    private TextView tvSendResult;
    private TextView tvReceive;
    private LinearLayout llDeviceList;
    private LinearLayout llDataSendReceive;
    private ListView lvDevices;
    private LVDevicesAdapter lvDevicesAdapter;

    private Context mContext;
    private Context mContext1;
    private BLEManager bleManager;
    private BLEBroadcastReceiver bleBroadcastReceiver;
    private BluetoothDevice curBluetoothDevice;  //当前连接的设备
    //当前设备连接状态
    private boolean curConnState = false;

    private NfcAdapter mNFcAdapter;  //设置实例
    private PendingIntent mPendingIntent = null;
    private IntentFilter[] mIntentFilter = null;
    private String[][] mTechList = null;
    private TextView titleTV, valTV, valTV2,valTV3,valTV4,valTV5,valTV6,valTV7;
    public static String medium1="";
    public static List<String> nsz = new ArrayList<>();
    public static String signal1="";
    public static String bt="";
    public static int i=0;
    public static int a=0;
    private Integer indx;
    public static String fromvalue="";
    public static String bolean="0";
    public static String bolean1="0";
    public static String receive="1";
    private boolean isFirst = true;

//    private EditText showScanResult;
    private TextView tag;
    private boolean isScaning = false;
    private SoundPool soundpool = null;
    private int soundid;
    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private String barcodeStr;
    private ActionBar actionBar;
    private ZoomImageView ziv;
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
//            showScanResult.setText("");
            mVibrator.vibrate(100);
            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barcode, 0, barcodelen);
//            showScanResult.append(""  +barcodeStr);
        }
    };

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case START_DISCOVERY:
                    Log.d(TAG, "开始搜索设备...");
                    break;

                case STOP_DISCOVERY:
                    Log.d(TAG, "停止搜索设备...");
                    break;

                case DISCOVERY_DEVICE:  //扫描到设备
                    BLEDevice bleDevice = (BLEDevice) msg.obj;
                    lvDevicesAdapter.addDevice(bleDevice);
                    break;

                case SELECT_DEVICE:
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) msg.obj;
//                    tvName.setText("JFDIII-F8217D");
//                    tvAddress.setText("CB:3F:F6:7D:21:F8");
                    tvName.setText(bluetoothDevice.getName());
                    tvAddress.setText(bluetoothDevice.getAddress());
                    curBluetoothDevice = bluetoothDevice;
                    if(!curConnState) {
                        if(bleManager != null){
                            bleManager.connectBleDevice(mContext,curBluetoothDevice,15000,SERVICE_UUID,READ_UUID,WRITE_UUID,onBleConnectListener);
                        }
                    }else{
//                        Toast.makeText(this, "当前设备已连接", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case CONNECT_FAILURE: //连接失败
                    Log.d(TAG, "连接失败");
                    tvCurConState.setText("连接失败");
                    curConnState = false;
                    MeasureActivity.logB = 0;
                    break;

                case CONNECT_SUCCESS:  //连接成功
                    Log.d(TAG, "连接成功");
                    tvCurConState.setText("连接成功");
                    curConnState = true;
                    MeasureActivity.logB = 1;
                    llDataSendReceive.setVisibility(View.VISIBLE);
                    llDeviceList.setVisibility(View.GONE);
                    break;

                case DISCONNECT_SUCCESS:
                    Log.d(TAG, "断开成功");
                    tvCurConState.setText("断开成功");
                    curConnState = false;
                    MeasureActivity.logB = 0;
                    break;
                //接收蓝牙数据
                case RECEIVE_SUCCESS:  //接收成功

//                    Log.d(TAG, "6546546"+proceedingMeasurement.sum_d);
                    if(StartMeasureActivity.medium=="Z1") {
                            byte[] recBufSuc = (byte[]) msg.obj;
                            String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);

                            StartMeasureActivity.bolean="0";
                            String  fromvalue = calculate(str);
                            StartMeasureActivity.valTV3.setTextColor(Color.parseColor("#000000"));
                            StartMeasureActivity.valTV.setText(fromvalue + " ");
                            StartMeasureActivity.val_sum.setText(fromvalue + " ");
                            Double data=Double.parseDouble(fromvalue);
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            Log.d(TAG, "@@@@$$%45"+indx);
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }


                            Double newdata=data-6;
                            if(newdata>=1.1||newdata<=-1.2){
                                StartMeasureActivity.llZ1.setBackgroundResource(R.drawable.bg_border_red);
                            }
                            else{
                            }
                        }
                    if(StartMeasureActivity.medium=="Z2") {
                            byte[] recBufSuc = (byte[]) msg.obj;
                            String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);
                            StartMeasureActivity.bolean="0";
                            String  fromvalue = calculate(str);
                            StartMeasureActivity.valTV4.setTextColor(Color.parseColor("#000000"));
                            StartMeasureActivity.valTV2.setText(fromvalue + " ");
                            StartMeasureActivity.val_sum.setText(fromvalue + " ");
                            Double data=Double.parseDouble(fromvalue);
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }

                            Double newdata=data-8;
                            if(newdata>=1.1||newdata<=-1.2){
                                StartMeasureActivity.llZ2.setBackgroundResource(R.drawable.bg_border_red);
                            }
                            else{
                            }
                        }
                    if(StartMeasureActivity.medium=="Z3") {
                        byte[] recBufSuc = (byte[]) msg.obj;
                        String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);
                        StartMeasureActivity.bolean="0";
                        String  fromvalue = calculate(str);
                        StartMeasureActivity.valTV5.setTextColor(Color.parseColor("#000000"));
                        StartMeasureActivity.valTV6.setText(fromvalue + " ");
                        StartMeasureActivity.val_sum.setText(fromvalue + " ");
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }

                    }
                    if(StartMeasureActivity.medium=="Z4") {
                        byte[] recBufSuc = (byte[]) msg.obj;
                        String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);
                        StartMeasureActivity.bolean="0";
                        String  fromvalue = calculate(str);
                        StartMeasureActivity.valTV7.setTextColor(Color.parseColor("#000000"));
                        StartMeasureActivity.valTV8.setText(fromvalue + " ");
                        StartMeasureActivity.val_sum.setText(fromvalue + " ");
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }

                    }
                    if(StartMeasureActivity.medium=="Z5") {
                        byte[] recBufSuc = (byte[]) msg.obj;
                        String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);

                        StartMeasureActivity.bolean="0";
                        String  fromvalue = calculate(str);
                        StartMeasureActivity.valTV10.setTextColor(Color.parseColor("#000000"));
                        StartMeasureActivity.valTV9.setText(fromvalue + " ");
                        StartMeasureActivity.val_sum.setText(fromvalue + " ");
                        Double data=Double.parseDouble(fromvalue);
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }

                    }
                    if(StartMeasureActivity.medium=="Z6") {
                        byte[] recBufSuc = (byte[]) msg.obj;
                        String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);

                        StartMeasureActivity.bolean="0";
                        String  fromvalue = calculate(str);
                        StartMeasureActivity.valTV11.setTextColor(Color.parseColor("#000000"));
                        StartMeasureActivity.valTV12.setText(fromvalue + " ");
                        StartMeasureActivity.val_sum.setText(fromvalue + " ");
                        Double data=Double.parseDouble(fromvalue);
                        if (StartMeasureActivity.flag) {
                            indx = (StartMeasureActivity.pageNum - 1) * StartMeasureActivity.pageSize + StartMeasureActivity.pointId;
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][0] = "1";
                            CheckActivity.pointDetail[indx+StartMeasureActivity.sum_f][1] = fromvalue;
                        }

                    }
                    if (bt=="1") {

                        byte[] recBufSuc = (byte[]) msg.obj;
                        String str = TypeConversion.bytes2HexString(recBufSuc, recBufSuc.length);
                        String fromvalue = calculate(str);
                        proceedingMeasurement.val8.setText(fromvalue);

                        a = nsz.size();
//                        Log.d(TAG, "handleMessage: AAA"+proceedingMeasurement.cont);
//                        Log.d(TAG, "handleMessage:BBB "+Float.parseFloat(CheckActivity.pagepoint[num-1]));
                        if (proceedingMeasurement.cont<CheckActivity.insNumber)
                        {
                            if(proceedingMeasurement.cont<Double.parseDouble(CheckActivity.pagepoint[num-1])) {
                                proceedingMeasurement.valTV.setText(proceedingMeasurement.pointCode_d[(int) (proceedingMeasurement.cont)]);
                                nsz.add(fromvalue);
                                Log.d(TAG, "第二次" + nsz);
                                bolean = "0";
                                bolean1 = "0";
                                receive = "1";
                                proceedingMeasurement.proceedingData();
                            }else{
                                Toast.makeText(getApplicationContext(), "该页测点已经全部测完", Toast.LENGTH_LONG).show();
//                                num=3
                            }
                            if(proceedingMeasurement.flag){
                                CheckActivity.pointDetailin[proceedingMeasurement.cont+proceedingMeasurement.sum_d][0]="1";
                                CheckActivity.pointDetailin[proceedingMeasurement.cont+proceedingMeasurement.sum_d][1]=fromvalue;

                            }
                            proceedingMeasurement.cont++;
                        }


                        }
                    break;

                case BT_CLOSED:
                    Log.d(TAG, "系统蓝牙已关闭");
                    break;

                case BT_OPENED:
                    Log.d(TAG, "系统蓝牙已打开");
                    break;
            }

        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);
        mContext = BluetoothConnectActivity.this;
        intance = this;
        indx=0;
        proceedingMeasurement.flag=false;
        StartMeasureActivity.flag=false;
        //动态申请权限（Android 6.0）

        //初始化视图
        initView();
        //初始化监听
        iniListener();
        //初始化数据
        initData();
        //注册广播
        initBLEBroadcastReceiver();
        //初始化权限
        initPermissions();
        if (MeasureActivity.logB==0){
            tvCurConState.setText("未连接");
        }else{
            tvCurConState.setText("已连接");
        }
        mContext1 = this;
        // 初始化UI
//        titleTV = (TextView) findViewById(R.id.title);
        valTV3=(TextView) findViewById(R.id.tv);
        valTV4=(TextView) findViewById(R.id.tv2);
        ziv = findViewById(R.id.my_image_view);
        signal1="1";
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

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        actionBar = getSupportActionBar();
    }

    private String calculate(String str) {
        // TODO Auto-generated method stub
        char a = str.charAt(6);
        char b = str.charAt(9);
        char c = str.charAt(10);
        char d = str.charAt(11);
        char e = str.charAt(12);
        int a1 = a - '0';
        int b1 = b - '0';
        int c1 = c - '0';
        int d1 = d - '0';
        int e1 = e - '0';
        String  fromvalue;
        if (a1 == 0) {
            Double value = (double) (b1 * 10 + c1 + d1 * 0.1 + e1 * 0.01);
              fromvalue = String.format("%.2f", value);
        }
        else {
            Double value = (double) (b1 * 10 + c1 + d1 * 0.1 + e1 * 0.01) * (-1);
            fromvalue = String.format("%.2f", value);
        }
        return fromvalue;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }



    private void NfcCheck() {   //NFC功能监测
        mNFcAdapter = NfcAdapter.getDefaultAdapter(this);   //初始化
        if (mNFcAdapter == null) {
            Toast.makeText(mContext1, "sorry,你的设备不支持NFC功能", Toast.LENGTH_LONG);
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
            Toast.makeText(mContext1, "Tag为空", Toast.LENGTH_LONG);
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
    @SuppressLint("RestrictedApi")
    private void parseRTDUriRecode(NdefRecord record){  //解析每一条recode
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)); //检查类型
        byte[] payload = record.getPayload();  //获取Payload内容
        Byte statusByte = record.getPayload()[0];  //第一个字节
        String textEncoding = ((statusByte & 0x80) == 0) ? "UTF-8" : "UTF-16"; //编码方式
        int langLength = statusByte & 0x3f;  //源码长度
        String kangCode = new String(payload, 1, langLength, Charset.forName("UTF-8"));  //源码
        try {
            String payLoadText = new String(payload, langLength + 1, payload.length - langLength - 1, textEncoding);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        btSearch = findViewById(R.id.bt_search);
        tvCurConState = findViewById(R.id.tv_cur_con_state);
        btConnect = findViewById(R.id.bt_connect);
        btDisconnect = findViewById(R.id.bt_disconnect);
        tvName = findViewById(R.id.tv_name);
        tvAddress = findViewById(R.id.tv_address);
        iv_back=findViewById(R.id.iv_back);
        llDeviceList = findViewById(R.id.ll_device_list);
        llDataSendReceive  = findViewById(R.id.ll_data_send_receive);
        lvDevices = findViewById(R.id.lv_devices);
    }


    /**
     * 初始化监听
     */
    private void iniListener() {
        btSearch.setOnClickListener(this);
        btConnect.setOnClickListener(this);
        btDisconnect.setOnClickListener(this);
       iv_back.setOnClickListener(this);
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BLEDevice bleDevice = (BLEDevice) lvDevicesAdapter.getItem(i);
                BluetoothDevice bluetoothDevice = bleDevice.getBluetoothDevice();
                if(bleManager != null){
                    bleManager.stopDiscoveryDevice();
                }
                Message message = new Message();
                message.what = SELECT_DEVICE;
                message.obj = bluetoothDevice;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SimpleDateFormat")
    private void initData() {
        //列表适配器
        lvDevicesAdapter = new LVDevicesAdapter(BluetoothConnectActivity.this);
        lvDevices.setAdapter(lvDevicesAdapter);

        //初始化ble管理器
        bleManager = new BLEManager();
        if(!bleManager.initBle(mContext)) {
            Log.d(TAG, "该设备不支持低功耗蓝牙");
            Toast.makeText(mContext, "该设备不支持低功耗蓝牙", Toast.LENGTH_SHORT).show();
        }else{
            if(!bleManager.isEnable()){
                //去打开蓝牙
                bleManager.openBluetooth(mContext,false);
            }
        }
    }

    /**
     * 注册广播
     */
    private void initBLEBroadcastReceiver() {
        //注册广播接收
        bleBroadcastReceiver = new BLEBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//手机蓝牙状态监听
        registerReceiver(bleBroadcastReceiver,intentFilter);
    }

    /**
     * 初始化权限
     */
    private void initPermissions() {
        //Android 6.0以上动态申请权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            final PermissionRequest permissionRequest = new PermissionRequest();
            permissionRequest.requestRuntimePermission( BluetoothConnectActivity.this, requestPermissionArray, new PermissionListener() {
                @Override
                public void onGranted() {
                    Log.d(TAG,"所有权限已被授予");
                }

                //用户勾选“不再提醒”拒绝权限后，关闭程序再打开程序只进入该方法！
                @Override
                public void onDenied(List<String> deniedPermissions) {
                    deniedPermissionList = deniedPermissions;
                    for (String deniedPermission : deniedPermissionList) {
                        Log.e(TAG,"被拒绝权限：" + deniedPermission);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销广播接收
       unregisterReceiver(bleBroadcastReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_search:  //搜索蓝牙
                llDataSendReceive.setVisibility(View.GONE);
                llDeviceList.setVisibility(View.VISIBLE);
                searchBtDevice();
                break;

            case R.id.bt_connect: //连接蓝牙
                if(!curConnState) {
                    if(bleManager != null){
                        bleManager.connectBleDevice(mContext,curBluetoothDevice,15000,SERVICE_UUID,READ_UUID,WRITE_UUID,onBleConnectListener);
                    }
                }else{
                    Toast.makeText(this, "当前设备已连接", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_disconnect: //断开连接
                if(curConnState) {
                    if(bleManager != null){
                        bleManager.disConnectDevice();
                    }
                }else{
                    Toast.makeText(this, "当前设备未连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back:
                finish();
//                Intent intent =new Intent();
//                intent.setClass(BluetoothConnectActivity.this, ChaoticMeasureActivity.class);
//                //打开activity
//                startActivity(intent);
        }
    }

    //////////////////////////////////  搜索设备  /////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void searchBtDevice() {
        if(bleManager == null){
            Log.d(TAG, "searchBtDevice()-->bleManager == null");
            return;
        }

        if (bleManager.isDiscovery()) { //当前正在搜索设备...
            bleManager.stopDiscoveryDevice();
        }

        if(lvDevicesAdapter != null){
            lvDevicesAdapter.clear();  //清空列表
        }

        //开始搜索
        bleManager.startDiscoveryDevice(onDeviceSearchListener,15000);
    }

    //扫描结果回调
    private OnDeviceSearchListener onDeviceSearchListener = new OnDeviceSearchListener() {

        @Override
        public void onDeviceFound(BLEDevice bleDevice) {
            Message message = new Message();
            message.what = DISCOVERY_DEVICE;
            message.obj = bleDevice;
            mHandler.sendMessage(message);
        }

        @Override
        public void onDiscoveryOutTime() {
            Message message = new Message();
            message.what = DISCOVERY_OUT_TIME;
            mHandler.sendMessage(message);
        }
    };

    //连接回调
    private OnBleConnectListener onBleConnectListener = new OnBleConnectListener() {
        @Override
        public void onConnecting(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void onConnectSuccess(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, int status) {
            //因为服务发现成功之后，才能通讯，所以在成功发现服务的地方表示连接成功
        }

        @Override
        public void onConnectFailure(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, String exception, int status) {
            Message message = new Message();
            message.what = CONNECT_FAILURE;
            mHandler.sendMessage(message);
        }

        @Override
        public void onDisConnecting(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void onDisConnectSuccess(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, int status) {
            Message message = new Message();
            message.what = DISCONNECT_SUCCESS;
            message.obj = status;
            mHandler.sendMessage(message);
        }

        @Override
        public void onServiceDiscoverySucceed(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, int status) {
            //因为服务发现成功之后，才能通讯，所以在成功发现服务的地方表示连接成功
            Message message = new Message();
            message.what = CONNECT_SUCCESS;
            mHandler.sendMessage(message);
        }

        @Override
        public void onServiceDiscoveryFailed(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, String failMsg) {
            Message message = new Message();
            message.what = CONNECT_FAILURE;
            mHandler.sendMessage(message);
        }

        @Override
        public void onReceiveMessage(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, BluetoothGattCharacteristic characteristic, byte[] msg) {
            Message message = new Message();
            message.what = RECEIVE_SUCCESS;
            message.obj = msg;
            mHandler.sendMessage(message);
        }

        @Override
        public void onReceiveError(String errorMsg) {
            Message message = new Message();
            message.what = RECEIVE_FAILURE;
            mHandler.sendMessage(message);
        }

        @Override
        public void onWriteSuccess(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, byte[] msg) {
            Message message = new Message();
//            message.what = SEND_SUCCESS;
            message.obj = msg;
            mHandler.sendMessage(message);
        }

        @Override
        public void onWriteFailure(BluetoothGatt bluetoothGatt, BluetoothDevice bluetoothDevice, byte[] msg, String errorMsg) {
            Message message = new Message();
//            message.what = SEND_FAILURE;
            message.obj = msg;
            mHandler.sendMessage(message);
        }

        @Override
        public void onReadRssi(BluetoothGatt bluetoothGatt, int Rssi, int status) {

        }

        @Override
        public void onMTUSetSuccess(String successMTU, int newMtu) {

        }

        @Override
        public void onMTUSetFailure(String failMTU) {

        }
    };


    /**
     * 蓝牙广播接收器
     */
    private class BLEBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_STARTED)) { //开启搜索
                Message message = new Message();
                message.what = START_DISCOVERY;
                mHandler.sendMessage(message);

            } else if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {//完成搜素
                Message message = new Message();
                message.what = STOP_DISCOVERY;
                mHandler.sendMessage(message);

            } else if(TextUtils.equals(action,BluetoothAdapter.ACTION_STATE_CHANGED)){   //系统蓝牙状态监听

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,0);
                if(state == BluetoothAdapter.STATE_OFF){
                    Message message = new Message();
                    message.what = BT_CLOSED;
                    mHandler.sendMessage(message);

                }else if(state == BluetoothAdapter.STATE_ON){
                    Message message = new Message();
                    message.what = BT_OPENED;
                    mHandler.sendMessage(message);

                }
            }
        }
    }
}

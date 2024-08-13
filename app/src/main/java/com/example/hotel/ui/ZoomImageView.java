package com.example.hotel.ui;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import static com.example.hotel.ui.LoginActivity.JSON;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.hotel.bean.PointWidgit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/***++++++++++++++++++++++**/

@SuppressLint("AppCompatCustomView")
public class ZoomImageView extends ImageView implements View.OnTouchListener {
    public class ZoomMode{
        public  final  static  int Ordinary=0;//普通
        public  final  static  int  ZoomIn=1;//双击放大
        public final static int TowFingerZoom = 2;//双指缩放
    }
    private Matrix matrix;
    //imageView的大小
    private PointF viewSize;
    //图片的大小
    public static PointF imageSize;
    public PointF getImageSize(){
        return imageSize;
    }
    public static String[][] pointCoordiniates;
    //接口
    private String result;
    private static final int POST = 2;
    private OkHttpClient client = new OkHttpClient();
    private Integer pageNum=1;
    public static Boolean flag;




    private Handler handlerviewclient = new Handler() {
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
//                    insToolName = (String) data.get("insToolName");  //检具名称
//                    imageUrl = (String) data.get("imageUrl");  //检具图片
                    List<Map<String, Object>> pointList = (List<Map<String, Object>>) data.get("pointList");  //列表
//                    Log.d(TAG, String.valueOf(pointList.size()));
                    pointCoordiniates = new String[pointList.size()][3];

                    for (Integer i = 0; i < pointList.size(); i++) {
                        Map list = (Map) pointList.get(i);
//                        pointId = (Double) list.get("pointId");
                        pointCoordiniates[i][0] = list.get("propLeft").toString();
                        pointCoordiniates[i][1] = list.get("propTop").toString();
                        pointCoordiniates[i][2] = (String) list.get("pointCode");
//                        upperTolerance = (Double) list.get("upperTolerance");
//                        lowerTolerance = (Double) list.get("lowerTolerance");
//                        Log.d(TAG, "handleMessage: 加点Location  "+pointCoordiniates[i][0]);
                        pointAdd(Float.parseFloat(pointCoordiniates[i][0]), Float.parseFloat(pointCoordiniates[i][1]),
                                mPaint, textPaint);


                    }
//                    Log.d(TAG, "handleMessage: imageSize.x+Zoom"+Float.parseFloat(pointCoordiniates[1][0])*imageSize.x);
//                    Log.d(TAG, "handleMessage: imageSize.x+Zoom"+Float.parseFloat(pointCoordiniates[2][0])*imageSize.x);

                    break;
            }
        }
    };


    //缩放后图片的大小
    private PointF scaleSize = new PointF();
    //最初的宽高的缩放比例
    private PointF originScale = new PointF();
    //imageview中bitmap的xy实时坐标
    private PointF bitmapOriginPoint = new PointF();
    //点击的点
    private PointF clickPoint = new PointF();
    //设置的双击检查时间限制
    private long doubleClickTimeSpan = 250;
    //上次点击的时间
    private long lastClickTime = 0;
    //双击放大的倍数
    private int doubleClickZoom = 3;
    //当前缩放的模式
    private int zoomInMode = ZoomMode.Ordinary;
    //临时坐标比例数据
    private PointF tempPoint = new PointF();
    //最大缩放比例
    private float maxScrole = 4;
    //两点之间的距离
    private float doublePointDistance = 0;
    //双指缩放时候的中心点
    private PointF doublePointCenter = new PointF();
    //两指缩放的比例
    private float doubleFingerScrole = 0;
    //上次触碰的手指数量
    private int lastFingerNum = 0;
    public static String bolean1="0";
    public static String test="";
    private  String data="";
    public  static float centerLeft,centerTop;

    //画笔
    private Paint mPaint;
    private TextPaint textPaint;
    //控件在画布中是否被拖拽
    private boolean isDragWidgetMode=false;
    //控件在画布选中模式
    private boolean isSelecWidgetMode=false;
    public static boolean size_data = true;
    public static int num=1;
    //    //新建圆的圆心的X坐标
//    public static float paintx = 510;
//    //新建圆的圆心的Y坐标
//    public static float painty = 310;
//    public static float[][] pointCoordiniates ={{150.0F,150.0F},{160.0F,100.0F},{160.0F,150.0F},
//        {170.0F,100.0F},{170.0F,150.0F},{200.0F,100.0F},{300.0F,300.0F}};

//    SharedPreferences points_data = CheckAjustActivity.points_data;


    //构造方法
    public ZoomImageView(Context context) {
        super(context);
        init();


    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    //画布初始化
    private void init(){
        setOnTouchListener(this);
        setScaleType(ScaleType.MATRIX);
        //创建矩阵 位移 旋转 缩放
        matrix = new Matrix();
        proceedingMeasurement.flag=false;
        StartMeasureActivity.flag=false;
        flag = true;

//        for (int i = 0; i < pointCoordiniates.length; i++) {
//            pointAdd(Float.parseFloat(pointCoordiniates[i][0]),Float.parseFloat(pointCoordiniates[i][1]),
//                    mPaint,textPaint);
//        }

    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        viewSize = new PointF(width,height);
        //  qh绘制背景
        Drawable drawable = getDrawable();
        if (drawable != null){
            imageSize = new PointF(drawable.getMinimumWidth(),drawable.getMinimumHeight());
//            imageSize = new PointF(192, 124);
            showCenter();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initControl();
        mPaint.setStrokeWidth(30);
        Set<Integer> integers = mDrawableMap.keySet();
        if(flag){
//            Log.d(TAG, "onDraw: 画点");
            for (Integer key :integers) {
                Paint paint1 =mPaint;
                mDrawableMap.get(key).draw(canvas,paint1,textPaint,pointCoordiniates[key][2]);
            }
        }
        if(StartMeasureActivity.flag){
//            Log.d(TAG, "onDraw: 无序");
        for (Integer key :integers) {
            Paint paint1 =mPaint;
            data=StartMeasureActivity.medium;
            if(key==judge(data)&&bolean1=="0"){
                Log.d(TAG, "onDraw: ####\t"+StartMeasureActivity.mark+"\t"+bolean1);
                paint1 = changeColor(judge(StartMeasureActivity.medium));
                data="";
                bolean1="1";
            }
            else if(judge(data)==-1&&bolean1=="1"){
                Log.d(TAG, "onDraw: !!!\t"+StartMeasureActivity.mark+"\t"+bolean1);
                paint1 = backColor(judge(StartMeasureActivity.medium));
            }


//            else if (judge(data)==-1&&bolean1=="1"){
//                Log.d(TAG, "onDraw: !!!\t"+StartMeasureActivity.mark+"\t"+bolean1);
//                paint1 = backColor(judge(StartMeasureActivity.medium));
//                bolean1="0";
//            }
//            else if (StartMeasureActivity.mark=="1"&&bolean1=="1"){
//                Log.d(TAG, "onDraw: @@@@\t"+StartMeasureActivity.mark+"\t"+bolean1);
//                paint1 = backColor(judge(StartMeasureActivity.medium));
//                StartMeasureActivity.mark="0";
//                bolean1="0";
//            }
            mDrawableMap.get(key).draw(canvas,paint1,textPaint,pointCoordiniates[key][2]);
//            k8--;
        }
        }
        if(proceedingMeasurement.flag){
//            Log.d(TAG, "onDraw: 有序");
            for (Integer key :integers) {
                Paint paint1 =mPaint;
                data=proceedingMeasurement.medium;
                if(key==judge(data)&&bolean1=="0"){
                    paint1 = changeColor(judge(proceedingMeasurement.medium));
                    data="";
                    bolean1="1";
//                    Log.d(TAG, "onDraw: ondraw"+00000000);
                }
                else if (judge(data)==-1&&bolean1=="1"){
                    paint1 = backColor(judge(proceedingMeasurement.medium));
                    bolean1="0";
                }
                else if (StartMeasureActivity.mark=="1"&&bolean1=="1"){
                    paint1 = backColor(judge(proceedingMeasurement.medium));
                    StartMeasureActivity.mark="0";
                    bolean1="0";
                }
                mDrawableMap.get(key).draw(canvas,paint1,textPaint,pointCoordiniates[key][2]);
//            k8--;
            }
        }
        if(size_data){
            size_data=false;
//             postView();
            postViewClient(num);

        }
//        postViewClient(num);


    }





    private void postViewClient(Integer num) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    centerLeft= (bitmapOriginPoint.x+(originScale.x*imageSize.x/2));
                    centerTop = (bitmapOriginPoint.x+(originScale.y*imageSize.y/2));
                    result = post("http://81.69.170.198:8002/point/propLocation", "{\n" +
                            "    \"insToolId\":"+CheckActivity.insToolId+",\n" +
                            "    \"pageNum\":"+num+"\n" +
                            "} ");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerviewclient.sendMessage(msg);
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


    //画笔初始化
    private void initControl() {
        mPaint = new Paint();
        //文字画笔格式
        textPaint = new TextPaint();
        textPaint.setARGB(0xFF, 0, 0, 0);
        textPaint.setTextSize(30.0F);
//        抗锯齿,就是让图形看上去没有齿轮状,看起来更柔和
        mPaint.setAntiAlias(true);
//        设置画笔的风格
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
//        设置画笔的线帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        设置画笔折断处的样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }


    /**等比居中显示
     **/
    protected void showCenter(){

        float scalex = viewSize.x/imageSize.x;
        float scaley = viewSize.y/imageSize.y;
        //最小缩放保障普通显示占满屏幕不溢出
        float scale = Math.min(scalex, scaley);
        scaleImage(new PointF(scale,scale));//缩放图片,获得scalesize当前缩放后实际尺寸
        scalePoint(new PointF(scale,scale));//缩放点
        //等比 加居中
        if (scalex<scaley){
            //居中
            translationImage(new PointF(0,viewSize.y/2 - scaleSize.y/2));
            translationPoints(new PointF(0,viewSize.y/2 - scaleSize.y/2));
            //保存原始显示左上角
            bitmapOriginPoint.x = 0;
            bitmapOriginPoint.y = viewSize.y/2 - scaleSize.y/2;
        }else {
            translationImage(new PointF(viewSize.x/2 - scaleSize.x/2,0));
            translationPoints(new PointF(viewSize.x/2 - scaleSize.x/2,0));
            //保存原始显示左上角点
            bitmapOriginPoint.x = viewSize.x/2 - scaleSize.x/2;
            bitmapOriginPoint.y = 0;
        }
        //保存原始显示宽高缩放比
        originScale.set(scale,scale);
        // 双指缩放比例
        doubleFingerScrole = scale;
    }



    /*****点击事件****
     *
     * @param v view
     * @param event 事件
     * @return
     *  祁
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //第一个触点按下之后就会触发到这个事件
            case MotionEvent.ACTION_DOWN:
                //获取触摸坐标 给point赋值
                clickPoint.set(event.getX(),event.getY());
                //判断屏幕上此时被按住的点的个数，当前屏幕只有一个点被点击的时候触发
                if (event.getPointerCount() == 1) {

                    //设置一个点击的间隔时长，来判断是不是双击
                    if (System.currentTimeMillis() - lastClickTime <= doubleClickTimeSpan) {

                        //判断缩放模式,如果图片此时缩放模式是普通模式，就触发双击放大
                        if (zoomInMode == ZoomMode.Ordinary) {
                            //临时点坐标数据,//分别记录被点击的点到图片左上角x,y轴的距离与图片x,y轴边长的比例，方便在进行缩放后，算出这个点对应的坐标点
                            tempPoint.set((clickPoint.x - bitmapOriginPoint.x) / scaleSize.x, (clickPoint.y - bitmapOriginPoint.y) / scaleSize.y);//比例
                            //进行缩放
                            scaleImage(new PointF(originScale.x * doubleClickZoom, originScale.y * doubleClickZoom));
                            scalePoint(new PointF(originScale.x * doubleClickZoom, originScale.y * doubleClickZoom));
                            //获取缩放后，图片左上角的xy坐标
                            getBitmapOffset();
                            Log.e("kzg","9**********************bitmapOriginPoint:"+bitmapOriginPoint);
                            //平移图片，使得被点击的点的位置不变。这里是计算缩放后被点击的xy坐标，与原始点击的位置的xy 双击中心放大
                            translationImage(new PointF(clickPoint.x - (bitmapOriginPoint.x + tempPoint.x * scaleSize.x), clickPoint.y - (bitmapOriginPoint.y + tempPoint.y * scaleSize.y)));
                            translationPoints(new PointF(clickPoint.x - (bitmapOriginPoint.x + tempPoint.x * scaleSize.x), clickPoint.y - (bitmapOriginPoint.y + tempPoint.y * scaleSize.y)));
                            //模式更改 双击放大模式
                            zoomInMode = ZoomMode.ZoomIn;
                            //双指缩放比例,并在双击放大后记录缩放比例
                            doubleFingerScrole = originScale.x*doubleClickZoom;
                        } else {
                            //双击还原
                            showCenter();
                            zoomInMode = ZoomMode.Ordinary;
                        }
                    } else {
                        lastClickTime = System.currentTimeMillis();//获得的是自1970-1-01 00:00:00.000 到当前时刻的时间距离,类型为long
                    }
                }
                break;

            //当屏幕上已经有触点处于按下的状态的时候，再有新的触点被按下时触发。
            case MotionEvent.ACTION_POINTER_DOWN:
                //计算最初的两个手指之间的距离
                doublePointDistance = getDoubleFingerDistance(event);
                break;
            //当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）触发。
            //当有一个手指离开屏幕后，就修改状态，这样如果双击屏幕就能恢复到初始大小
            case MotionEvent.ACTION_POINTER_UP:
                //当有一个手指离开屏幕后，就修改状态，这样如果双击屏幕就能恢复到初始大小
                zoomInMode = ZoomMode.ZoomIn;
                //记录此时的双指缩放比例
                doubleFingerScrole =scaleSize.x/imageSize.x;
                //记录此时屏幕触碰的点的数量
                lastFingerNum = 1;
                //判断缩放后的比例，如果小于最初的那个比例，就恢复到最初的大小
                if (scaleSize.x<viewSize.x && scaleSize.y<viewSize.y){
                    zoomInMode = ZoomMode.Ordinary;
                    showCenter();
                }
                break;


            /**************************************移动*******************************************/
            //当触点在屏幕上移动时触发，触点在屏幕上停留也是会触发的，
            case MotionEvent.ACTION_MOVE:

                if (zoomInMode != ZoomMode.Ordinary) {

                    float currentX = 0;
                    float currentY = 0;
                    //获取此时屏幕上被触碰的点有多少个
                    int pointCount = event.getPointerCount();
                    //如果是多指，计算中心点为假设的点击的点,计算出中间点所在的坐标
                    for (int i = 0; i < pointCount; i++) {
                        currentX += event.getX(i);
                        currentY += event.getY(i);
                    }
                    currentX /= pointCount;
                    currentY /= pointCount;//平均中心
                    //当屏幕被触碰的点的数量变化时，将最新算出来的中心点看作是被点击的点
                    if (lastFingerNum != event.getPointerCount()) {
                        clickPoint.x = currentX;
                        clickPoint.y = currentY;
                        lastFingerNum = event.getPointerCount();
                    }
                    //将移动手指时，实时计算出来的中心点坐标，减去被点击点的坐标就得到了需要移动的距离
                    float moveX = currentX - clickPoint.x;
                    float moveY = currentY - clickPoint.y;
                    //计算边界，使得不能已出边界，但是如果是双指缩放时移动，因为存在缩放效果，
                    //所以此时的边界判断无效
                    float[] moveFloat = moveBorderDistance(moveX, moveY);

                    if(isDragWidgetMode()){
                        int ik1 = getDownWidget(clickPoint.x,clickPoint.y);
                        if (ik1>-1){
                            translationPoint(ik1, new PointF(moveFloat[0], moveFloat[1]));
                            clickPoint.set(currentX, currentY);

                        }

                    }else {
                        //处理移动图片的事件
                        translationImage(new PointF(moveFloat[0], moveFloat[1]));
                        translationPoints(new PointF(moveFloat[0], moveFloat[1]));
                        clickPoint.set(currentX, currentY);
                    }


                }


                /**************************************缩放*******************************************/
                //判断当前是两个手指接触到屏幕才处理缩放事件
                if (event.getPointerCount() == 2){
                    //如果此时缩放后的大小，大于等于了设置的最大缩放的大小，就不处理
                    if ((scaleSize.x/imageSize.x >= originScale.x * maxScrole || scaleSize.y/imageSize.y >= originScale.y * maxScrole) && getDoubleFingerDistance(event) - doublePointDistance > 0){
                        break;
                    }
                    //这里设置当双指缩放的的距离变化量大于50，并且当前不是在双指缩放状态下，就计算中心点，等一些操作
                    if (Math.abs(getDoubleFingerDistance(event) - doublePointDistance) > 50 && zoomInMode != ZoomMode.TowFingerZoom){
                        //计算两个手指之间的中心点，当作放大的中心点
                        doublePointCenter.set((event.getX(0) + event.getX(1))/2,(event.getY(0) + event.getY(1))/2);
                        //将双指的中心点就假设为点击的点
                        clickPoint.set(doublePointCenter);
                        //下面就和双击放大基本一样
                        getBitmapOffset();
                        //分别记录被点击的点到图片左上角x,y轴的距离与图片x,y轴边长的比例，方便在进行缩放后，算出这个点对应的坐标点
                        tempPoint.set((clickPoint.x - bitmapOriginPoint.x)/scaleSize.x,(clickPoint.y - bitmapOriginPoint.y)/scaleSize.y);
                        //设置进入双指缩放状态
                        zoomInMode = ZoomMode.TowFingerZoom;
                    }
                    //如果已经进入双指缩放状态，就直接计算缩放的比例，并进行位移
                    if (zoomInMode == ZoomMode.TowFingerZoom){
                        //用当前的缩放比例与此时双指间距离的缩放比例相乘，就得到对应的图片应该缩放的比例
                        float scrole = doubleFingerScrole*getDoubleFingerDistance(event)/doublePointDistance;
                        //这里也是和双击放大时一样的

                        scaleImage(new PointF(scrole,scrole));
                        scalePoint(new PointF(scrole,scrole));
                        //获取view中bitmap的坐标点
                        getBitmapOffset();
                        //平移
                        translationImage(new PointF(clickPoint.x - (bitmapOriginPoint.x + tempPoint.x*scaleSize.x),clickPoint.y - (bitmapOriginPoint.y + tempPoint.y*scaleSize.y)));
                        translationPoints(new PointF(clickPoint.x - (bitmapOriginPoint.x + tempPoint.x*scaleSize.x),clickPoint.y - (bitmapOriginPoint.y + tempPoint.y*scaleSize.y)));
                    }
                }
                break;
            //当触点松开时被触发。
            case MotionEvent.ACTION_UP:
                if(isDragWidgetMode()){
                    int ik1 = getDownWidget(clickPoint.x,clickPoint.y);
                    if (ik1>-1){
                        changeXY(ik1);
                        int ik2 = ik1+1;
                        Toast.makeText(getContext(), "你编辑了Z"+ik2+"点", Toast.LENGTH_SHORT-1500).show();
                    }

                }


                Log.e("kzg","***********************ACTION_UP");
                lastFingerNum = 0;
                break;
        }
        return true;
    }
/*****************************************触点事件******************************************************************/


    /****图片缩放后比例大小*
     *
     * @param scaleXY 图片缩放宽高比
     *                祁
     */
    public void scaleImage(PointF scaleXY){
        matrix.setScale(scaleXY.x,scaleXY.y);
        scaleSize.set(scaleXY.x * imageSize.x,scaleXY.y * imageSize.y);
        setImageMatrix(matrix);//通过矩阵改变图片
    }

    /****点的缩放比例*
     *
     * @param scaleXY 比例参数
     *                祁
     */
    public void scalePoint(PointF scaleXY){
        Set<Integer> integers = mDrawableMap.keySet();
        for (Integer ketSet:integers) {
            float cx=mDrawableMap.get(ketSet).getCoordinateX(imageSize);
            float cy=mDrawableMap.get(ketSet).getCoordinateY(imageSize);
            mDrawableMap.get(ketSet).setScaleX(scaleXY.x * cx);//缩放后的坐标
            mDrawableMap.get(ketSet).setScaleY(scaleXY.y * cy);

            invalidate();}


    }

    /**
     * 对图片进行x和y轴方向的平移
     * @param pointF 宽高位移
     */
    public void translationImage(PointF pointF){
        matrix.postTranslate(pointF.x,pointF.y);
        setImageMatrix(matrix);
    }
    /**
     * 对所有点进行x和y轴方向的平移
     * @param pointF 点的xy移动
     *               祁
     */
    public void translationPoints(PointF pointF){

        Set<Integer> integers = mDrawableMap.keySet();
        for (Integer ketSet:integers) {
            float i = mDrawableMap.get(ketSet).getScaleX();
            float j =mDrawableMap.get(ketSet).getScaleY();
            mDrawableMap.get(ketSet).setScaleX(i+pointF.x);
            mDrawableMap.get(ketSet).setScaleY(j+pointF.y);
            invalidate();
        }
    }

    /**
     * 对于单测点进行修改坐标
     * @param ikey 单测点的键值
     * @param pointF 位移
     *               祁
     */
    public void translationPoint (int ikey,PointF pointF){

        Integer ik=ikey;
        float i = mDrawableMap.get(ik).getScaleX();
        float j =mDrawableMap.get(ik).getScaleY();
        float k = i+pointF.x;
        float l = j+pointF.y;
        mDrawableMap.get(ik).setScaleX(k);
        mDrawableMap.get(ik).setScaleY(l);
        invalidate();

    }

    /***
     * 改变测点的原始xy值 从而改变相对图片的位置
     * @param ikey 需要改变的测点的键
     *             祁
     */
    public void changeXY(int ikey){
        Integer ik=ikey;
        float i = mDrawableMap.get(ik).getScaleX();
        float j =mDrawableMap.get(ik).getScaleY();
        mDrawableMap.get(ik).setCoordinateX((i-bitmapOriginPoint.x)/doubleFingerScrole,imageSize);
        mDrawableMap.get(ik).setCoordinateY((j-bitmapOriginPoint.y)/doubleFingerScrole,imageSize);
    }



    /**
     * 防止移动图片超过边界，计算边界情况
     * @param moveX
     * @param moveY
     * @return
     */
    public float[] moveBorderDistance(float moveX,float moveY){
        //计算bitmap的左上角坐标
        getBitmapOffset();
        Log.e("kzg","**********************moveBorderDistance--bitmapOriginPoint:"+bitmapOriginPoint);
        //计算bitmap的右下角坐标
        float bitmapRightBottomX = bitmapOriginPoint.x + scaleSize.x;
        float bitmapRightBottomY = bitmapOriginPoint.y + scaleSize.y;

        if (moveY > 0){
            //向下滑
            if (bitmapOriginPoint.y + moveY > 0){
                if (bitmapOriginPoint.y < 0){
                    moveY = -bitmapOriginPoint.y;
                }else {
                    moveY = 0;
                }
            }
        }else if (moveY < 0){
            //向上滑
            if (bitmapRightBottomY + moveY < viewSize.y){
                if (bitmapRightBottomY > viewSize.y){
                    moveY = -(bitmapRightBottomY - viewSize.y);
                }else {
                    moveY = 0;
                }
            }
        }

        if (moveX > 0){
            //向右滑
            if (bitmapOriginPoint.x + moveX > 0){
                if (bitmapOriginPoint.x < 0){
                    moveX = -bitmapOriginPoint.x;
                }else {
                    moveX = 0;
                }
            }
        }else if (moveX < 0){
            //向左滑
            if (bitmapRightBottomX + moveX < viewSize.x){
                if (bitmapRightBottomX > viewSize.x){
                    moveX = -(bitmapRightBottomX - viewSize.x);
                }else {
                    moveX = 0;
                }
            }
        }
        return new float[]{moveX,moveY};
    }

    /**
     * 获取view中bitmap的坐标点
     */
    public void getBitmapOffset(){
        float[] value = new float[9];
        float[] offset = new float[2];
        Matrix imageMatrix = getImageMatrix();
        imageMatrix.getValues(value);
        offset[0] = value[2];
        offset[1] = value[5];
        bitmapOriginPoint.set(offset[0],offset[1]);
    }


    /**
     * 计算零个手指间的距离
     * @param event
     * @return
     */
    public static float  getDoubleFingerDistance(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return  (float)Math.sqrt(x * x + y * y) ;
    }




    /*********** 测点的存储 查找 移动标志**********/
    //存储键值对的map
    public static HashMap<Integer, PointWidgit> mDrawableMap=new HashMap<>();
    //Map 键值
    private int key = 0;
    //点控件列表
    public ArrayList<PointWidgit> mDrawableList = new ArrayList<>();

    //添加点控件的键值对
    public boolean addCanvasDrawableMap(int key,PointWidgit pointWidgit) {
        mDrawableMap.put(key,pointWidgit);
        return true;
    }

    //蓝牙与存储信息配对
    public int judge(String expression){
        if(!expression.equals("")){
            String[] arr = expression.split("Z");
            return   StartMeasureActivity.index;
//            return   StartMeasureActivity.index = 3 ;
        }else if(expression.equals("K1")){
//            return   StartMeasureActivity.index = BluetoothConnectActivity.a ;
            return   StartMeasureActivity.index = proceedingMeasurement.cont;
        }else return -1;
    }
    //转换成字符串
    protected String changeStr(int i){
        String result = 'Z'+String.valueOf(i);
        return result;
    }
    // 根据获得键值获取点位置信息
    public PointF getValue(int key){
        float x = mDrawableMap.get(key).getScaleX();
        float y = mDrawableMap.get(key).getScaleY();
        PointF point1 =new PointF(x,y);
        return point1;
    }


    public  void doubleClickPoint(String expression){
        PointF point =getValue(judge(expression));
        if (zoomInMode == ZoomMode.Ordinary) {
            float chooseX = point.x*originScale.x+bitmapOriginPoint.x;
            float chooseY = point.y*originScale.y+bitmapOriginPoint.y;
            //临时点坐标数据,//分别记录被点击的点到图片左上角x,y轴的距离与图片x,y轴边长的比例，方便在进行缩放后，算出这个点对应的坐标点
            tempPoint.set((chooseX - bitmapOriginPoint.x) / scaleSize.x, (chooseY - bitmapOriginPoint.y) / scaleSize.y);//比例
            //进行缩放
            scaleImage(new PointF(originScale.x * doubleClickZoom, originScale.y * doubleClickZoom));
            scalePoint(new PointF(originScale.x * doubleClickZoom, originScale.y * doubleClickZoom));
            //获取缩放后，图片左上角的xy坐标
            getBitmapOffset();
            Log.e("kzg","9**********************bitmapOriginPoint:"+bitmapOriginPoint);
            //平移图片，使得被点击的点的位置不变。这里是计算缩放后被点击的xy坐标，与原始点击的位置的xy 双击中心放大
            translationImage(new PointF(chooseX - (bitmapOriginPoint.x + tempPoint.x * scaleSize.x), chooseY - (bitmapOriginPoint.y + tempPoint.y * scaleSize.y)));
            translationPoints(new PointF(chooseX - (bitmapOriginPoint.x + tempPoint.x * scaleSize.x), chooseY - (bitmapOriginPoint.y + tempPoint.y * scaleSize.y)));
            //模式更改 双击放大模式
            zoomInMode = ZoomMode.ZoomIn;
            //双指缩放比例,并在双击放大后记录缩放比例
            doubleFingerScrole = originScale.x*doubleClickZoom;
        } else {
            //双击还原
            showCenter();
            zoomInMode = ZoomMode.Ordinary;
        }

    }
    //测点变红
    public Paint changeColor(int key){
        Paint mpaint = new Paint();
        mpaint.setColor(Color.RED);
//        mDrawableMap.get(key).setmPaint(mPaint);
        return mpaint;
    }
    //测点恢复颜色
    public Paint backColor(int key){
        Paint mpaint = new Paint();
        mpaint.setColor(Color.YELLOW);
        return mpaint;
    }



    //判断点击是否在控件坐标,返回key
    private int getDownWidget(float x,float y ) {
        Set<Integer> integers = mDrawableMap.keySet();
        for (Integer key :integers) {
            float xcoords = mDrawableMap.get(key).getScaleX();
            float ycoords = mDrawableMap.get(key).getScaleY();
            double abs = Math.sqrt((x - xcoords) * (x - xcoords) + (y  - ycoords) * (y  - ycoords));
            //点落在控件内
            if (abs < mDrawableMap.get(key).getRADIUS()*1.5) {
//                Log.d(TAG, "getDownWidget: 111");
                return key;
            }

        }
        return -1;
    }
    //设置拖拽模式是否
    public void setDragMode(boolean dragMode) {
        isDragWidgetMode = dragMode;
    }
    //判断是否为拖拽模式
    public boolean isDragWidgetMode() {
        return isDragWidgetMode;
    }
    //设置选中模式是否
    public void setSelectMode(boolean selectMode) {
        isSelecWidgetMode = selectMode;
    }
    //判断是否为拖拽模式
    public boolean isSelecWidgetMode() {
        return isSelecWidgetMode;
    }
    //测点增加
    private boolean pointAdd(float x, float y, Paint paint,TextPaint textPaint){
        PointWidgit mPointWidgit = new PointWidgit(x, y, paint,textPaint);
        addCanvasDrawableMap(key++, mPointWidgit);
        return true;
    }

    //数据重载
//    public static void reloadPoints(Context context){
//        int i=0;
//        SharedPreferences points_data = context.getSharedPreferences("error_data", MODE_PRIVATE);;
//        //获取编辑器对象
//        SharedPreferences.Editor editor = points_data.edit();
//        Set<Integer> integers = mDrawableMap.keySet();
//        for (Integer ketSet:integers) {
//            float cx=points_data.getFloat("Z"+i +".x",Float.parseFloat(pointCoordiniates[i][0]));
//            float cy=points_data.getFloat("Z"+i+".y",Float.parseFloat(pointCoordiniates[i][1]));
//
//            i++;
//            Objects.requireNonNull(mDrawableMap.get(ketSet)).setCoordinateX(cx);
//            Objects.requireNonNull(mDrawableMap.get(ketSet)).setCoordinateY(cy);
//
//        }
//        editor.apply();//数据提交
//    }


}


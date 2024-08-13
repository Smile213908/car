package com.example.hotel.util;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class port {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
//    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int POST = 2;
    private Button login, logout, instool, point, batchupdate, update, view, save, checksave;
    private TextView tv_result, tv;
    private OkHttpClient client = new OkHttpClient();
    private static final int GET = 1;
    private String result;
    private ImageView img;

    private String token, customerName, customerLogo, pointCode, insToolName, imageUrl, checkUser, checkCode, checkBatchNumber;
    private Double total, id, pointId, upperTolerance, lowerTolerance, correctValue, insToolId, centerLeft, centerTop;
    private Double pointLeft, pointTop, checkValue, code;
    private Integer pageNum, pageSize, checkResult;

    private Handler handlerlogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    tv_result.setText(strData);
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    code = (Double) jsonMap.get("code");
                    token = data.get("token").toString();   //token
                    Map userInfo = (Map) data.get("userInfo");
                    customerName = userInfo.get("customerName").toString();  //客户名称
                    customerLogo = userInfo.get("customerLogo").toString();  //客户logo
                    tv.setText(customerLogo);
                    if(customerLogo != null){

                    }
                    tv.setText(code.toString());

                    break;
            }
        }
    };
    private Handler handlerlogout = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    tv_result.setText(jsonMap.get("msg").toString());

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
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    total = (Double) data.get("total");
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("list");  //列表
                    for (Integer i = 0; i < total; i++) {
                        Map list = (Map) listtotal.get(i);
                        id = (Double) list.get("id");  //id
                        insToolName = (String) list.get("insToolName");  //检具名称
                        imageUrl = (String) list.get("imageUrl");   //检具图片
                    }
                    tv_result.setText(insToolName);

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
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    total = (Double) data.get("total");
                    List<Map<String, Object>> listtotal = (List<Map<String, Object>>) data.get("list");  //列表
                    for (Integer i = 0; i < total; i++) {
                        Map list = (Map) listtotal.get(i);
                        pointId = (Double) list.get("pointId");  //pointId
                        pointCode = (String) list.get("pointCode");
                        insToolId = (Double) list.get("insToolId");
                        upperTolerance = (Double) list.get("upperTolerance");
                        lowerTolerance = (Double) list.get("lowerTolerance");
                        correctValue = (Double) list.get("correctValue");

                    }
                    tv_result.setText(upperTolerance.toString());
//                    tv_result.setText(jsonMap.get("msg").toString());

                    break;
            }
        }
    };


    private Handler handlerbatchupdate = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    tv_result.setText(jsonMap.get("msg").toString());

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
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    tv.setText(jsonMap.get("msg").toString());

                    break;
            }
        }
    };


    private Handler handlerview = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    insToolName = (String) data.get("insToolName");  //检具名称
                    imageUrl = (String) data.get("imageUrl");  //检具图片
                    List<Map<String, Object>> pointList = (List<Map<String, Object>>) data.get("pointList");  //列表
                    for (Integer i = 0; i < pointList.size(); i++) {
                        Map list = (Map) pointList.get(i);
                        pointId = (Double) list.get("pointId");
                        pointLeft = (Double) list.get("pointLeft");
                        pointTop = (Double) list.get("pointTop");
                        pointCode = (String) list.get("pointCode");
                        upperTolerance = (Double) list.get("upperTolerance");
                        lowerTolerance = (Double) list.get("lowerTolerance");
                    }
                    tv.setText(pointLeft.toString());
                    tv_result.setText(pointCode);

                    break;
            }
        }
    };


    private Handler handlersave = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    tv_result.setText(jsonMap.get("msg").toString());

                    break;
            }
        }
    };
    private Handler handlerchecksave = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case POST:
                    tv.setText("");
                    tv_result.setText("");
                    String strData = (String) msg.obj;
                    Gson gson = new Gson();
                    Map<String, Object> jsonMap = gson.fromJson(strData, new TypeToken<Map<String, Object>>() { }.getType());
                    Map data = (Map) jsonMap.get("data");
                    tv.setText(jsonMap.get("msg").toString());

                    break;
            }
        }
    };
    private void postLogin() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/user/login", "{\n" +
                            "    \"account\":\"北汽芜湖分厂\",\n" +
                            "    \"password\":\"123456\"\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerlogin.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
    private void postInstool() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/insTool/list", "{\n" +
                            "    \"pageNum\":"+pageNum+",\n" +
                            "    \"pageSize\":"+pageSize+"\n" +
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
    private void postPoint() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/list", "{\n" +
                            "    \"pageNum\":"+pageNum+",\n" +
                            "    \"pageSize\":"+pageSize+",\n" +
                            "    \"insToolId\": "+insToolId+""+
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
    private void postBatchupdate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/batchUpdate", "{\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"upperTolerance\":"+upperTolerance+",\n" +
                            "    \"lowerTolerance\":"+lowerTolerance+",\n" +
                            "    \"correctValue\":"+correctValue+"\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerbatchupdate.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private void postUpdate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/update", "[\n" +
                            "    {\n" +
                            "    \"pointId\":1,\n" +
                            "    \"insToolId\":1,\n" +
                            "    \"upperTolerance\":0.5,\n" +
                            "    \"lowerTolerance\":-0.6,\n" +
                            "    \"correctValue\":3\n" +
                            "    },{\n" +
                            "    \"pointId\":"+pointId+",\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"upperTolerance\":"+upperTolerance+",\n" +
                            "    \"lowerTolerance\":"+lowerTolerance+",\n" +
                            "    \"correctValue\":"+correctValue+"\n" +
                            "    }\n" +
                            "]");

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
    private void postView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/view", "{\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"centerLeft\":"+centerLeft+",\n" +
                            "    \"centerTop\":"+centerTop+",\n" +
                            "    \"pageNum\":"+pageNum+"\n" +
                            "}");

                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerview.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private void postSave() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    result = post("http://81.69.170.198:8002/point/save", "{\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"centerLeft\":"+centerLeft+",\n" +
                            "    \"centerTop\":"+centerTop+",\n" +
                            "    \"pageNum\":"+pageNum+",\n" +
                            "    \"pointInfoList\":[{\n" +
                            "            \"pointId\": "+pointId+",\n" +
                            "            \"pointLeft\": "+pointLeft+",\n" +
                            "            \"pointTop\": "+pointTop+"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"pointId\": 2,\n" +
                            "            \"pointLeft\": 175.97,\n" +
                            "            \"pointTop\": 87.77\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"pointId\": 3,\n" +
                            "            \"pointLeft\": 635.60,\n" +
                            "            \"pointTop\": 91.66\n" +
                            "        }]\n" +
                            "}");

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
    private void postChecksave() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {/*
                    result = post("http://81.69.170.198:8002/point/checkSave", "{\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"checkUser\":"+checkUser+",\n" +
                            "    \"checkCode\":"+checkCode+",\n" +
                            "    \"checkBatchNumber\":"+checkBatchNumber+",\n" +
                            "    \"pointDetail\":[{\n" +
                            "        \"pointId\":"+pointId+",\n" +
                            "        \"checkResult\":"+checkResult+",\n" +
                            "        \"checkValue\":"+checkValue+"\n" +
                            "    },{\n" +
                            "        \"pointId\":2,\n" +
                            "        \"checkResult\":2\n" +
                            "    },{\n" +
                            "        \"pointId\":3,\n" +
                            "        \"checkResult\":1,\n" +
                            "        \"checkValue\":0.2\n" +
                            "    }]\n" +
                            "}");*/

                    result = post("http://81.69.170.198:8002/point/checkSave", "{\n" +
                            "    \"insToolId\":"+insToolId+",\n" +
                            "    \"checkUser\":\""+checkUser+"\",\n" +
                            "    \"checkCode\":\""+checkCode+"\",\n" +
                            "    \"checkBatchNumber\":\""+checkBatchNumber+"\",\n" +
                            "    \"pointDetail\":[{\n" +
                            "        \"pointId\":"+pointId+",\n" +
                            "        \"checkResult\":"+checkResult+",\n" +
                            "        \"checkValue\":"+checkValue+"\n" +
                            "    },{\n" +
                            "        \"pointId\":2,\n" +
                            "        \"checkResult\":2\n" +
                            "    },{\n" +
                            "        \"pointId\":3,\n" +
                            "        \"checkResult\":1,\n" +
                            "        \"checkValue\":0.2\n" +
                            "    }]\n" +
                            "}");
                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handlerchecksave.sendMessage(msg);
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
                .addHeader("authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}

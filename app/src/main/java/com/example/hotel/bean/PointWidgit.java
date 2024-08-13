package com.example.hotel.bean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.TextPaint;

/**************************************
 * 内部类点控件
 *
 ********************************************/
public  class PointWidgit  {
    private float RADIUS = 15;                        //半径
    private float coordinateX, coordinateY;           //控件的根坐标
    private float scaleX, scaleY;                     //当前缩放后坐标

    private Paint Paint2;
    //画笔
    private Paint mPaint;
    private TextPaint textPaint;

    //构造方法
    public PointWidgit(float coordinateX, float coordinateY, Paint mPaint, TextPaint textPaint) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.Paint2 = mPaint;
        this.textPaint=textPaint;
    }

    public  float getRADIUS() {
        return RADIUS;
    }

    public float getCoordinateX(PointF imageSize) {
        return coordinateX*imageSize.x;
    }

    public float getCoordinateY(PointF imageSize) {
        return coordinateY*imageSize.y;
    }

    public Paint getmPaint() {
        return Paint2;
    }

    public Paint getPaint2() {
        return Paint2;
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public float getScaleX() {            return scaleX;        }

    public float getScaleY() {            return scaleY;        }

    public void setCoordinateX(float coordinateX,PointF imageSize) {
        this.coordinateX = coordinateX/imageSize.x;
    }

    public void setCoordinateY(float coordinateY,PointF imageSize) {
        this.coordinateY = coordinateY/imageSize.y;
    }

    public void setmPaint(Paint mPaint) {
        this.Paint2 = mPaint;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setPaint2(Paint paint2) {
        Paint2 = paint2;
    }

    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
    }

    public void draw(Canvas canvas, Paint paint,TextPaint textPaint ,String st) {
        setmPaint(paint);
        canvas.drawCircle(scaleX, scaleY, 15, Paint2);
        canvas.drawText(st,scaleX, scaleY, textPaint);
    }

    @Override
    public String toString() {
        return "PointWidgit{" +
                "RADIUS=" + RADIUS +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", Paint2=" + Paint2 +
                ", mPaint=" + mPaint +
                ", textPaint=" + textPaint +
                '}';
    }
}

package com.example.hotel.ui;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.customview.widget.ViewDragHelper;

/**
 * Created by bss on 2016/10/18.
 */
public class DragLayout extends RelativeLayout {

    private ViewDragHelper viewDragHelper;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //当child 为  FrameLayout 可拖拽,别的不可拖拽
                //这个可以自己定义为自己想要的布局
                if (child instanceof FrameLayout) {
                    return true;
                }
                return false;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 100;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 100;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}

package com.yzr.switchbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


/**
 * 用法：
 <SwitchButton
 xmlns:custom="http://schemas.android.com/apk/res-auto"
 android:id="@+id/sb_yejian"
 android:layout_width="50dp"
 android:layout_height="30dp"
 custom:buttonOnsrc="@drawable/white_shape"
 custom:buttonOffsrc="@drawable/white_shape"
 custom:bgOffsrc="@drawable/bg_switchbtn"
 custom:bgOnsrc="@drawable/bg_switchbtn1"/>
 *
 * @Description 自定义滑动开关
 * @Author yzr/609560302@qq.com
 * @Date 2016/3/22 16:07
 */
public class SwitchButton extends View {

    /**
     * Scroller滑动过程中
     */
    private boolean isScrolling = false;
    /**
     * Touch滑动过程中
     */
    private boolean isMoving = false;
    /**
     * 上次的滑动位置
     */
    private int Lastx = 0;
    /**
     * 滑动按钮X轴位置
     */
    private int X_btn;
    Scroller mScroller;
    /**
     * 滑动开关图片
     */
    private Drawable button_on;
    private Drawable button_off;
    /**
     * 背景图片
     */
    private Drawable bg_drawable_on;
    private Drawable bg_drawable_off;
    /**
     * 开关状态
     */
    private boolean  isOpen = false;
    private OnSwitchChangeListener mOnChangeListener;
    /**
     * 定义Switch控件的转换监听事件
     *
     */
    public interface OnSwitchChangeListener{
        void onSwitchChange(boolean control);
    };

    public OnSwitchChangeListener getOnSwitchChangeListener(){
        return mOnChangeListener;
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener){
        this.mOnChangeListener = onSwitchChangeListener;
    }

    /**
     * 判断Switch状态，设置Switch放置的位置
     */
    public boolean getSwitchStatus(){
        return isOpen ;
    }

    public SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        button_on = a.getDrawable(R.styleable.SwitchButton_buttonOnsrc);
        button_off = a.getDrawable(R.styleable.SwitchButton_buttonOffsrc);
        bg_drawable_on = a.getDrawable(R.styleable.SwitchButton_bgOnsrc);
        bg_drawable_off = a.getDrawable(R.styleable.SwitchButton_bgOffsrc);
        a.recycle();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        button_on = a.getDrawable(R.styleable.SwitchButton_buttonOnsrc);
        button_off = a.getDrawable(R.styleable.SwitchButton_buttonOffsrc);
        bg_drawable_on = a.getDrawable(R.styleable.SwitchButton_bgOnsrc);
        bg_drawable_off = a.getDrawable(R.styleable.SwitchButton_bgOffsrc);
        a.recycle();
    }

    //设置开关图片
    public void setButtonOnSrc(int ResourceId){
        button_on = ContextCompat.getDrawable(getContext(),ResourceId);
    }
    public void setButtonOffSrc(int ResourceId){
        button_off =  ContextCompat.getDrawable(getContext(),ResourceId);
    }

    public void setBgOnSrc(int ResourceId){
        bg_drawable_on = ContextCompat.getDrawable(getContext(),ResourceId);
    }

    public void setBgOffSrc(int ResourceId){
        bg_drawable_off = ContextCompat.getDrawable(getContext(),ResourceId);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension((int) (button_on.getIntrinsicWidth()*2.5),button_on.getIntrinsicHeight());
        }else if(widthSpecMode == MeasureSpec.AT_MOST ){
            setMeasuredDimension((int) (button_on.getIntrinsicWidth()*2.5),heightSpecSize);
        }else if(heightSpecMode == MeasureSpec.AT_MOST ){
            setMeasuredDimension(widthSpecSize,button_on.getIntrinsicHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int bg_padding = 0;


        if(isScrolling || isMoving){
            int middle = width/2;
            if( X_btn <= middle){
                bg_drawable_off.setBounds(0,bg_padding,width,height-bg_padding);
                //bg_drawable_off.setAlpha((int) (0xff * (1-(float)X_btn/middle)));
                bg_drawable_off.draw(canvas);
                button_off.setBounds(X_btn+1, 1, X_btn+height-1, height-1);
                button_off.draw(canvas);
            }else{
                bg_drawable_on.setBounds(0,bg_padding,width,height-bg_padding);
                //bg_drawable_on.setAlpha((int) (0xff * (float)((X_btn-middle)/middle)));
                bg_drawable_on.draw(canvas);
                button_on.setBounds(X_btn, 0, X_btn+height-1, height-1);
                button_on.draw(canvas);
            }

        }else{
            if(isOpen){
                bg_drawable_on.setBounds(0,bg_padding,width,height-bg_padding);
                bg_drawable_on.draw(canvas);
                X_btn = getWidth()-getHeight();
                button_on.setBounds(X_btn+1,1,X_btn+getHeight()-1,getHeight()-1);
                button_on.draw(canvas);

            }else{
                bg_drawable_off.setBounds(0,bg_padding,width,height-bg_padding);
                bg_drawable_off.draw(canvas);
                X_btn = 0;
                button_off.setBounds(X_btn+1,1,X_btn+getHeight()-1,getHeight()-1);
                button_off.draw(canvas);
            }
        }


    }

    public void switch_click(){
        if(isOpen){
            close();
        }else{
            open();
        }
    }
    /**
     *
     */
    public void open(){
        if(isOpen){
            return;
        }
        scrolltoopen();
        isOpen = true;
        mOnChangeListener.onSwitchChange(isOpen);
    }

    public void close(){
        if(!isOpen){
            return;
        }
        scrolltoclose();
        isOpen = false;
        mOnChangeListener.onSwitchChange(isOpen);
    }

    private void scrolltoopen(){
        isScrolling = true;
        mScroller.startScroll(X_btn,0, getWidth()-getHeight()-X_btn, 0,1000);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    private void scrolltoclose(){
        isScrolling = true;
        mScroller.startScroll(X_btn, 0, 0-X_btn, 0,1000);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }



    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            //scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            X_btn = mScroller.getCurrX();
            Log.e("SCROLL"," X_btn: " + X_btn+"");
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }else{
            isScrolling =false;
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                isMoving = true;
                int dx = x-Lastx;
                Log.e("MOVE"," X: "+x);
                X_btn = Math.min(Math.max(x,0),getWidth()-getHeight());
                Log.e("MOVE"," X_btn: "+X_btn);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                //大于200ms判断为滑动事件，小于200ms为点击事件
                if(Math.abs(event.getEventTime() - event.getDownTime())>200) {
                    if (X_btn > (getWidth()-getHeight()) / 2) {
                        open();
                    } else {
                        close();
                    }
                }else {
                    switch_click();
                }
                break;
        }
        Lastx = x;
        return true;
    }
}

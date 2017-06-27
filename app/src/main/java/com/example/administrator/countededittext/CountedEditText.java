package com.example.administrator.countededittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：alsoWell on 2017/6/6 17:50
 * 邮箱：1161882463@qq.com
 */
public class CountedEditText extends android.support.v7.widget.AppCompatEditText{

    private int mCountColor; //计数颜色
    private float mCountedSize;//计数的大小
    private int mOverCountColor;//计数越界颜色
    private int mCountMaxNum;// 设置可输入最大值
    private int mCountedMode;//计数模式  0 正常  1  为 2/3 模式
    private StringBuffer countString;
    private Paint paint;
    private TextPaint countPaint;
    private int presentCount;//当前长度
    private boolean isOverCount=false;
    private float mPaddingToBottom;
    private float mPaddingToRight;

    public void changeCountedMode() {
        mCountedMode = mCountedMode==0?1:0;
    }

    //当前是否超过约定数目
    public boolean isOverCount() {
        return isOverCount;
    }

    public void setOverCount(boolean overCount) {
        isOverCount = overCount;
    }

    public CountedEditText(Context context) {
        super(context);
        initEdit();
    }

    public CountedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initEdit();
    }


    public CountedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initEdit();

    }



    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountedEditText);
        try {
            mCountColor = mTypedArray.getColor(R.styleable.CountedEditText_countColor, Color.parseColor("#000000"));
            mCountedSize = mTypedArray.getDimension(R.styleable.CountedEditText_countSize,40);
            mOverCountColor = mTypedArray.getColor(R.styleable.CountedEditText_overCountColor, Color.RED);
            mCountMaxNum = mTypedArray.getInteger(R.styleable.CountedEditText_maxNum, Integer.MAX_VALUE);
            mCountedMode = mTypedArray.getInt(R.styleable.CountedEditText_mode, 0);
            mPaddingToBottom = mTypedArray.getDimension(R.styleable.CountedEditText_paddingToBottom, 0);
            mPaddingToRight = mTypedArray.getDimension(R.styleable.CountedEditText_paddingToRight, 0);
        } finally {
            mTypedArray.recycle();
        }
    }

    private void initEdit() {
        // 用来存放右下角的计数内容的字符串
        countString = new StringBuffer();
        if (mCountedMode == 1) {
            countString.append(presentCount);
            countString.append(" / ");
            countString.append(mCountMaxNum);
        } else if (mCountedMode == 0) {
            countString.append(mCountMaxNum - presentCount);
        }
        // 初始化字符计数的画笔
        countPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        countPaint.setTextSize(mCountedSize);
        countPaint.setColor(mCountColor);

        // 初始化监听器
        initListener();

    }

    private void initListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 当内容改变后，计数、并判断是否超过规定字符长度
                if (mCountMaxNum != -1) {
                    // 添加计数字符串
                    countString.delete(0, countString.length());
                    presentCount = s.length();
                    if (mCountedMode == 1) {
                        countString.append(presentCount);
                        countString.append(" / ");
                        countString.append(mCountMaxNum);
                    } else if (mCountedMode == 0) {
                        countString.append(mCountMaxNum - presentCount);
                    }
                    // 超过规定长度时，绘制的颜色发生变化
                    if (presentCount > mCountMaxNum) {
                        isOverCount=true;
                        countPaint.setColor(mOverCountColor);
                    } else {
                        isOverCount = false;
                        countPaint.setColor(mCountColor);
                    }
                }
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 获取焦点时
                if (hasFocus) {
                    if (presentCount > mCountMaxNum && mCountMaxNum != -1) {
                        // 超出字符长度时，设置画笔颜色
                        countPaint.setColor(mOverCountColor);
                    } else {
                        // 不超出字符长度/不设置规定长度时，设置画笔颜色
                        countPaint.setColor(mCountColor);
                    }
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置edittext的背景为空，主要为了隐藏自带的下划线
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mMeasuredHeight = getMeasuredHeight();
        int mMeasuredWidth = getMeasuredWidth();
        int mTextWidth = getTextWidth(countString.toString(), countPaint);
        if (mCountMaxNum != -1) {
            canvas.drawText(countString.toString(), mMeasuredWidth- this.getPaddingRight()-
                            mTextWidth-mPaddingToRight,
                    mMeasuredHeight-this.getPaddingBottom()-mPaddingToBottom, countPaint);
        }
        super.onDraw(canvas);
    }

    // 获取字符串的宽度
    private int getTextWidth(String text, TextPaint paint) {
        return (int) paint.measureText(text);
    }

    // 获取文字的高度
    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.descent - fontMetrics.ascent;
    }


}

package net.anumbrella.customedittext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by anumbrella on 16/3/8.
 * <p/>
 * 自定义EditText视图
 */
public class MyEditText extends EditText {

    /**
     * 右边的视图
     */

    private final Drawable mRightDrawable = getCompoundDrawables()[2];

    /**
     * 下滑线绘制画笔
     */
    private Paint underlinePaint;


    /**
     * 输入框是否有聚焦
     */
    private boolean isHasFocus;


    /**
     * 设置选中的颜色
     */
    private int selectColor;


    /**
     * 设置没有选中的颜色
     */
    private int unSelectColor;


    /**
     * 设置出错时的颜色
     */
    private int errorColor;


    /**
     * 下划线的高度
     */
    private float underlineHeight;


    /**
     * 是否显示EditText中带删除的图标
     */
    private boolean displayEditTextDelete = false;


    /**
     * 选中时下滑线的高度
     */
    private float selectUnderlineHeight;


    /**
     * 没有选中时下划线的高度
     */
    private float unSelectUnderlineHeight;


    /**
     * 错误时下滑线的高度
     */
    private float errorUnderlineHeight;


    public MyEditText(Context context) {
        this(context, null);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.MyEditTextStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) {
            return;
        }

        //获取资源对象
        final Resources res = getResources();

        //EditText选中时的颜色
        final int defaultSelectColor = res.getColor(R.color.defaultSelectedColor);
        //EditText没有选中时的颜色
        final int defaultUnSeletColor = res.getColor(R.color.defaultUnselectedColor);
        //EditText选择错误时的颜色
        final int defaultErrorColor = res.getColor(R.color.defaultErrorColor);
        //EditText错误时的高度
        final float defaultErrorUnderlineHeight = res.getDimension(R.dimen.errorUnderlineHeight);
        //EditText选中时的高度
        final float defaultSelectUnderlineHeight = res.getDimension(R.dimen.selectUnderlineHeight);
        //EditText没有选中时的高度
        final float defaultUnselectUnderlineHeight = res.getDimension(R.dimen.unSelectUnderlineHeight);


        //获取在xml里定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextStyle, defStyle, 0);


        displayEditTextDelete = array.getBoolean(R.styleable.MyEditTextStyle_displayDelete, displayEditTextDelete);

        selectColor = array.getColor(R.styleable.MyEditTextStyle_selectedColor, defaultSelectColor);

        unSelectColor = array.getColor(R.styleable.MyEditTextStyle_unselectedColor, defaultUnSeletColor);

        errorColor = array.getColor(R.styleable.MyEditTextStyle_errorColor, defaultErrorColor);

        errorUnderlineHeight = array.getDimension(R.styleable.MyEditTextStyle_errorUnderlineHeight, defaultErrorUnderlineHeight);

        selectUnderlineHeight = array.getDimension(R.styleable.MyEditTextStyle_selectUnderlineHeight, defaultSelectUnderlineHeight);

        unSelectUnderlineHeight = array.getDimension(R.styleable.MyEditTextStyle_unSelectUnderlineHeight, defaultUnselectUnderlineHeight);

        array.recycle();

        init();

    }


    private void init() {

        initPaint();

        this.setFocusable(true);
        this.setFocusableInTouchMode(true);


        /**
         * 在xml中定义的图片资源
         *
         * 该方法返回包含控件左,上,右,下四个位置的Drawable的数组
         * */

        // 为EditText编辑框添加焦点变化监听(当前对象为编辑框)
        this.setOnFocusChangeListener(new FocusChangeListener());
        // 为EditText编辑框添加输入文字变化监听
        this.addTextChangedListener(new TextChangeListener());

        // 设置清除按钮图标是否可见
        setDrawableVisible(false);

    }

    private void initPaint() {

        underlinePaint = new Paint();
        setUnderlinePaintColor(unSelectColor);
        underlinePaint.setStyle(Paint.Style.STROKE);
        setUnderlineHeight(unSelectUnderlineHeight);

    }


    /**
     * (模拟点击清除图标的操作)
     * <p/>
     * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作
     * <p/>
     * getWidth():得到控件的宽度
     * <p/>
     * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * <p/>
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * <p/>
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离 于是:
     * <p/>
     * getWidth() - getTotalPaddingRight()表示: 控件左边到clean的图标左边缘的区域
     * <p/>
     * getWidth() - getPaddingRight()表示: 控件左边到clean的图标右边缘的区域
     * 所以这两者之间的区域刚好是clean的图标的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 模拟点击了清除图标按钮的事件
                if ((event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()))) {
                    setText("");
                }
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 设置清除按钮的图标是否可见
     *
     * @param isVisibleClean
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setDrawableVisible(boolean isVisibleClean) {
        Drawable rightDrawable;
        if (isVisibleClean && displayEditTextDelete) {
            if (mRightDrawable == null) {
                rightDrawable = getResources().getDrawable(R.drawable.ic_edittext_delete);
                rightDrawable.setBounds(0, 0, 75, 75);
            } else {
                rightDrawable = mRightDrawable;
            }

        } else {
            rightDrawable = null;
        }


        // 重新设置当前位置左,上,右,下的图片
        // 使用代码设置该控件left, top, right, and bottom处的图标
        // 重新更新四个图片的资源
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], rightDrawable,
                getCompoundDrawables()[3]);

    }

    /**
     * 自定义绘制
     *
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawLine(0, this.getHeight() - underlineHeight / 2, this.getWidth(),
                this.getHeight() - underlineHeight / 2, underlinePaint);

    }

    /**
     * 设置下滑线的高度
     *
     * @param height
     */
    public void setUnderlineHeight(float height) {
        this.underlineHeight = height;
        underlinePaint.setStrokeWidth(underlineHeight);
    }


    /**
     * 设置下滑线的颜色
     *
     * @param color
     */
    public void setUnderlinePaintColor(int color) {
        this.underlinePaint.setColor(color);
    }


    /**
     * 焦点获取监听类
     */
    private class FocusChangeListener implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {
                // 如果编辑框有输入文字,就显示删除的图片按钮
                boolean isVisible = getText().toString().length() >= 1;
                setDrawableVisible(isVisible);
                setUnderlinePaintColor(selectColor);
                setUnderlineHeight(selectUnderlineHeight);

                invalidate();

            } else {
                setDrawableVisible(false);
                setUnderlinePaintColor(unSelectColor);
                setUnderlineHeight(unSelectUnderlineHeight);
                invalidate();
            }
        }
    }


    /**
     * EditText编辑框的监听类,监听编辑框的输入的变化的改变
     */
    private class TextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 监听输入变化后的编辑框是否有内容
            boolean isVisible = getText().toString().length() >= 1;
            setDrawableVisible(isVisible);

        }
    }
}

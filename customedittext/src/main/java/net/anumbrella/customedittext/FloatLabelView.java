package net.anumbrella.customedittext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * author：Anumbrella
 * Date：16/4/26 下午3:53
 * 自定义视图的EditText
 */
public class FloatLabelView extends FrameLayout {


    /**
     * 视图状态保存索引key
     */
    private static final String SAVE_STATE_KEY_EDIT_TEXT = "saveStateEditText";

    private static final String SAVE_STATE_KEY_LABEL = "saveStateLabel";

    private static final String SAVE_STATE_PARENT = "saveStateParent";

    private static final String SAVE_STATE_TAG = "saveStateTag";

    private static final String SAVE_STATE_KEY_FOCUS = "saveStateFocus";


    /**
     * FloatLable与左边的margin距离
     */
    private float leftDistance;


    /**
     * FloatLable与EditText距离
     */
    private float bottomDistance;


    /**
     * 绑定的EditText的视图
     */
    private EditText mEditText;


    /**
     * 浮动的文本视图
     */
    private TextView mLabel;

    /**
     * 保存视图的状态
     */
    private Bundle mSavedState;


    /**
     * Label是否显示
     */
    private boolean mLabelShowing;


    /**
     * 是否TextView显示忽略动画
     */
    private boolean mSkipAnimation = false;


    /**
     * 初始化视图是否完成，在未完成之前不能添加子视图
     */
    private boolean mInitComplete = false;


    /**
     * LabelAnimator为label出现or消失的动画方式
     */
    private LabelAnimator mLabelAnimator = new DefaultLabelAnimator();


    /**
     * 提供自定义的浮动的动画
     */
    public interface LabelAnimator {
        /**
         * 当视图显示的时候调用
         *
         * @param label 显示的label
         */
        public void onDisplayLabel(View label);

        /**
         * 当视图隐藏的时候调用
         *
         * @param label 隐藏的label
         */
        public void onHideLabel(View label);

    }


    public FloatLabelView(Context context) {
        this(context, null, 0);
    }

    public FloatLabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @Override
    public void addView(View child) {
        if (mInitComplete) {
            throw new UnsupportedOperationException("你不能添加一个视图到FloatLabelView中");
        } else {
            super.addView(child);
        }
    }


    @Override
    public void addView(View child, int index) {
        if (mInitComplete) {
            throw new UnsupportedOperationException("你不能添加一个视图到FloatLabelView中");
        } else {
            super.addView(child, index);
        }
    }


    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (mInitComplete) {
            throw new UnsupportedOperationException("你不能添加一个视图到FloatLabelView中");
        } else {
            super.addView(child, index, params);
        }
    }


    @Override
    public void addView(View child, int width, int height) {
        if (mInitComplete) {
            throw new UnsupportedOperationException("你不能添加一个视图到FloatLabelView中");
        } else {
            super.addView(child, width, height);
        }
    }


    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        if (mInitComplete) {
            throw new UnsupportedOperationException("你不能添加一个视图到FloatLabelView中");
        } else {
            super.addView(child, params);
        }
    }

    /**
     * 获取当前的EditEext资源
     *
     * @return 返回EditText
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * 获取当前的TextView资源
     *
     * @return 返回Label
     */
    public TextView getLabel() {
        return mLabel;
    }


    /**
     * 定义Label的字体，传入id资源
     *
     * @param resid 资源id
     */
    public void setLabel(int resid) {
        setLabel(getContext().getString(resid));
    }

    /**
     * 定义Label的字体，传入CharSequence资源
     *
     * @param hint CharSequence的字符队列
     */
    public void setLabel(CharSequence hint) {
        mEditText.setHint(hint);
        mLabel.setText(hint);
    }


    /**
     * 设定Label的动画，如果为null就是默认的动画
     *
     * @param labelAnimator 设定的动画
     */
    public void setLabelAnimator(LabelAnimator labelAnimator) {
        if (labelAnimator == null) {
            mLabelAnimator = new DefaultLabelAnimator();
        } else {
            mLabelAnimator = labelAnimator;
        }
    }


    /**
     * 设定EditText的字体，传入id资源
     *
     * @param resid 资源id
     */
    public void setText(int resid) {
        mEditText.setText(resid);
    }

    /**
     * 设定EditText的字体，传入char数组资源
     *
     * @param text  char数组文本
     * @param start 起始位置
     * @param len   长度
     */
    public void setText(char[] text, int start, int len) {
        mEditText.setText(text, start, len);
    }

    /**
     * 设定EditText的字体，传入id资源
     *
     * @param resid 资源di
     * @param type  type类型
     */
    public void setText(int resid, TextView.BufferType type) {
        mEditText.setText(resid, type);
    }

    /**
     * 设定EditText的字体，传入CharSequence资源
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    /**
     * 设定EditText的字体，传入CharSequence资源
     *
     * @param text CharSequence字符队列
     * @param type type类型
     */
    public void setText(CharSequence text, TextView.BufferType type) {
        mEditText.setText(text, type);
    }


    /**
     * 设定EditText并且Label没有动画，传入资源id
     *
     * @param resid 资源id
     */
    public void setTextWithoutAnimation(int resid) {
        mSkipAnimation = true;
        mEditText.setText(resid);
    }

    /**
     * 设定EditText并且Label没有动画，传入char数组
     *
     * @param text  char数组
     * @param start 起始位置
     * @param len   长度
     */
    public void setTextWithoutAnimation(char[] text, int start, int len) {
        mSkipAnimation = true;
        mEditText.setText(text, start, len);

    }

    /**
     * 设定EditText并且Label没有动画，传入资源id
     *
     * @param resid 资源id
     * @param type  type类型
     */
    public void setTextWithoutAnimation(int resid, TextView.BufferType type) {
        mSkipAnimation = true;
        mEditText.setText(resid, type);
    }

    /**
     * 设定EditText并且Label没有动画，传入资源CharSequence
     *
     * @param text CharSequence字符队列
     */
    public void setTextWithoutAnimation(CharSequence text) {
        mSkipAnimation = true;
        mEditText.setText(text);
    }

    /**
     * 设定EditText并且Label没有动画，传入资源CharSequence
     *
     * @param text 文本
     * @param type type类型
     */
    public void setTextWithoutAnimation(CharSequence text, TextView.BufferType type) {
        mSkipAnimation = true;
        mEditText.setText(text, type);
    }


    /**
     * 布局子视图
     *
     * @param changed 是否在改变
     * @param left    左边距离
     * @param top     顶部距离
     * @param right   右边距离
     * @param bottom  底部距离
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int childLeft = getPaddingLeft();
        final int childRight = right - left - getPaddingRight();

        int childTop = getPaddingTop();
        final int childBottom = bottom - top - getPaddingBottom();

        //布局子视图
        layoutChild(mLabel, childLeft, childTop, childRight, childBottom, 1);
        layoutChild(mEditText, childLeft, childTop + mLabel.getMeasuredHeight(), childRight, childBottom, 2);
    }

    /**
     * 对子视图进行布局
     *
     * @param child        子视图
     * @param parentLeft   左边距离
     * @param parentTop    顶部距离
     * @param parentRight  右边距离
     * @param parentBottom 底部距离
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void layoutChild(View child, int parentLeft, int parentTop, int parentRight, int parentBottom, int type) {
        if (child.getVisibility() != View.GONE) {
            //获取视图的属性
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            //获取视图的宽和高
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int childLeft;
            final int childTop = parentTop - lp.topMargin;

            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = Gravity.START | Gravity.TOP;
            }

            //定义视图的方向
            final int layoutDirection;
            //api支持版本
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //水平布局，从左到右
                layoutDirection = LAYOUT_DIRECTION_LTR;
            } else {
                layoutDirection = getLayoutDirection();
            }

            final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);

            //当前绝对布局和一个对齐方向的绝对水平对齐方向的二进制掩码相与
            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = parentLeft + (parentRight - parentLeft - width) / 2 + lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.END:
                    childLeft = parentRight - width - lp.rightMargin;
                    break;
                case Gravity.START:
                default:
                    childLeft = parentLeft + lp.leftMargin;
                    break;
            }

            if (type == 1) {
                child.layout(childLeft + (int) leftDistance, childTop, childLeft + width, childTop + height);
            } else if (type == 2) {
                child.layout(childLeft, childTop - (int) bottomDistance, childLeft + width, childTop + height);
            }


        }
    }


    /**
     * 视图测量大小并设置
     *
     * @param widthMeasureSpec  宽MeasureSpec
     * @param heightMeasureSpec 高MeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //恢复保存的视图状态
        if (mSavedState != null) {
            Parcelable childState = mSavedState.getParcelable(SAVE_STATE_KEY_EDIT_TEXT);
            mEditText.onRestoreInstanceState(childState);
            childState = mSavedState.getParcelable(SAVE_STATE_KEY_LABEL);
            mLabel.onRestoreInstanceState(childState);
            //是否有焦点
            if (mSavedState.getBoolean(SAVE_STATE_KEY_FOCUS, false)) {
                mEditText.requestFocus();
            }
            mSavedState = null;
        }
        measureChild(mEditText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mLabel, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

    }

    /**
     * 测量子视图的高度
     *
     * @param heightMeasureSpec 高MeasureSpec
     * @return 测量的高度
     */
    private int measureHeight(int heightMeasureSpec) {

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算视图的高
        int result = 0;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = mEditText.getMeasuredHeight() + mLabel.getMeasuredHeight();
            result += getPaddingBottom() + getPaddingTop();
            result = Math.max(result, getSuggestedMinimumHeight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量子视图的高度
     *
     * @param widthMeasureSpec 宽MeasureSpec
     * @return 返回测量的宽度
     */
    private int measureWidth(int widthMeasureSpec) {

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        //计算视图的高
        int result = 0;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = Math.max(mEditText.getMeasuredWidth(), mLabel.getMeasuredWidth());
            result = Math.max(result, getSuggestedMinimumWidth());
            result += getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    /**
     * 恢复保存的状态
     *
     * @param state 保存的state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle savedState = (Bundle) state;
            if (savedState.getBoolean(SAVE_STATE_TAG, false)) {
                //获得视图保存的信息
                mSavedState = savedState;
                super.onRestoreInstanceState(savedState.getParcelable(SAVE_STATE_PARENT));
                return;
            }
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * 保存状态
     *
     * @return 保存的state
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final Bundle saveState = new Bundle();
        saveState.putParcelable(SAVE_STATE_KEY_EDIT_TEXT, mEditText.onSaveInstanceState());
        saveState.putParcelable(SAVE_STATE_KEY_LABEL, mLabel.onSaveInstanceState());
        saveState.putBoolean(SAVE_STATE_KEY_FOCUS, mEditText.isFocused());
        saveState.putBoolean(SAVE_STATE_TAG, true);
        saveState.putParcelable(SAVE_STATE_PARENT, superState);
        return saveState;
    }


    /**
     * 如果attrs不为空就从中加载视图的默认值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //Label(TextView)视图的布局
        final int layout;

        int editTextId = R.id.edit_text;

        int floatLabelId = R.id.float_label;
        //EditText文本
        final CharSequence text;
        //EditText文本提示信息
        final CharSequence hint;
        //label字体的颜色
        final int floatLabelColor;
        //EditText输入类型
        final int inputType;
        //制定提示颜色
        final ColorStateList hintColor;
        //FloatLable字体的大小
        float labelTextSize = 15;
        //EidtText字体的大小
        float editTextSize = 15;

        //下一个焦点视图
        final int nextFocusDownId;
        final int nextFocusForwardId;
        final int nextFocusLeftId;
        final int nextFocusRightId;
        final int nextFocusUpId;

        //手机输入法软键盘回车的功能设置
        final int imeOptions;


        //FloatLable显示的方式
        final int floatLabelDisplay;


        if (attrs == null) {
            layout = R.layout.floatlabel_view;
            text = null;
            hint = null;
            hintColor = null;
            floatLabelColor = 0;
            inputType = 0;
            imeOptions = 0;

            nextFocusDownId = NO_ID;
            nextFocusForwardId = NO_ID;
            nextFocusLeftId = NO_ID;
            nextFocusRightId = NO_ID;
            nextFocusUpId = NO_ID;

        } else {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FloatLabelViewStyle, defStyleAttr, 0);

            //主要的属性设置
            layout = array.getResourceId(R.styleable.FloatLabelViewStyle_android_layout, R.layout.floatlabel_view);
            editTextId = array.getResourceId(R.styleable.FloatLabelViewStyle_editTextId, R.id.edit_text);
            floatLabelId = array.getResourceId(R.styleable.FloatLabelViewStyle_labelId, R.id.float_label);
            text = array.getText(R.styleable.FloatLabelViewStyle_android_text);
            hint = array.getText(R.styleable.FloatLabelViewStyle_android_hint);
            hintColor = array.getColorStateList(R.styleable.FloatLabelViewStyle_android_textColorHint);
            floatLabelColor = array.getColor(R.styleable.FloatLabelViewStyle_floatLabelColor, 0);
            inputType = array.getInt(R.styleable.FloatLabelViewStyle_android_inputType, InputType.TYPE_CLASS_TEXT);
            leftDistance = array.getDimension(R.styleable.FloatLabelViewStyle_leftDistance, 20);
            bottomDistance = array.getDimension(R.styleable.FloatLabelViewStyle_bottomDistance, 10);
            imeOptions = array.getInt(R.styleable.FloatLabelViewStyle_android_imeOptions, 0);
            labelTextSize = array.getDimension(R.styleable.FloatLabelViewStyle_labelTextSize, 15);
            editTextSize = array.getDimension(R.styleable.FloatLabelViewStyle_editTextSize, 15);


            //下一个焦点视图
            nextFocusDownId = array.getResourceId(R.styleable.FloatLabelViewStyle_android_nextFocusDown, NO_ID);
            nextFocusForwardId = array.getResourceId(R.styleable.FloatLabelViewStyle_android_nextFocusForward, NO_ID);
            nextFocusLeftId = array.getResourceId(R.styleable.FloatLabelViewStyle_android_nextFocusLeft, NO_ID);
            nextFocusRightId = array.getResourceId(R.styleable.FloatLabelViewStyle_android_nextFocusRight, NO_ID);
            nextFocusUpId = array.getResourceId(R.styleable.FloatLabelViewStyle_android_nextFocusUp, NO_ID);

            array.recycle();
        }

        inflate(context, layout, this);

        mEditText = (EditText) findViewById(editTextId);
        mEditText.setTextSize(editTextSize);
        if (mEditText == null) {
            mEditText = (EditText) findViewById(R.id.edit_text);
        }
        if (mEditText == null) {
            throw new RuntimeException(
                    "你必须添加EditText并且@id/edit_text");
        }
        if (editTextId != R.id.edit_text) {
            mEditText.setId(editTextId);
        }
        mEditText.setHint(hint);
        mEditText.setText(text);
        if (hintColor != null) {
            mEditText.setHintTextColor(hintColor);
        }
        if (imeOptions != 0) {
            mEditText.setImeOptions(imeOptions);
        }
        if (inputType != 0) {
            mEditText.setInputType(inputType);
        }

        //设定各方位的焦点
        mEditText.setNextFocusDownId(nextFocusDownId);
        mEditText.setNextFocusForwardId(nextFocusForwardId);
        mEditText.setNextFocusLeftId(nextFocusLeftId);
        mEditText.setNextFocusRightId(nextFocusRightId);
        mEditText.setNextFocusUpId(nextFocusUpId);

        //获取FloatLabel
        mLabel = (TextView) findViewById(floatLabelId);
        mLabel.setTextSize(labelTextSize);
        if (mLabel == null) {
            mLabel = (TextView) findViewById(R.id.float_label);

        }
        if (mLabel == null) {
            throw new RuntimeException(
                    "你必须添加TextView并且@id/float_label");
        }
        if (floatLabelId != R.id.float_label) {
            mLabel.setId(floatLabelId);
        }
        //建立提示样式
        mLabel.setText(mEditText.getHint());


        if (floatLabelColor != 0) {
            mLabel.setTextColor(floatLabelColor);
        }

        mEditText.addTextChangedListener(new EditTextWatcher());
        Log.i("anumbrella", "mLable :" + mEditText.getText().length());

        // 检查当前EditText的状态
        if (mEditText.getText().length() == 0) {
            mLabel.setAlpha(0);
            mLabelShowing = false;
        } else {
            mLabel.setVisibility(View.VISIBLE);
            mLabelShowing = true;
        }

        //视图的初始化完成
        mInitComplete = true;
    }


    /**
     * Label显示or消失的默认动画
     */
    private static class DefaultLabelAnimator implements LabelAnimator {

        @Override
        public void onDisplayLabel(View label) {

            final float offset = label.getHeight() / 2;
            final float currentY = label.getY();
            if (currentY != offset) {
                label.setY(offset);
            }
            label.animate().alpha(1).y(0);

        }

        @Override
        public void onHideLabel(View label) {
            final float offset = label.getHeight() / 2;
            final float currentY = label.getY();
            if (currentY != 0) {
                label.setY(0);
            }
            label.animate().alpha(0).y(offset);
        }

    }


    /**
     * TextWatcher检测当前EditText是否有输入，当前的输入状态是否改变
     */
    private class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("anumbrella", "mLablesssss :" + s.length());
            //是否忽略显示动画
            if (mSkipAnimation) {
                mSkipAnimation = false;
                //没有文本输入
                if (s.length() == 0) {
                    if (mLabelShowing) {
                        mLabel.setAlpha(0);
                        mLabelShowing = false;
                    }
                    //有文本输入时
                } else if (!mLabelShowing) {
                    mLabel.setAlpha(1);
                    mLabel.setY(0);
                    mLabelShowing = true;
                }
                return;
            }

            if (s.length() == 0) {
                //没有文本输入时
                if (mLabelShowing) {
                    mLabelAnimator.onHideLabel(mLabel);
                    mLabelShowing = false;
                }
            } else if (!mLabelShowing) {
                //有文本输入时
                mLabelShowing = true;
                mLabelAnimator.onDisplayLabel(mLabel);
            }

        }
    }
}




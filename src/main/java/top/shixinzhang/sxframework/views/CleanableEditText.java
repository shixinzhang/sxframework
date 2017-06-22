package top.shixinzhang.sxframework.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


import top.shixinzhang.sxframework.R;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/12/29
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class CleanableEditText extends EditText implements TextWatcher {

    private Drawable mClearDrawable;

    /**
     * @param context context
     */
    public CleanableEditText(Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context context
     * @param attrs   attrs
     */
    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        final int PADDING_RIGHT = (int) ViewHelper.dp2Px(context, 6);
        final int PADDING_TOP = (int) ViewHelper.dp2Px(context, 4);

        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.mipmap.icon_clean_text);
        }
        mClearDrawable.setBounds(-PADDING_RIGHT, PADDING_TOP
                , mClearDrawable.getIntrinsicWidth() - PADDING_RIGHT
                , mClearDrawable.getIntrinsicHeight() + PADDING_TOP);
        setClearIconVisible(false);
        addTextChangedListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean isClearButtonClicked = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (isClearButtonClicked) {
                    setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}

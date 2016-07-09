package com.example.rajesh.udacitycapstoneproject;


import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Contains left icon, title, and right icon
 */
public class CustomToolbar extends LinearLayout {

    private static final String TAG = CustomToolbar.class.getSimpleName();
    View view;

    @Bind(R.id.img_btn_left)
    ImageButton imgButtonLeft;

    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Bind(R.id.tv_right)
    TextView tvRight;


    ToolbarLeftButtonClickListener toolbarLeftButtonClickListener = null;
    ToolbarRightButtonClickListener toolbarRightIconClickListener = null;


    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attributeSet, int defStyleAttr) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_toolbar_layout, this, true);
        ButterKnife.bind(this, view);
    }

    public void setLeftButtonClickListener(ToolbarLeftButtonClickListener toolbarLeftButtonClickListener) {
        this.toolbarLeftButtonClickListener = toolbarLeftButtonClickListener;
    }

    public void setRightButtonClickListener(ToolbarRightButtonClickListener toolbarRightIconClickListener) {
        this.toolbarRightIconClickListener = toolbarRightIconClickListener;
    }


    /**
     * Set toolbar with left drawable ,toolbar title and right drawable
     *
     * @param leftDrawable  left drawable of toolbar
     * @param toolbarTitle  title of toolbar
     * @param rightDrawable right drawable of toolbar
     */
    public void setToolbar(@IntegerRes int leftDrawable, String toolbarTitle, @IntegerRes int rightDrawable) {
        tvRight.setVisibility(GONE);
        imgButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), leftDrawable));
        tvToolbarTitle.setText(toolbarTitle);
        invalidate();
    }

    /**
     * Set toolbar with left drawable, toolbar title and right button text
     *
     * @param leftDrawable left drawable of toolbar
     * @param toolbarTitle title of toolbar
     * @param rightText    text of right button
     */
    public void setToolbar(@IntegerRes int leftDrawable, String toolbarTitle, String rightText) {
        tvRight.setVisibility(VISIBLE);
        imgButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), leftDrawable));
        tvToolbarTitle.setText(toolbarTitle);
        tvRight.setText(rightText);
        invalidate();
    }

    /**
     * Set toolbar with left drawable and toolbar title
     *
     * @param leftDrawable left drawable of toolbar
     * @param toolbarTitle title of toolbar
     */
    public void setToolbar(@IntegerRes int leftDrawable, String toolbarTitle) {
        imgButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), leftDrawable));
        tvToolbarTitle.setText(toolbarTitle);
        tvRight.setVisibility(GONE);
        invalidate();
    }

    /**
     * Set toolbar with back navigation arrow,toolbar title
     *
     * @param toolbarTitle title of toolbar
     */
    public void setToolbar(String toolbarTitle) {
        imgButtonLeft.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back));
        tvToolbarTitle.setText(toolbarTitle);
        tvRight.setVisibility(GONE);
    }

    public interface ToolbarLeftButtonClickListener {
        void onLeftButtonClick();
    }

    public interface ToolbarRightButtonClickListener {
        void onRightButtonClick();
    }

    @OnClick({R.id.img_btn_left,R.id.tv_right})
    public void onClick(View view) {

        try {
            switch (view.getId()) {
                case R.id.img_btn_left:
                    toolbarLeftButtonClickListener.onLeftButtonClick();
                    break;
                case R.id.tv_right:
                    toolbarRightIconClickListener.onRightButtonClick();
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "------ set listener ------");
        }
    }

    /**
     * Disable/enable button with text
     *
     * @param control boolean
     */
    public void controlRightTextButton(boolean control) {
        tvRight.setEnabled(control);
    }

    /**
     * Set dynamic text of right text view.
     *
     * @param action {@link String}
     */
    public void setRightTextButtonText(String action) {
        tvRight.setText(action);
    }
}

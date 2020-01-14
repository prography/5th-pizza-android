package com.prography.prography_pizza.src.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;

public class CustomSimpleMessageDialog extends Dialog implements View.OnClickListener {

    public static final int FINISH_ACTIVITY = 0;
    public static final int FINISH_ACTIVITY_THEN_START = 1;
    public static final int FINISH_NONE = 2;


    private String mMessage;
    private String mBtnText;
    private Context mContext;
    private int mType;
    private Class<?> mNextActivity;

    private TextView tvDesc;
    private TextView tvButton;

    public CustomSimpleMessageDialog(@NonNull Context context, String message, String btnText, int type, Class<?> nextActivity) {
        super(context);
        mBtnText = btnText;
        mMessage = message;
        mContext = context;
        mType = type;
        mNextActivity = nextActivity;
    }

    public CustomSimpleMessageDialog(Builder builder) {
        this(builder.mContext, builder.mMessage, builder.mBtnText, builder.mType, builder.mNextActivity);
    }

    public static class Builder {
        private String mMessage = "";
        private String mBtnText = "";
        private Context mContext = null;
        private int mType = FINISH_NONE;
        private Class<?> mNextActivity = null;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }
        public Builder setButtonText(String btnText) {
            mBtnText = btnText;
            return this;
        }

        public Builder setType(int type) {
            mType = type;
            return this;
        }

        public Builder setNextActivity(Class<?> activity) {
            mNextActivity = activity;
            return this;
        }

        public CustomSimpleMessageDialog build() {
            return new CustomSimpleMessageDialog(this);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_simple_message);

        /* findViewByID */
        tvDesc = findViewById(R.id.tv_desc_dialog_simple_message);
        tvButton = findViewById(R.id.tv_positive_dialog_simple_message);

        /* Set View */
        tvDesc.setText(mMessage);
        tvButton.setText(mBtnText);

        /* Set On Click Listener */
        tvButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive_dialog_simple_message:
                dismiss();
                if (mType == FINISH_ACTIVITY) {
                    ((Activity) mContext).finish();
                } else if (mType == FINISH_ACTIVITY_THEN_START) {
                    ((BaseActivity) mContext).startNextActivity(mNextActivity);
                    ((BaseActivity) mContext).finish();
                }
                break;
        }
    }
}

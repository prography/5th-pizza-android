package com.prography.prography_pizza.src.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prography.prography_pizza.R;

public class CustomPosNegDialog extends Dialog implements View.OnClickListener{

    public static final int FINISH_ACTIVITY = 0;
    public static final int FINISH_NONE = 1;

    private Context mContext;
    private String mMessage;
    private int mType;
    private View.OnClickListener posListener = null;

    public CustomPosNegDialog(@NonNull Context context, String message, int type, View.OnClickListener posListener) {
        super(context);
        mContext = context;
        mMessage = message;
        mType = type;
        this.posListener = posListener;
    }

    public CustomPosNegDialog(Builder builder) {
        this(builder.mContext, builder.mMessage, builder.mType, builder.posListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_posneg);

        /* Set Text */
        ((TextView) findViewById(R.id.tv_desc_dialog_posneg)).setText(mMessage);

        /* Set OnClick Listener */
        if (posListener == null)
            findViewById(R.id.tv_positive_dialog_posneg).setOnClickListener(this);
        else
            findViewById(R.id.tv_positive_dialog_posneg).setOnClickListener(posListener);
        findViewById(R.id.tv_negative_dialog_posneg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive_dialog_posneg:
                dismiss();
                if (mType == FINISH_ACTIVITY) {
                    ((Activity) mContext).finish();
                }
                break;
            case R.id.tv_negative_dialog_posneg:
                dismiss();
                break;
        }
    }

    public static class Builder {
        private Context mContext;
        private String mMessage;
        private int mType;
        private View.OnClickListener posListener = null;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }
        public Builder setType(int type) {
            this.mType = type;
            return this;
        }

        public Builder setPosListener(View.OnClickListener listener) {
            this.posListener = listener;
            return this;
        }

        public CustomPosNegDialog build() {
            return new CustomPosNegDialog(this);
        }
    }
}

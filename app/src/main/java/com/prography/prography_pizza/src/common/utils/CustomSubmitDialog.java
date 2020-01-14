package com.prography.prography_pizza.src.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.record.RecordActivity;

import java.util.ArrayList;

public class CustomSubmitDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView tvPositive;
    private TextView tvNegative;
    private TextView tvDesc;
    private TextView tvTitle;
    private ArrayList<ImageView> ivStars = new ArrayList<>();
    private int countStar = 0;
    private float percentage = 0.f;
    private String mDesc = "";

    public CustomSubmitDialog(@NonNull Context context, int countStar, float percentage, String desc) {
        super(context);
        mContext = context;
        this.countStar = countStar;
        this.percentage = percentage;
        mDesc = desc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_submit_record);

        /* findViewByID */
        tvPositive = findViewById(R.id.tv_positive_dialog_submit_record);
        tvNegative = findViewById(R.id.tv_negative_dialog_submit_record);
        tvDesc = findViewById(R.id.tv_desc_dialog_submit_record);
        tvTitle = findViewById(R.id.tv_title_dialog_submit_record);
        ivStars.add((ImageView) findViewById(R.id.iv_star1_record));
        ivStars.add((ImageView) findViewById(R.id.iv_star2_record));
        ivStars.add((ImageView) findViewById(R.id.iv_star3_record));

        /* Set View */
        tvDesc.setText(mDesc);
        tvTitle.setText(String.format("%.1f", percentage) + "%");
        for (int i = 0; i < countStar; i++) {
            ivStars.get(i).setImageResource(R.drawable.ic_star);
            ivStars.get(i).setAlpha(0.f);
            ivStars.get(i).setScaleX(1.5f);
            ivStars.get(i).setScaleY(1.5f);
            ivStars.get(i).animate().setDuration(200).setStartDelay(i * 100).setInterpolator(new LinearInterpolator()).scaleX(1).scaleY(1).alpha(1.f).start();
        }

        /* Set On Click Listener */
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive_dialog_submit_record:
                ((RecordActivity) mContext).tryPostImgToFirebase(((RecordActivity) mContext).getmImg());
                dismiss();
                break;
            case R.id.tv_negative_dialog_submit_record:
                dismiss();
                break;
        }
    }
}

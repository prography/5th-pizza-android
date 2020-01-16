package com.prography.prography_pizza.src.settings;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.settings.interfaces.SettingActivityView;

import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SettingActivity extends BaseActivity implements SettingActivityView {


    private ImageView ivProfile;
    private ImageView ivChangeProfile;
    private TextView tvUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* findViewByID */
        ivProfile = findViewById(R.id.iv_profile_settings);
        ivChangeProfile = findViewById(R.id.iv_change_profile_settings);
        tvUserName = findViewById(R.id.tv_username_settings);

        /* Set View */
        tvUserName.setText(sSharedPreferences.getString(USER_NAME, "USERNAME"));
        ivProfile.setClipToOutline(true);
        Glide.with(this)
                .load(sSharedPreferences.getString(USER_PROFILE, ""))
                .placeholder(R.drawable.kakao_default_profile_image)
                .error(R.drawable.kakao_default_profile_image)
                .into(ivProfile);

        /* Set On Click Listener */
        ivChangeProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_profile_settings:
                showSimpleMessageDialog(getString(R.string.not_implemented));
                break;
        }
    }
}

package com.prography.prography_pizza.src.mypage;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.mypage.interfaces.MyPageActivityView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static com.prography.prography_pizza.src.ApplicationClass.KAKAO_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class MyPageActivity extends BaseActivity implements MyPageActivityView {

    private Toolbar tbMyPage;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        /* findViewByID */
        tbMyPage = findViewById(R.id.toolbar_mypage);
        ivProfile = findViewById(R.id.iv_profile_myPage);

        /* Toolbar */
        setSupportActionBar(tbMyPage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        tbMyPage.setLogo(R.drawable.ic_close);

        /* Init View */
        ivProfile.setBackground(new ShapeDrawable(new OvalShape()));
        ivProfile.setClipToOutline(true);
        Glide.with(this)
                .load(sSharedPreferences.getString(KAKAO_PROFILE, ""))
                .placeholder(R.drawable.kakao_default_profile_image)
                .error(R.drawable.kakao_default_profile_image)
                .centerCrop()
                .into(ivProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mypage, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ti_setting_mypage:
                showToast("setting");
                break;
        }
        return true;
    }
}

package com.prography.prography_pizza.src.mypage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.mypage.adapter.MyPagePager;
import com.prography.prography_pizza.src.mypage.interfaces.MyPageActivityView;
import com.prography.prography_pizza.src.settings.SettingActivity;

import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class MyPageActivity extends BaseActivity implements MyPageActivityView {

    public static Activity sMyPageActivity;

    private Toolbar tbMyPage;
    private TextView tvToolbarTitleCollapsed;
    private AppBarLayout ablMypage;
    private ConstraintLayout clToolbarContainer;
    private TabLayout tlMypage;
    private ViewPager vpMypage;
    private ImageView ivProfile;
    private TextView tvUserName;
    private TextView tvStar;
    private TextView tvSuccess;
    private TextView tvSuccessPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        sMyPageActivity = this;

        /* findViewByID */
        tbMyPage = findViewById(R.id.toolbar_mypage);
        ablMypage = findViewById(R.id.abl_mypage);
        tlMypage = findViewById(R.id.tl_mypage);
        vpMypage = findViewById(R.id.vp_mypage);
        clToolbarContainer = findViewById(R.id.cl_toolbar_container_mypage);
        tvToolbarTitleCollapsed = findViewById(R.id.tv_toolbar_title_collapsed_mypage);
        ivProfile = findViewById(R.id.iv_profile_img_mypage);
        tvUserName = findViewById(R.id.tv_username_mypage);
        tvStar = findViewById(R.id.tv_star_mypage);
        tvSuccess = findViewById(R.id.tv_success_mypage);
        tvSuccessPercentage = findViewById(R.id.tv_percent_mypage);

        /* Toolbar */
        setSupportActionBar(tbMyPage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* AppBar Layout OffSet Change Listener */
        ablMypage.addOnOffsetChangedListener(this);

        /* Tab Layout Select Change Listener */
        tlMypage.addOnTabSelectedListener(this);

        /* ViewPager */
        vpMypage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlMypage));
        vpMypage.setAdapter(new MyPagePager(getSupportFragmentManager()));

        /* Set On Click Listener */
        tbMyPage.setOnClickListener(this);
        ablMypage.setOnClickListener(this);

        /* Init View */
        tvUserName.setText(sSharedPreferences.getString(USER_NAME, "USERName") + "님");
        tvToolbarTitleCollapsed.setText("마이페이지");
        Glide.with(this)
                .load(sSharedPreferences.getString(USER_PROFILE, null))
                .centerCrop()
                .error(R.drawable.kakao_default_profile_image)
                .placeholder(R.drawable.kakao_default_profile_image)
                .into(ivProfile);
        ivProfile.setClipToOutline(true);
        // tvSuccess
        // tvSuccessPercentage
        // tvStar

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mypage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.ti_setting_mypage:
                startNextActivity(SettingActivity.class);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_mypage:
            case R.id.abl_mypage:
                ablMypage.setExpanded(true, true);
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        /* Calculating Alpha... */
        float collapsedTitleAlpha = 1.0f - Math.abs((i + appBarLayout.getTotalScrollRange()) * 2.5f / (float) appBarLayout.getTotalScrollRange());
        if (collapsedTitleAlpha <= 0.0f)
            collapsedTitleAlpha = 0.0f;
        else if (collapsedTitleAlpha >= 1.0f)
            collapsedTitleAlpha = 1.0f;
        float expandedTitleAlpha = Math.abs((i + appBarLayout.getTotalScrollRange()) * 2.5f / (float) appBarLayout.getTotalScrollRange()) - 1.0f;
        if (expandedTitleAlpha <= 0.0f)
            expandedTitleAlpha = 0.0f;
        else if (expandedTitleAlpha >= 1.0f)
            expandedTitleAlpha = 1.0f;

        /* Set View */
        clToolbarContainer.setAlpha(expandedTitleAlpha);
        tvToolbarTitleCollapsed.setAlpha(collapsedTitleAlpha);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpMypage.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

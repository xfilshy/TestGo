package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.R;
import com.xue.support.view.CircleImageView;
import com.xue.support.view.FloatingActionButton;
import com.xue.support.view.FloatingActionsMenu;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        context.startActivity(intent);
    }

    private AppBarLayout mAppBarLayout;

    private CircleImageView mActionLogoImageView;

    private TextView mActionTitleTextView;

    private LinearLayout mInfoLayout;

    private FloatingActionsMenu mFloatingActionsMenu;

    private boolean mActionBarVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findView();
        initActionBar();
        initAppBarLayout();
        initFloatingActionsMenu();
    }

    private void findView() {
        mInfoLayout = findViewById(R.id.infoLayout);
        mFloatingActionsMenu = findViewById(R.id.floatingMenu);
    }

    private void initAppBarLayout() {
        mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mActionLogoImageView = toolbar.findViewById(R.id.logo);
        mActionTitleTextView = toolbar.findViewById(R.id.title);
        setSupportActionBar(toolbar);
    }

    private void initFloatingActionsMenu() {
        FloatingActionButton actionButton = new FloatingActionButton(this);
        actionButton.setTitle("关心");
        actionButton.setOnClickListener(this);
        actionButton.setColorNormalResId(R.color.red_a200);
        actionButton.setColorPressedResId(R.color.red_a400);
        actionButton.setIcon(R.drawable.icon_favorite_border);
        mFloatingActionsMenu.addButton(actionButton);

        actionButton = new FloatingActionButton(this);
        actionButton.setTitle("消息");
        actionButton.setOnClickListener(this);
        actionButton.setColorNormalResId(R.color.blue_a200);
        actionButton.setColorPressedResId(R.color.blue_a400);
        actionButton.setIcon(R.drawable.icon_chat);
        mFloatingActionsMenu.addButton(actionButton);

        actionButton = new FloatingActionButton(this);
        actionButton.setTitle("电话");
        actionButton.setOnClickListener(this);
        actionButton.setColorNormalResId(R.color.green_a200);
        actionButton.setColorPressedResId(R.color.green_a400);
        actionButton.setIcon(R.drawable.icon_call);
        mFloatingActionsMenu.addButton(actionButton);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxRange = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxRange;
        handleActionBar(percentage);
        handlerInfo(percentage);
        if (verticalOffset == 0) {
            //TODO 预留
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            //TODO 预留
        } else {
            //TODO 预留
        }
    }

    private void handlerInfo(float percentage) {
        float p = 1 - (1.3f * percentage);
        mInfoLayout.setAlpha(p < 0 ? 0 : p);
    }

    private void handleActionBar(float percentage) {
        if (percentage > 0.7) {
            if (!mActionBarVisible) {
                startAlphaAnimation(mActionLogoImageView, 500, VISIBLE);
                startAlphaAnimation(mActionTitleTextView, 500, VISIBLE);
                mActionBarVisible = true;
            }
        } else {
            if (mActionBarVisible) {
                startAlphaAnimation(mActionLogoImageView, 500, INVISIBLE);
                startAlphaAnimation(mActionTitleTextView, 500, INVISIBLE);
                mActionBarVisible = false;
            }
        }
    }

    private void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onClick(View v) {

    }
}

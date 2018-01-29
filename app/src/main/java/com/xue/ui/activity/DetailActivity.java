package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.DetailListAdapter;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.support.view.DividerItemDecoration;
import com.xue.support.view.FloatingActionButton;
import com.xue.ui.views.ChatImageBehavior;
import com.xue.ui.views.FavoriteImageBehavior;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    public static void launch(Context context, String uid) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    private String mUid;

    private AppBarLayout mAppBarLayout;

    private ImageView mActionLogoImageView;

    private TextView mActionTitleTextView;

    private ImageView mActionFavoriteImageView;

    private ImageView mActionChatImageView;

    private LinearLayout mInfoLayout;

    private TextView mVipTextView;

    private ImageView mFavoriteImageView;

    private ImageView mChatImageView;

    private FloatingActionButton mCallFloatingButton;

    private RecyclerView mRecyclerView;

    private DetailListAdapter mAdapter;

    private boolean mActionBarVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findView();

        initRecyclerView();

        readExtra();
    }

    private void findView() {
        initActionBar();
        initAppBarLayout();

        mInfoLayout = findViewById(R.id.infoLayout);
        mVipTextView = findViewById(R.id.vip);
        mFavoriteImageView = findViewById(R.id.favorite);
        mChatImageView = findViewById(R.id.chat);

        mRecyclerView = findViewById(R.id.recyclerView);

        mCallFloatingButton = findViewById(R.id.call);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFavoriteImageView.getLayoutParams();
        layoutParams.setBehavior(new FavoriteImageBehavior(this, mActionFavoriteImageView, null));

        layoutParams = (CoordinatorLayout.LayoutParams) mChatImageView.getLayoutParams();
        layoutParams.setBehavior(new ChatImageBehavior(this, mActionChatImageView, null));

        mCallFloatingButton.setOnClickListener(this);
    }

    private void initAppBarLayout() {
        mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mActionLogoImageView = toolbar.findViewById(R.id.logo);
        mActionTitleTextView = toolbar.findViewById(R.id.title);
        mActionFavoriteImageView = toolbar.findViewById(R.id.actionFavorite);
        mActionChatImageView = toolbar.findViewById(R.id.actionChat);
        setSupportActionBar(toolbar);

        ImageCacheMannager.loadImage(this, R.drawable.photo_test, mActionLogoImageView, true);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));
        if (mAdapter == null) {
            mAdapter = new DetailListAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void readExtra() {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
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

    public void goChat(View view) {
        ChatActivity.launch(this, mUid);
    }

    @Override
    public void onClick(View v) {
        if (v == mCallFloatingButton) {
            AVChatActivity.launchVideoCall(this, mUid);
        }

    }
}

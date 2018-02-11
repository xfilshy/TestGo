package com.xue.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xue.R;
import com.xue.adapter.AdapterOnItemClickCallback;
import com.xue.adapter.DetailListAdapter;
import com.xue.asyns.HttpAsyncTask;
import com.xue.bean.DetailHelper;
import com.xue.bean.FollowResult;
import com.xue.bean.MomentInfoList;
import com.xue.bean.OrderCommentInfo;
import com.xue.bean.OrderCommentList;
import com.xue.bean.User;
import com.xue.bean.UserDetailInfo;
import com.xue.bean.UserExpertInfo;
import com.xue.bean.UserFriendInfo;
import com.xue.http.HttpApi;
import com.xue.http.impl.DataHull;
import com.xue.imagecache.ImageCacheMannager;
import com.xue.support.view.DividerItemDecoration;
import com.xue.support.view.FloatingActionButton;
import com.xue.ui.views.ChatImageBehavior;
import com.xue.ui.views.FavoriteImageBehavior;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener, AdapterOnItemClickCallback<DetailHelper.ItemType> {

    public static void launch(Context context, String uid) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    private String mUid;

    private DetailHelper mDetailHelper = new DetailHelper();

    private User mUser;

    private AppBarLayout mAppBarLayout;

    private ImageView mActionLogoImageView;

    private TextView mActionTitleTextView;

    private ImageView mActionFollowImageView;

    private ImageView mActionChatImageView;

    private LinearLayout mInfoLayout;

    private TextView mVipTextView;

    private ImageView mFollowImageView;

    private ImageView mChatImageView;

    private ImageView mCoverImageView;

    private TextView mRealNameTextView;

    private TextView mSignatureTextView;

    private TextView mFeeTextView;

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

        readExtra();
        findView();
        initRecyclerView();

        new DetailTask(this, mUid).start();

        //TODO 测试代码
//        new CreateComment(this).start();
    }

    private void findView() {
        initActionBar();
        initAppBarLayout();

        mInfoLayout = findViewById(R.id.infoLayout);
        mVipTextView = findViewById(R.id.vip);
        mFollowImageView = findViewById(R.id.favorite);
        mChatImageView = findViewById(R.id.chat);

        mCoverImageView = findViewById(R.id.cover);
        mRealNameTextView = findViewById(R.id.realName);
        mSignatureTextView = findViewById(R.id.signature);
        mFeeTextView = findViewById(R.id.fee);

        mRecyclerView = findViewById(R.id.recyclerView);

        mCallFloatingButton = findViewById(R.id.call);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFollowImageView.getLayoutParams();
        layoutParams.setBehavior(new FavoriteImageBehavior(this, mActionFollowImageView, null));

        layoutParams = (CoordinatorLayout.LayoutParams) mChatImageView.getLayoutParams();
        layoutParams.setBehavior(new ChatImageBehavior(this, mActionChatImageView, null));

        mFollowImageView.setOnClickListener(this);
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
        mActionFollowImageView = toolbar.findViewById(R.id.actionFavorite);
        mActionChatImageView = toolbar.findViewById(R.id.actionChat);
        setSupportActionBar(toolbar);

        mActionFollowImageView.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 1, R.color.grey_light));

    }

    private void fillData() {
        UserDetailInfo userDetailInfo = mUser.getUserDetailInfo();
        if (userDetailInfo != null) {
            ImageCacheMannager.loadImage(this, userDetailInfo.getCover(), mCoverImageView, false);
            ImageCacheMannager.loadImage(this, userDetailInfo.getProfile(), mActionLogoImageView, true);
            mActionTitleTextView.setText(userDetailInfo.getRealName());
            mRealNameTextView.setText(userDetailInfo.getRealName());
        }

        UserFriendInfo userFriendInfo = mUser.getUserFriendInfo();
        if (userFriendInfo != null) {
            mFollowImageView.setSelected(userFriendInfo.isFollow());
            mActionFollowImageView.setSelected(userFriendInfo.isFollow());
        }

        UserExpertInfo userExpertInfo = mUser.getUserExpertInfo();
        if (userExpertInfo != null) {
            mSignatureTextView.setText(userExpertInfo.getSignature());
            mFeeTextView.setText(userExpertInfo.getServiceFee() + "钻石/每分钟");
        }

        if (mAdapter == null) {
            mAdapter = new DetailListAdapter();
            mAdapter.setCallback(this);
            mAdapter.setData(mDetailHelper);
            mRecyclerView.setAdapter(mAdapter);
        }

        mAdapter.notifyDataSetChanged();
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
        } else if (v == mActionFollowImageView || v == mFollowImageView) {
            boolean isFollow = mUser.getUserFriendInfo().isFollow();
            if (isFollow) {
                new DeleteFollow(this, mUid).start();
            } else {
                new CreateFollow(this, mUid).start();
            }
        }

    }

    @Override
    public void onItemClick(DetailHelper.ItemType itemType, View view) {
        if (itemType == DetailHelper.ItemType.Gallery) {
            OtherGalleryActivity.launch(this, mDetailHelper.getOtherGallery());
        } else if (itemType == DetailHelper.ItemType.CommentTitle) {
            CommentListActivity.launch(this, mUid);
        }
    }

    private class DetailTask extends HttpAsyncTask<User> {

        private String uid;

        public DetailTask(Context context, String uid) {
            super(context);
            this.uid = uid;
        }

        @Override
        public DataHull<User> doInBackground() {
            return HttpApi.getDetailInfo(uid);
        }

        @Override
        public void onPostExecute(int updateId, User result) {
            mUser = result;
            mDetailHelper.setUser(mUser);
            fillData();

            new GetLastMomentInfoTask(context, uid).start();
            new GetLastCommentTask(context, uid).start();
        }
    }

    private class GetLastMomentInfoTask extends HttpAsyncTask<MomentInfoList.MomentInfo> {

        private String uid;

        public GetLastMomentInfoTask(Context context, String uid) {
            super(context);
            this.uid = uid;
        }

        @Override
        public DataHull<MomentInfoList.MomentInfo> doInBackground() {
            return HttpApi.getLastMomentInfo(uid);
        }

        @Override
        public void onPostExecute(int updateId, MomentInfoList.MomentInfo result) {
            mDetailHelper.setMomentInfo(result);
            fillData();
        }
    }

    private class CreateFollow extends HttpAsyncTask<FollowResult> {

        private String uid;

        public CreateFollow(Context context, String uid) {
            super(context);
            this.uid = uid;
        }

        @Override
        public DataHull<FollowResult> doInBackground() {
            return HttpApi.createFollow(uid);
        }

        @Override
        public void onPostExecute(int updateId, FollowResult result) {
            mUser.getUserFriendInfo().setFollow(true);
            mUser.getUserFriendInfo().setFansCount(result.getFansCount());

            fillData();
        }
    }

    private class DeleteFollow extends HttpAsyncTask<FollowResult> {

        private String uid;

        public DeleteFollow(Context context, String uid) {
            super(context);
            this.uid = uid;
        }

        @Override
        public DataHull<FollowResult> doInBackground() {
            return HttpApi.deleteFollow(uid);
        }

        @Override
        public void onPostExecute(int updateId, FollowResult result) {
            mUser.getUserFriendInfo().setFollow(false);
            mUser.getUserFriendInfo().setFansCount(result.getFansCount());

            fillData();
        }
    }

    private class GetLastCommentTask extends HttpAsyncTask<OrderCommentInfo> {

        private String uid;

        public GetLastCommentTask(Context context, String uid) {
            super(context);
            this.uid = uid;
        }

        @Override
        public DataHull<OrderCommentInfo> doInBackground() {
            return HttpApi.getLastOrderCommentInfo(uid);
        }

        @Override
        public void onPostExecute(int updateId, OrderCommentInfo result) {
            mDetailHelper.setOrderCommentInfo(result);
            fillData();
        }
    }

    private class CreateComment extends HttpAsyncTask<OrderCommentList.Comment> {

        public CreateComment(Context context) {
            super(context);
        }

        @Override
        public DataHull<OrderCommentList.Comment> doInBackground() {
            return HttpApi.createOrderComment(mUid, "0", "5", "我就是看看，有没有用");
        }

        @Override
        public void onPostExecute(int updateId, OrderCommentList.Comment result) {

        }
    }
}

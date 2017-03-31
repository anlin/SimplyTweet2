package com.thunder.simplytweet.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.fragments.UserTimelineFragment;
import com.thunder.simplytweet.models.User;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profile_user_image)
    ImageView userProfileImage;
    @BindView(R.id.profile_user_name)
    TextView userName;
    @BindView(R.id.profile_user_tagline)
    TextView userTagline;
    @BindView(R.id.profile_follower_count)
    TextView userFollowerCount;
    @BindView(R.id.profile_following_count)
    TextView userFollowingCount;

    ProgressDialog progressDialog;

    TweetClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupProgressDialog();
        // get Screen Name
        String screenName = getIntent().getStringExtra("screen_name");
        client = TweetApplication.getRestClient();
        if(!progressDialog.isShowing())
            progressDialog.show();
        if(!TextUtils.isEmpty(screenName)){
            getOtherUserInfo(screenName);
        }
        else{
            getOwnInfo();
        }

        if(savedInstanceState == null) {
            // Create User Timeline
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(screenName);
            //Display user fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.profile_user_timeline, fragment);
            fragmentTransaction.commit();
        }
    }

    private void getOtherUserInfo(String screenName) {
        client.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject jsonObject){
                user = User.fromJson(jsonObject);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                // populate header
                populateUserHeaderData(user);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getOwnInfo() {
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject jsonObject){
                user = User.fromJson(jsonObject);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                // populate header
                populateUserHeaderData(user);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
            }
        });
    }

    private void populateUserHeaderData(User user) {
        Glide.with(this).load(user.getProfileImageUrl()).into(userProfileImage);
        userName.setText(user.getName());
        userTagline.setText(user.getTagline());
        userFollowerCount.setText(user.getFollowerCount()+ " Followers");
        userFollowingCount.setText(user.getFollowingCount() + " Followings");
    }

    void setupProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Retrieving tweets...");
        progressDialog.setCancelable(false);
    }
}

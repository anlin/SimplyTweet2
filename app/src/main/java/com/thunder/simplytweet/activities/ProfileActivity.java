package com.thunder.simplytweet.activities;

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

    TweetClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        client = TweetApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONObject jsonObject){
                user = User.fromJson(jsonObject);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                // populate header
                populateUserHeaderData(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        // get Screen Name
        String screenName = getIntent().getStringExtra("screen_name");
        if(savedInstanceState == null) {
            // Create User Timeline
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(screenName);
            //Display user fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.profile_user_timeline, fragment);
            fragmentTransaction.commit();
        }
    }

    private void populateUserHeaderData(User user) {
        Glide.with(this).load(user.getProfileImageUrl()).into(userProfileImage);
        userName.setText(user.getName());
        userTagline.setText(user.getTagline());
        userFollowerCount.setText(String.valueOf(user.getFollowerCount()));
        userFollowingCount.setText(String.valueOf(user.getFollowingCount()));
    }
}

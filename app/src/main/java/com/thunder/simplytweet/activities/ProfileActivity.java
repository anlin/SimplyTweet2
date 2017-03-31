package com.thunder.simplytweet.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.databinding.ActivityProfileBinding;
import com.thunder.simplytweet.fragments.UserTimelineFragment;
import com.thunder.simplytweet.models.User;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class ProfileActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    TweetClient client;
    User user;
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
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
        binding.setUser(user);
        ImageView userProfileImage = (ImageView) findViewById(R.id.profile_user_image);
        Glide.with(this).load(user.getProfileImageUrl()).into(userProfileImage);
    }

    void setupProgressDialog(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Retrieving tweets...");
        progressDialog.setCancelable(false);
    }
}

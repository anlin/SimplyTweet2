package com.thunder.simplytweet.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anlinsquall on 31/3/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

    ProgressDialog progressDialog;

    public static UserTimelineFragment newInstance(String screenName){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    protected void loadMoreTweets(int page) {
        setupProgressDialog();
        String screenName = getArguments().getString("screen_name");
        TweetClient tweetClient = TweetApplication.getRestClient();
        progressDialog.show();
        tweetClient.getUserTimeline(screenName, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                addAll(Tweet.fromJson(jsonArray));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
            }
        });
    }

    void setupProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Retrieving tweets...");
        progressDialog.setCancelable(false);
    }
}

package com.thunder.simplytweet.fragments;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anlinsquall on 31/3/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(String screenName){
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    protected void loadMoreTweets(int page) {
        String screenName = getArguments().getString("screen_name");
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.getUserTimeline(screenName, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                addAll(Tweet.fromJson(jsonArray));
            }
        });
    }
}

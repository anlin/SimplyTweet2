package com.thunder.simplytweet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by anlinsquall on 30/3/17.
 */

public class MentionTweetsList extends TweetsListFragment {

    @Override
    protected void loadMoreTweets(int page) {
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.getMentionTimeline(page, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                addAll(Tweet.fromJson(jsonArray));
            }
        });
    }
}

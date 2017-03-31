package com.thunder.simplytweet.fragments;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anlinsquall on 30/3/17.
 */

public class HomeTweetsListFragment extends TweetsListFragment {
    @Override
    protected void loadMoreTweets(int page) {
        {
            TweetClient tweetClient = TweetApplication.getRestClient();
            tweetClient.getHomeTimeline(page, new JsonHttpResponseHandler(){
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                    addAll(Tweet.fromJson(jsonArray));
                }
            });
        }
    }
}

package com.thunder.simplytweet.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.thunder.simplytweet.models.Tweet_Table.screenName;

/**
 * Created by anlinsquall on 3/4/17.
 */

public class SearchTweetFragment extends TweetsListFragment {

    ProgressDialog progressDialog;

    public static SearchTweetFragment newInstance(String keyword){
        SearchTweetFragment searchTweetFragment = new SearchTweetFragment();
        Bundle args = new Bundle();
        args.putString("q", keyword);
        searchTweetFragment.setArguments(args);
        return searchTweetFragment;
    }

    @Override
    protected void loadMoreTweets(int page) {
        setupProgressDialog();
        String keyword = getArguments().getString("q");
        TweetClient tweetClient = TweetApplication.getRestClient();
        progressDialog.show();
        tweetClient.searchTweet(keyword, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject){
                try {
                    addAll(Tweet.fromJson(jsonObject.getJSONArray("statuses")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

package com.thunder.simplytweet.fragments;

import android.app.ProgressDialog;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by anlinsquall on 30/3/17.
 */

public class HomeTweetsListFragment extends TweetsListFragment {

    ProgressDialog progressDialog;

    @Override
    protected void loadMoreTweets(int page) {
        {
            setupProgressDialog();
            TweetClient tweetClient = TweetApplication.getRestClient();
            progressDialog.show();
            tweetClient.getHomeTimeline(page, new JsonHttpResponseHandler(){
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
    }

    void setupProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Retrieving tweets...");
        progressDialog.setCancelable(false);
    }
}

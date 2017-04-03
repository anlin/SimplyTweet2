package com.thunder.simplytweet.restclient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import static com.thunder.simplytweet.models.Tweet_Table.screenName;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TweetClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "YOUR_API_KEY";       // Change this
    public static final String REST_CONSUMER_SECRET = "YOUR_API_SECRET"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://thunderSimplyTweet"; // Change this (here and in manifest)

    public TweetClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // get home time line
    public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        client.get(apiUrl, params, handler);
    }

    // get Mention Timeline
    public void getMentionTimeline(int page, AsyncHttpResponseHandler handler ) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        client.get(apiUrl, params, handler);
    }

    // post a tweet
    public void postTweet(String body, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        client.post(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    // Get user info profile
    public void getUserInfo(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }

    // Get other user profile
    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    // Get followers list
    public void getFollowersList(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }

    // Get followings list
    public void getFollowingsList(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }

    // Get followings list
    public void searchTweet(String keyword, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        params.put("q", keyword);
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }

    // post a tweet for reply
    public void replyTweet(String body, String replyId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("in_reply_to_status_id", replyId);
        params.put("status", body);
        client.post(apiUrl, params, handler);
    }

    // post a favourite
    public void favouriteTweet(long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    // post a unfavourite
    public void unfavouriteTweet(long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    // post a retweet
    public void postRetweet(long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/retweet/" +id +".json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    // post a unretweet
    public void postUnretweet(long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/unretweet/" +id +".json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }
}

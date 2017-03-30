package com.thunder.simplytweet.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.adapters.EndlessRecyclerViewScrollListener;
import com.thunder.simplytweet.adapters.TweetAdapters;
import com.thunder.simplytweet.fragments.ComposeDialogFragment;
import com.thunder.simplytweet.fragments.TweetsListFragment;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;
import com.thunder.simplytweet.utils.ItemClickSupport;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.thunder.simplytweet.R.id.swipeContainer;

public class TimelineActivity extends AppCompatActivity implements
        ComposeDialogFragment.ComposeDialogListener, TweetsListFragment.LoadTweetsListener {

    TweetsListFragment tweetsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        if(savedInstanceState == null){
            tweetsListFragment = (TweetsListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_timeline);
        }
    }

    private void showComposeDialog() {
        FragmentManager manager = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance("Simply Tweet");
        composeDialogFragment.show(manager, "fragment_compose");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                showComposeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinishComposeDialog(final String tweet) {
        if(!isOnline()){
            Toast.makeText(TimelineActivity.this ,
                    "The device is offline now.", Toast.LENGTH_LONG).show();
            return;
        }
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.postTweet(tweet,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Tweet postedTweet = new Tweet(jsonObject);
//                tweets.add(0, postedTweet);
//                adapter.notifyItemInserted(0);
//                tweetsView.scrollToPosition(0);
            }
        });
    }

    // Check if the device can go online
    private boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onComposeClick(View view) {
        showComposeDialog();
    }



    private void loadMoreTweets(int page){
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.getHomeTimeline(page, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray){
                tweetsListFragment.addAll(Tweet.fromJson(jsonArray));
            }
        });
    }

    @Override
    public void onLoadTweetFinish(int page) {
        loadMoreTweets(page);
    }
}

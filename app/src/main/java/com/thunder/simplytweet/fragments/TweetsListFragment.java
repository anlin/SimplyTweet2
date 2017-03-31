package com.thunder.simplytweet.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.activities.TweetDetailsActivity;
import com.thunder.simplytweet.adapters.EndlessRecyclerViewScrollListener;
import com.thunder.simplytweet.adapters.TweetAdapters;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.utils.ItemClickSupport;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anlinsquall on 30/3/17.
 */

public abstract class TweetsListFragment extends Fragment {

    @BindView(R.id.tweets_timeline)
    RecyclerView tweetsView;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    ArrayList<Tweet> tweets;
    TweetAdapters adapter;

    private EndlessRecyclerViewScrollListener scrollListener;

    public interface LoadTweetsListener{
        void onLoadTweetFinish(int page);
    }

    //Creation Life Cycle.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetAdapters(getActivity(), tweets);
    }

    //Inflation Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, view);
        setupTweetUI();
        if(!isOnline()){
            loadTweetsFromCache();
        }
        loadMoreTweets(0);
        return view;
    }

    private void setupTweetUI() {
        tweetsView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                tweetsView.getContext(),linearLayoutManager.getOrientation());
        tweetsView.addItemDecoration(dividerItemDecoration);
        tweetsView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreTweets(page);
            }
        };

        ItemClickSupport.addTo(tweetsView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Tweet tweet = tweets.get(position);
                Intent intent = new Intent(getActivity(), TweetDetailsActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweet));
                startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline() || !isNetworkAvailable()){
                    Toast.makeText(getActivity(),
                            "The device is offline now.", Toast.LENGTH_LONG).show();
                    swipeContainer.setRefreshing(false);
                    return;
                }
                adapter.clear();
                loadMoreTweets(0);
            }
        });

        tweetsView.addOnScrollListener(scrollListener);
    }

    public void addAll(List<Tweet> tweets){
        this.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    private void loadTweetsFromCache(){
        List<Tweet> cacheTweets = SQLite.select().from(Tweet.class).queryList();
        tweets.addAll(cacheTweets);
        adapter.notifyDataSetChanged();
    }

    // check if the device is connected to network
    private Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
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

    protected abstract void loadMoreTweets(int page);
}

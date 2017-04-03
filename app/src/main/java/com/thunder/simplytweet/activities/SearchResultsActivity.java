package com.thunder.simplytweet.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thunder.simplytweet.R;
import com.thunder.simplytweet.fragments.SearchTweetFragment;

import static android.R.attr.key;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        String keyword = getIntent().getExtras().getString("keyword");
        SearchTweetFragment searchTweetFragment = SearchTweetFragment.newInstance(keyword);
        setTitle("Search results for " + keyword);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.search_results_placeholder, searchTweetFragment);
        fragmentTransaction.commit();
    }
}

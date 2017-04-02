package com.thunder.simplytweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.adapters.UserListAdapter;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.models.User;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static java.util.Collections.addAll;

public class UserListActivity extends AppCompatActivity {

    @BindView(R.id.user_list)
    RecyclerView userList;

    ArrayList<User> users;
    UserListAdapter userListAdapter;
    String screenName;
    Boolean isFollowersPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenName = getIntent().getExtras().getString("screen_name");
        isFollowersPage = getIntent().getBooleanExtra("is_followers", true);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        users = new ArrayList<User>();
        userListAdapter = new UserListAdapter(users, this);
        SetupUI();
        loadUsers();
    }

    private void SetupUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,linearLayoutManager.getOrientation());
        userList.addItemDecoration(dividerItemDecoration);
        userList.setLayoutManager(linearLayoutManager);
        userList.setAdapter(userListAdapter);
    }

    private void loadUsers() {
        TweetClient tweetClient = TweetApplication.getRestClient();
        if(isFollowersPage) {
            tweetClient.getFollowersList(screenName, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        userListAdapter.addAll(User.fromJson(response.getJSONArray("users")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }else{
            tweetClient.getFollowingsList(screenName, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        userListAdapter.addAll(User.fromJson(response.getJSONArray("users")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    //TODO: Move to users list fragments
    //TODO: UI Improvements
    //TODO: Button change state if the user is already added
}

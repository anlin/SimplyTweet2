package com.thunder.simplytweet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.activities.ProfileActivity;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;
import com.thunder.simplytweet.ui.PatternEditableBuilder;
import com.thunder.simplytweet.utils.Utils;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.thunder.simplytweet.R.id.tweet;
import static com.thunder.simplytweet.R.id.tweet_reply;

/**
 * Created by anlinsquall on 25/3/17.
 */

public class TweetAdapters extends RecyclerView.Adapter<TweetAdapters.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_profile)
        ImageView profile;
        @BindView(R.id.user_username)
        TextView userName;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.body)
        TextView body;
        @BindView(R.id.time)
        TextView timeAgo;
        @BindView(R.id.mediaImage)
        ImageView mediaImage;
        @BindView(R.id.tweet_reply)
        Button tweetReply;
        @BindView(R.id.tweet_retweet)
        Button tweetRetweet;
        @BindView(R.id.tweet_heart)
        Button tweetHeart;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Tweet> tweets;
    private Context context;

    private View.OnClickListener replyOnClickListener;
    private View.OnClickListener retweetOnClickListener;
    private View.OnClickListener heartOnClickListener;

    public TweetAdapters(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }

    private Context getContext(){
        return this.context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tweet tweet = tweets.get(position);

        setupButtonsListeners(holder, tweet);

        //Bind data with view
        holder.name.setText(tweet.getName());
        holder.userName.setText(tweet.getScreenName());
        holder.body.setText(tweet.getBody());
        LinkifyBodyText(holder);
        holder.timeAgo.setText(Utils.getRelativeTimeAgo(tweet.getTimestamp()));
        Glide.with(context).load(tweet.getProfileImageUrl()).into(holder.profile);
        if(!TextUtils.isEmpty(tweet.getMediaImageUrl())){
            Glide.with(context).load(tweet.getMediaImageUrl()).into(holder.mediaImage);
        }
        else{
            holder.mediaImage.setVisibility(View.GONE);
        }

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserProfileClick(tweet.getScreenName());
            }
        });

        holder.tweetHeart.setText(String.valueOf(tweet.getLikesCount()));
        holder.tweetRetweet.setText(String.valueOf(tweet.getRetweetsCount()));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clear all tweets
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add tweets
    public void addAll(List<Tweet> tweets){
        this.tweets.addAll(tweets);
        notifyDataSetChanged();
    }

    private void onUserProfileClick(String screenName){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("screen_name", screenName);
        getContext().startActivity(intent);
    }

    private void LinkifyBodyText(ViewHolder holder) {
        // Linkify body text
        new PatternEditableBuilder()
                .addPattern(Pattern.compile("\\@(\\w+)"),
                        new PatternEditableBuilder.SpannableClickedListener(){
                            @Override
                            public void onSpanClicked(String screenName) {
                                onUserProfileClick(screenName);
                            }
                        })
                .addPattern(Pattern.compile("\\#(\\w+)"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                // TODO Link to search page for hashtags.
                            }
                        })
                .into(holder.body);
    }

    private void setupButtonsListeners(ViewHolder holder, final Tweet tweet) {
        replyOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click Reply", Toast.LENGTH_SHORT).show();
                //TODO Reply Dialog
            }
        };
        retweetOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetClient tweetClient = TweetApplication.getRestClient();
                if(tweet.getRetweeted()){
                    tweetClient.postUnretweet(tweet.getTweetId(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.setRetweeted(false);
                            tweet.save();
                            Log.d("DEBUG", responseBody.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("DEBUG", responseBody.toString());
                        }
                    });
                }else{
                    tweetClient.postRetweet(tweet.getTweetId(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.setRetweeted(true);
                            tweet.save();
                            Log.d("DEBUG", responseBody.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("DEBUG", responseBody.toString());
                        }
                    });
                }
                // TODO Update UI button. So, user will know if it is favorited
            }
        };
        heartOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetClient tweetClient = TweetApplication.getRestClient();
                if(tweet.getFavourited()){
                    tweetClient.unfavouriteTweet(tweet.getTweetId(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.setFavourited(false);
                            tweet.save();
                            Log.d("DEBUG", responseBody.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("DEBUG", responseBody.toString());
                        }
                    });
                }else{
                    tweetClient.favouriteTweet(tweet.getTweetId(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            tweet.setFavourited(true);
                            tweet.save();
                            Log.d("DEBUG", responseBody.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("DEBUG", responseBody.toString());
                        }
                    });
                }
                // TODO Update UI button. So, user will know if it is favorited
            }
        };

        holder.tweetReply.setOnClickListener(replyOnClickListener);
        holder.tweetRetweet.setOnClickListener(retweetOnClickListener);
        holder.tweetHeart.setOnClickListener(heartOnClickListener);
    }
}

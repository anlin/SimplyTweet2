package com.thunder.simplytweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;
import com.thunder.simplytweet.utils.Utils;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.id.edit;
import static com.thunder.simplytweet.R.id.swipeContainer;

public class TweetDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tweet_details_profile_image)
    ImageView profileImage;
    @BindView(R.id.tweet_details_name)
    TextView name;
    @BindView(R.id.tweet_details_screen_name)
    TextView screenName;
    @BindView(R.id.tweet_details_body)
    TextView body;
    @BindView(R.id.tweet_details_media_image)
    ImageView mediaImage;
    @BindView(R.id.tweet_details_retweets_count)
    TextView retweetCount;
    @BindView(R.id.tweet_details_likes_count)
    TextView likesCount;
    @BindView(R.id.tweet_details_timestamp)
    TextView timestamp;
    @BindView(R.id.tweet_details_reply_edittext)
    EditText replyText;
    @BindView(R.id.tweet_details_reply_button)
    Button reply;
    @BindView(R.id.video)
    VideoPlayerView videoPlayerView;

    Tweet tweet;

    private VideoPlayerManager<MetaData> videoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        ButterKnife.bind(this);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        setDataToViews(tweet);
    }

    private void setDataToViews(final Tweet tweet) {
        Glide.with(this).load(tweet.getProfileImageUrl()).into(profileImage);
        name.setText(tweet.getName());
        screenName.setText(tweet.getScreenName());
        body.setText(tweet.getBody());
        retweetCount.setText(String.valueOf(tweet.getRetweetsCount()));
        likesCount.setText(String.valueOf(tweet.getLikesCount()));
        timestamp.setText(Utils.getDateTime(tweet.getTimestamp()));
        replyText.setText(tweet.getScreenName()+" ");

        if(!TextUtils.isEmpty(tweet.getMediaImageUrl())){
            Glide.with(this).load(tweet.getMediaImageUrl()).into(mediaImage);
        }else{
            mediaImage.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(tweet.getExtendedMediaVideoUrl())){
            mediaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoPlayerView.setVisibility(View.VISIBLE);
                    videoPlayerView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener(){
                        @Override
                        public void onVideoPreparedMainThread() {
                            super.onVideoPreparedMainThread();
                        }

                        @Override
                        public void onVideoStoppedMainThread() {
                            super.onVideoStoppedMainThread();
                        }

                        @Override
                        public void onVideoCompletionMainThread() {
                            super.onVideoCompletionMainThread();
                        }
                    });
                    videoPlayerManager.playNewVideo(null, videoPlayerView, tweet.getExtendedMediaVideoUrl());
                }
            });
        }
    }

    @OnClick(R.id.tweet_details_reply_button)
    public void replyTweet(){
        String status = replyText.getText().toString();
        String replyID = String.valueOf(tweet.getTweetId());
        TweetClient tweetClient = TweetApplication.getRestClient();
        tweetClient.replyTweet(status, replyID, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject){
                Toast.makeText(TweetDetailsActivity.this,
                        "Successfully reply", Toast.LENGTH_LONG).show();
                replyText.setText("");
                //TODO Add newly reply in below reply list
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop video when activity is not in foreground
        videoPlayerManager.stopAnyPlayback();
    }
}

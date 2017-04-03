package com.thunder.simplytweet.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.Tweet;
import com.thunder.simplytweet.restclient.TweetApplication;
import com.thunder.simplytweet.restclient.TweetClient;
import com.thunder.simplytweet.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.width;
import static android.support.v7.appcompat.R.attr.height;

/**
 * Created by anlinsquall on 3/4/17.
 */

public class ReplyDialogFragment extends DialogFragment {
    @BindView(R.id.reply_profile_image)
    ImageView profileImage;
    @BindView(R.id.reply_name)
    TextView replyName;
    @BindView(R.id.reply_screen_name)
    TextView replyScreenname;
    @BindView(R.id.reply_time_since)
    TextView replyTimeSince;
    @BindView(R.id.reply_body)
    TextView replyBody;
    @BindView(R.id.reply_to)
    TextView replyTo;
    @BindView(R.id.reply_text)
    EditText replyText;
    @BindView(R.id.reply_text_count)
    TextView replyTextCount;
    @BindView(R.id.reply_button)
    Button reply;

    static Tweet tweet;

    public interface ReplyDialogListener{
        void onFinishReplyDialog(String tweet);
    }

    public ReplyDialogFragment() {
    }

    public static ReplyDialogFragment newInstance(String title, Tweet tweet){
        ReplyDialogFragment.tweet = tweet;
        ReplyDialogFragment fragment = new ReplyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_dialog, container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateData();
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        handleTextChanged(replyText);
    }

    private void populateData() {
        Glide.with(getActivity()).load(tweet.getProfileImageUrl()).into(profileImage);
        replyName.setText(tweet.getName());
        replyScreenname.setText(tweet.getScreenName());
        replyTimeSince.setText(Utils.getRelativeTimeAgo(tweet.getTimestamp()));
        replyBody.setText(tweet.getBody());
        String screenNameText = tweet.getScreenName() + " ";
        replyText.setText(screenNameText);
        replyText.setSelection(replyText.getText().length());
        replyTo.setText("Reply to " + tweet.getScreenName());
    }

    private void handleTextChanged(final EditText composeText) {
        int maxLength = 140;
        composeText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        composeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                replyTextCount.setText("" + (140 - composeText.getText().toString().length()));
            }
        });
    }

    @OnClick(R.id.reply_button)
    public void postReplyTweet(View view){
        TweetClient client = TweetApplication.getRestClient();
        client.replyTweet(replyText.getText().toString(), String.valueOf(tweet.getTweetId()), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getActivity(), "Successfully replied", Toast.LENGTH_LONG);
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Failed replied", Toast.LENGTH_LONG);
            }
        });
    }
}

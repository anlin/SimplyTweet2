package com.thunder.simplytweet.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thunder.simplytweet.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.handle;

/**
 * Created by anlinsquall on 26/3/17.
 */

public class ComposeDialogFragment extends DialogFragment {

    @BindView(R.id.compose_text)
    EditText composeText;
    @BindView(R.id.remaining_length)
    TextView remainingLength;
    @BindView(R.id.tweet)
    Button tweet;
    @BindView(R.id.cancel)
    Button cancel;

    public interface ComposeDialogListener{
        void onFinishComposeDialog(String tweet);
    }

    public ComposeDialogFragment() {
    }

    public static ComposeDialogFragment newInstance(String title){
        ComposeDialogFragment fragment = new ComposeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get fields from Views
        //Fetch title and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        handleTextChanged(composeText);

        //Show soft keyboard automatically and request focus to field
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
                remainingLength.setText("" + (140 - composeText.getText().toString().length()));
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_compose);
        return dialog;
    }

    @OnClick(R.id.cancel)
    public void onCancel(View view){
        dismiss();
    }

    @OnClick(R.id.tweet)
    public void postTweet(View view){
        ComposeDialogListener listener = (ComposeDialogListener) getActivity();
        listener.onFinishComposeDialog(composeText.getText().toString());
        dismiss();
    }
}

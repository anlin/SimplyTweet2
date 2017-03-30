package com.thunder.simplytweet.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.restclient.TweetClient;

import java.io.IOException;

import static android.R.string.yes;

// Where user will sign in
public class LoginActivity extends OAuthLoginActionBarActivity<TweetClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if(!isNetworkAvailable() || !isOnline()){
			loadTweets();
		}

	}

	private void loadTweets() {
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		 Intent i = new Intent(this, TimelineActivity.class);
		 startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

	// check if the device is connected to network
	private Boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

}

package com.thunder.simplytweet.restclient;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;
import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TweetClient client = TweetApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TweetApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TweetApplication.context = this;
	}

	public static TweetClient getRestClient() {
		return (TweetClient) TweetClient.getInstance(TweetClient.class, TweetApplication.context);
	}
}
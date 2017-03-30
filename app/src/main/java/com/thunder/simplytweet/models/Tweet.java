package com.thunder.simplytweet.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.thunder.simplytweet.database.MyDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the DBFlow wiki for more details:
 * https://github.com/codepath/android_guides/wiki/DBFlow-Guide
 *
 * Note: All models **must extend from** `BaseModel` as shown below.
 * 
 */
@Table(database = MyDatabase.class)
@Parcel(analyze = {Tweet.class})
public class Tweet extends BaseModel {

	@PrimaryKey
	@Column
	Long tweetId;
	@Column
	String name;
	@Column
	String screenName;
	@Column
	String timestamp;
	@Column
	String body;
	@Column
	String profileImageUrl;
	@Column
	String mediaImageUrl;
    @Column
    long retweetsCount;
    @Column
    long likesCount;
	@Column
	String extendedMediaVideoUrl;



	public Tweet() {
		super();
	}

	// Parse model from JSON
	public Tweet(JSONObject object){
		super();

		try {
			this.tweetId = object.getLong("id");
            this.name = object.getJSONObject("user").getString("name");
			this.screenName = object.getJSONObject("user").getString("screen_name");
			this.timestamp = object.getString("created_at");
			this.body = object.getString("text");
            this.retweetsCount = object.getLong("retweet_count");
            this.likesCount = object.getLong("favorite_count");
			this.profileImageUrl = object.getJSONObject("user").getString("profile_image_url");
            this.mediaImageUrl = "";
			this.extendedMediaVideoUrl="";
            JSONObject entityObject = object.getJSONObject("entities");
            if(entityObject.has("media")) {
                this.mediaImageUrl = entityObject.getJSONArray("media").getJSONObject(0)
                        .getString("media_url");
            }
			if(object.has("extended_entities")){
				JSONObject extendedEntityObject = object.getJSONObject("extended_entities");
				this.extendedMediaVideoUrl = extendedEntityObject.getJSONArray("media").getJSONObject(0)
						.getJSONObject("video_info").getJSONArray("variants")
						.getJSONObject(0).getString("url");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

    public Long getTweetId() {
        return tweetId;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

    public String getMediaImageUrl() {
        return mediaImageUrl;
    }

    public long getRetweetsCount() {
        return retweetsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

	public String getExtendedMediaVideoUrl() {
		return extendedMediaVideoUrl;
	}

	// Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static ArrayList<Tweet> fromJson (JSONArray jsonArray){
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i ++){
			JSONObject tweetJson = null;

			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = new Tweet(tweetJson);
			tweet.save();
			tweets.add(tweet);
		}
		return tweets;
	}

}

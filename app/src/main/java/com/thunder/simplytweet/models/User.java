package com.thunder.simplytweet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.thunder.simplytweet.R.id.tweet;

/**
 * Created by anlinsquall on 31/3/17.
 */

public class User {

    String screenName;
    String profileImageUrl;
    String name;
    String tagline;
    Long followerCount;
    Long followingCount;

    public User(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public Long getFollowingCount() {
        return followingCount;
    }

    // Parse model from JSON
    public User(JSONObject object){
        super();

        try {
            this.screenName = object.getString("screen_name");
            this.profileImageUrl = object.getString("profile_image_url");
            this.name = object.getString("name");
            this.tagline = object.getString("description");
            this.followerCount = object.getLong("followers_count");
            this.followingCount = object.getLong("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static User fromJson (JSONObject jsonObject){
        User user = new User(jsonObject);
        return user;
    }

    public static ArrayList<User> fromJson (JSONArray jsonArray){
        ArrayList<User> users = new ArrayList<User>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i ++){
            JSONObject userJson = null;

            try {
                userJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            User user = new User(userJson);
            users.add(user);
        }
        return users;
    }
}

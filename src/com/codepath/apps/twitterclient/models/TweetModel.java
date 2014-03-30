package com.codepath.apps.twitterclient.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.TweetUtils;
import com.codepath.apps.twitterclient.TwitterClient;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "tweets")
public class TweetModel extends Model {
	// Define table fields
	@Column(name = "body")
	private String body;
	
	@Column(name = "uid", index=true)
	private long uid;
	
	@Column(name = "createdAt")
	private long createdAt;
	
	@Column(name = "favorited")
	private boolean favorited;
	
	@Column(name = "retweeted")
	private boolean retweeted;
	
	@Column(name = "user")
	private UserModel user;
	
	public TweetModel() {
		super();
	}
	
	public TweetModel(String body, long uid, long createdAt, boolean favorited,
			boolean retweeted, UserModel user) {
		super();
		this.body = body;
		this.uid = uid;
		this.createdAt = createdAt;
		this.favorited = favorited;
		this.retweeted = retweeted;
		this.user = user;
	}


	// Getters
	public String getName() {
		return body;
	}
	
	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public UserModel getUser() {
		return user;
	}
	
	public static TweetModel fromJSON(JSONObject jsonObject) throws JSONException {
		TweetModel tweet = new TweetModel(
											jsonObject.getString("text"), 									// body
											jsonObject.getLong("id"), 										// uid
											TweetUtils.parseTweetJSONDate(jsonObject.getString("created_at")), 	// uid
											jsonObject.getBoolean("favorited"),								// favorited
											jsonObject.getBoolean("retweeted"),								// retweeted
											UserModel.fromJSON(jsonObject.getJSONObject("user"))			// User
											);
        return tweet;
	}

	public static List<TweetModel> fromJson(JSONArray jsonTweets) {
		List<TweetModel> tweets = new ArrayList<TweetModel>();
		try {
			int count = jsonTweets.length();
			for (int i = 0; i < count; i++) {
				JSONObject jsonTweet = (JSONObject) jsonTweets.get(i);
				TweetModel tweet = TweetModel.fromJSON(jsonTweet);
				tweets.add(tweet);
			}
			Log.d("TwitterClient", jsonTweets.toString());					
		} catch(JSONException e) {
			Log.e(TwitterClient.LOG_NAME, "Home timeline", e);
		}
		return tweets;
	}
	
	// Record Finders
	public static TweetModel byId(long id) {
	   return new Select().from(TweetModel.class).where("id = ?", id).executeSingle();
	}
	
	public static List<TweetModel> recentItems() {
      return new Select().from(TweetModel.class).orderBy("id DESC").limit("300").execute();
	}
}
package com.codepath.apps.twitterclient.tasks;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.twitterclient.TweetsAdapter;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.TwitterClientApp;
import com.codepath.apps.twitterclient.models.TweetModel;
import com.codepath.apps.twitterclient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PostTweetTask extends AsyncTask<Object, Void, Void> {

	@Override
	protected Void doInBackground(final Object... params) {
		final String tweet = (String) params[0];
		final Context context = (Context) params[1];
		TwitterClientApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, JSONObject jsonObject) {
				UserModel.save(jsonObject);
				TweetModel.save(jsonObject);				
				TweetsAdapter.instance.clearView();
				TweetsAdapter.instance.updateView(null);
				Toast.makeText(context, "TwitterBird reached safely", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, JSONArray jsonArray) {
				UserModel.save(jsonArray);
				TweetModel.save(jsonArray);
				TweetsAdapter.instance.clearView();
				TweetsAdapter.instance.updateView(null);
				Toast.makeText(context, "TwitterBird reached safely", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
				Toast.makeText(context, "TwitterBird shot down. Call PETA.", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject arg1) {
				Log.e(TwitterClient.LOG_NAME, "Unable to post", e);
				Toast.makeText(context, "TwitterBird shot down. Call PETA.", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		return null;
	}

}

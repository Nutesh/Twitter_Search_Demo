package com.example.nutesh.twitter_search_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.net.URLEncoder;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private TextView tweetDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tweetDisplay = (TextView) findViewById(R.id.tweet_txt);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_tweet);


        final LinearLayout myLayout
                = (LinearLayout) findViewById(R.id.my_tweet_layout);

        final long tweetId = 510908133917487104L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        myLayout.addView(TweetView);

                    }


                    @Override
                    public void failure(TwitterException exception) {

                        exception.printStackTrace();
                    }
                }


        );
    /*public void searchTwitter(View view) {

        EditText searchTxt = (EditText)findViewById(R.id.search_edit);
        String searchTerm = searchTxt.getText().toString();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("CONSUMER_KEY", "CONSUMER_SECRET"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        }

    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
    StatusesService statusesService = twitterApiClient.getStatusesService();
    Call<Tweet> call = statusesService.show(524971209851543553L, null, null, null);

        public void success(Result<Tweet> result) {

            //Do something with result
        }

    public void failure(TwitterException exception) {
        //Do something on failure
    }
    */
    }
}




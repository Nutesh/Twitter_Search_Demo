package com.example.nutesh.twitter_search_demo;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



import com.loopj.android.http.*;
import org.json.JSONObject;

import cz.msebera.android.httpclient.entity.mime.Header;
import retrofit2.Call;

public class MainActivity extends ListActivity {


    /* AppCompatActivity is changed to ListActivity
    private TextView tweetDisplay;
private TweetView tweetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tweetDisplay = (TextView) findViewById(R.id.tweet_txt);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("CONSUMER_KEY", "CONSUMER_SECRET"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        }
*/


    private ListActivity activity;
    final static String SearchTerm = "rats ship";
    final static String LOG_TAG = "rnc";
    String Key = null;
    String Secret = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Key = getStringFromManifest("CONSUMER_KEY");
        Secret = getStringFromManifest("CONSUMER_SECRET");
        downloadSearches();
    }

    private String getStringFromManifest(String key) {
        String results = null;

        try {
            Context context = this.getBaseContext();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            results = (String) ai.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    // download twitter searches after first checking to see if there is a network connection
    public void downloadSearches() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTwitterTask().execute(SearchTerm);
        } else {
            Log.v(LOG_TAG, "No network connection available.");
        }
    }

    // Uses an AsyncTask to download data from Twitter
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterSearchURL = "https://api.twitter.com/1.1/search/tweets.json?q=";

        @Override
        protected String doInBackground(String... searchTerms) {
            String result = null;

            if (searchTerms.length > 0) {
                result = getSearchStream(searchTerms[0]);
            }
            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            Searches searches = jsonToSearches(result);

            // lets write the results to the console as well
            for (Search search : searches) {
                Log.i(LOG_TAG, search.getText());
            }

            // send the tweets to the adapter for rendering
            ArrayAdapter<Search> adapter = new ArrayAdapter<Search>(activity, android.R.layout.simple_list_item_1, searches);
            setListAdapter(adapter);
        }

        // converts a string of JSON data into a SearchResults object
        private Searches jsonToSearches(String result) {
            Searches searches = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    // bring back the entire search object
                    SearchResults sr = gson.fromJson(result, SearchResults.class);
                    // but only pass the list of tweets found (called statuses)
                    searches = sr.getStatuses();
                } catch (IllegalStateException ex) {
                    // just eat the exception for now, but you'll need to add some handling here
                }
            }
            return searches;
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception for now, but you'll need to add some handling here
                }
            }
            return auth;
        }

        //vishal get post method
        String urlLink = "https://api.twitter.com/1/";
        //  String APP_ID = "c9e3f70e376c436f8acaf82a6a40f3fd025eff0b9f29346176c8baec3ea5432b08d9b63ab571be3030c951f805dfb6812450e4c4e19a71db711f39141eb13951";






/*
        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }
*/


//github method of extrcating data

        /*      private String getStream(String url) {
                  String results = null;

                  // Step 1: Encode consumer key and secret
                  try {
                      // URL encode the consumer key and secret
                      String urlApiKey = URLEncoder.encode(Key, "UTF-8");
                      String urlApiSecret = URLEncoder.encode(Secret, "UTF-8");

                      // Concatenate the encoded consumer key, a colon character, and the encoded consumer secret
                      String combined = urlApiKey + ":" + urlApiSecret;

                      // Base64 encode the string
                      String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                      // Step 2: Obtain a bearer token
                      HttpPost httpPost = new HttpPost(TwitterTokenURL);
                      httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                      httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                      httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                      String rawAuthorization = getResponseBody(httpPost);
                      Authenticated auth = jsonToAuthenticated(rawAuthorization);

                      // Applications should verify that the value associated with the
                      // token_type key of the returned object is bearer
                      if (auth != null && auth.token_type.equals("bearer")) {

                          // Step 3: Authenticate API requests with bearer token
                          HttpGet httpGet = new HttpGet(url);

                          // construct a normal HTTPS request and include an Authorization
                          // header with the value of Bearer <>
                          httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                          httpGet.setHeader("Content-Type", "application/json");
                          // update the results with the body of the response
                          results = getResponseBody(httpGet);
                      }
                  } catch (UnsupportedEncodingException ex) {
                  } catch (IllegalStateException ex1) {
                  }
                  return results;
              }
      */
        private String getSearchStream(String searchTerm) {
            String results = null;
            try {
                String encodedUrl = URLEncoder.encode(searchTerm, "UTF-8");
                results = TwitterSearchURL + encodedUrl;
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }

        private void ButtonOnClick(){



        }
    }
}







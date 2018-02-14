package com.example.nutesh.twitter_search_demo;

/**
 * Created by nutesh on 14/2/18.
 */

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.mime.Header;

class TwitterRestClientUsage {
    public void getPublicTimeline() throws JSONException {
        TwitterRestClient.get("statuses/public_timeline.json", null, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }


            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) throws JSONException {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = (JSONObject) timeline.get(0);
                String tweetText = firstEvent.getString("text");

                // Do something with the response
                System.out.println(tweetText);
            }
        });
    }
}
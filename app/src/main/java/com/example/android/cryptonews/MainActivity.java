package com.example.android.cryptonews;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;

import org.eluder.coveralls.maven.plugin.domain.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public ArrayList<JSONObject> coinData;  // holds each coins information
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Using volley to add requests to a queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(coinMarketData());
        queue.add(redditLogin());

    }

    // TODO: 18/08/2017 Create a request for each API.

    //This method returns a  request to CoinMarket API to be added to the queue.
    public Request coinMarketData(){
        String url = "https://api.coinmarketcap.com/v1/ticker/";
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    coinData = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        coinData.add(response.getJSONObject(i));
                    }
                    Log.d("CoinMarketAPI", coinData.toString());
                }
                catch (JSONException e){
                    Log.d("CoinMarketAPI",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CoinMarketAPI",error.toString());
            }
        });

        return jsArrayRequest;
    }

    //This method returns a request to Reddits API to be added to the queue
    public Request redditLogin(){
        String url = "https://www.reddit.com/api/v1/access_token";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{
                        JSONObject jsResponse = new JSONObject(response);
                        access_token = jsResponse.getString("access_token");
                        Log.d("reddit", access_token);
                        } catch (Exception e){
                            Log.d("Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", "CryptoNews1");
                params.put("password","johncena123");

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String creds = String.format("%s:%s","SWl6aZhQN8A10Q","64vaHGfBR-ViI1ygwuHHV0kdsbs");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        return postRequest;
    }


}

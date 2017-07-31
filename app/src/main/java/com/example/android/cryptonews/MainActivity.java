package com.example.android.cryptonews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<JSONObject> coinData;  // holds each coins information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Using volley to add requests to a queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(coinMarketData());
    }

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


}

package com.auction.grouptwo.androidapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String AUCTION = "AUCTION";

    private ArrayList<Auction> auctions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest("http://nackademiska-api.azurewebsites.net/api/auction",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject auction = (JSONObject) response.get(i);
                                auctions.add(new Auction(auction.getInt("id"), auction.getString("name"), auction.getString("description"),
                                        auction.getString("startTime"), auction.getString("endTime"), auction.getString("imageUrl"),
                                        auction.getInt("categoryId"), auction.getInt("supplierId"), auction.getDouble("buyNowPrice")));
                            }
                            setupAuctionList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);

    }

    private void setupAuctionList() {

        AuctionListAdapter auctionAdapter = new AuctionListAdapter(this, R.layout.auction_list_item, auctions);

        ListView auctionListView = (ListView) findViewById(R.id.auctionListView);

        auctionListView.setAdapter(auctionAdapter);

        auctionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(AUCTION, auctions.get(position));
                startActivity(intent);
            }
        });

    }
}

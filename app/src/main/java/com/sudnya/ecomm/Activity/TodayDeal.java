package com.sudnya.ecomm.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Adapter.TodaysDealAdapter;
import com.sudnya.ecomm.Helper.GenerateRandom_Color;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.HomeModel;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Created by Dell on 28-Sep-17.
 */

public class TodayDeal extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = TodayDeal.class.getSimpleName();
    private static TodayDeal instance;
    RecyclerView recyclerView;
    List<HomeModel> SubServiceList = new ArrayList<>();
    TextView txtAlert;
    String Email, Id, Name;
    String jsonString;
    SharedPreferences sharedPreferences;
    JSONArray jsonArray;
    Button sort;

    public static TodayDeal getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        instance = this;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Todays Deal");
            //       getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }
        sort = (Button) findViewById(R.id.sort);
        sort.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        txtAlert.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setBackgroundColor(GenerateRandom_Color.generateRandomColor());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        String urls = UrlLink.OFFERS_URL;
        Log.d(TAG, urls);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urls,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Find", String.valueOf(response));
                        SearchData(response);
                        Sorting();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void Sorting() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLink.PRODUCTS_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(TodayDeal.this, "Data Successfully Fetched", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject js = new JSONObject(response);
                            JSONArray jsonArray = js.getJSONArray("results");
                            jsonString = jsonArray.toString();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("jsonString", jsonString);
                            editor.apply();


                            JSONArray sortedJsonArray = new JSONArray();
                            List<JSONObject> jsonValues = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonValues.add(jsonArray.getJSONObject(i));
                            }
                            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                                //You can change "Name" with "ID" if you want to sort by ID
                                private static final String KEY_NAME = "id";

                                @Override
                                public int compare(JSONObject a, JSONObject b) {
                                    String valA = "";
                                    String valB = "";

                                    try {
                                        valA = (String) a.get(KEY_NAME);
                                        valB = (String) b.get(KEY_NAME);
                                    } catch (JSONException e) {
                                        //do something
                                    }
                                    return valA.compareTo(valB);
                                }
                            });

                            for (int i = 0; i < jsonArray.length(); i++) {
                                sortedJsonArray.put(jsonValues.get(i));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }

    private void SearchData(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            HomeModel superHero = new HomeModel();
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                superHero.setId(json.getString("id"));
                superHero.setCategory(json.getString("category"));
                superHero.setSubCategory(json.getString("subCategory"));
                superHero.setName(json.getString("productName"));
                superHero.setProductCompany(json.getString("productCompany"));
                superHero.setProductPrice(json.getString("productPrice"));
                superHero.setProductPriceBeforeDiscount(json.getString("productPriceBeforeDiscount"));
                superHero.setProductImage1(json.getString("productImage1"));
                superHero.setProductImage2(json.getString("productImage2"));
                superHero.setProductImage3(json.getString("productImage3"));
                superHero.setShippingCharge(json.getString("shippingCharge"));
                superHero.setOffers(json.getString("offers"));
                superHero.setProductAvailability(json.getString("productAvailability"));
                // superHero.setProductDescription(json.getString("productDescription"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SubServiceList.add(superHero);

        }
        TodaysDealAdapter adapter = new TodaysDealAdapter(SubServiceList, this);
        recyclerView.setAdapter(adapter);
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }


    public void onPause() {
        super.onPause();
        Log.d(TAG, "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "In the onStop() event");
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void addNewModules(HomeModel homeModel) {
        Intent intent = new Intent(getApplicationContext(), Details_Products.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", (homeModel.getId()));
        bundle.putString("category", (homeModel.getCategory()));
        bundle.putString("subCategory", (homeModel.getSubCategory()));
        bundle.putString("productName", (homeModel.getName()));
        bundle.putString("productCompany", (homeModel.getProductCompany()));
        bundle.putString("productPrice", (homeModel.getProductPrice()));
        bundle.putString("productPriceBeforeDiscount", (homeModel.getProductPriceBeforeDiscount()));
        bundle.putString("productImage1", (homeModel.getProductImage1()));
        bundle.putString("productImage2", (homeModel.getProductImage2()));
        bundle.putString("productImage3", (homeModel.getProductImage3()));
        bundle.putString("shippingCharge", (homeModel.getShippingCharge()));
        bundle.putString("productAvailability", (homeModel.getProductAvailability()));
        bundle.putString("productDescription", (homeModel.getProductDescription()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void addtoWishlist(HomeModel homeModel) {
        //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //   Toast.makeText(getApplicationContext, "Hello" + homeModel.getName(), Toast.LENGTH_SHORT).show();
        //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  context.startActivity(intent);
        if (UrlLink.getInstance(this).isLoggedIn()) {
            User user = UrlLink.getInstance(this).getUser();
            //(user.getName());
            Email = (user.getEmail());
            Name = homeModel.getName().trim();
            Id = homeModel.getId().trim();
        }


        if (!UrlLink.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLink.URL_WISHLIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                System.out.println("response=" + response);
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    //     startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_name", Name);
                    params.put("email", Email);
                    params.put("product_id", Id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == sort) {
            SubServiceList.clear();
            jsonString = sharedPreferences.getString("jsonString", null);

            try {
                jsonArray = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray sortedJsonArray = new JSONArray();
            List<JSONObject> jsonValues = new ArrayList<
                    >();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    jsonValues.add(jsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                //You can change "Name" with "ID" if you want to sort by ID
                private static final String KEY_NAME = "id";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = "";
                    String valB = "";

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    } catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                    //if you want to change the sort order, simply use the following:
                    //return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArray.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
            for (int i = 0; i < sortedJsonArray.length(); i++) {
                try {

                    JSONObject jsonObject = sortedJsonArray.getJSONObject(i);
                    HomeModel homeModel = new HomeModel();
                    homeModel.setId(jsonObject.getString("id"));
                    homeModel.setCategory(jsonObject.getString("category"));
                    homeModel.setSubCategory(jsonObject.getString("subCategory"));
                    homeModel.setName(jsonObject.getString("productName"));
                    homeModel.setProductCompany(jsonObject.getString("productCompany"));
                    homeModel.setProductPrice(jsonObject.getString("productPrice"));
                    homeModel.setProductPriceBeforeDiscount(jsonObject.getString("productPriceBeforeDiscount"));
                    homeModel.setProductImage1(jsonObject.getString("productImage1"));
                    homeModel.setProductImage2(jsonObject.getString("productImage2"));
                    homeModel.setProductImage3(jsonObject.getString("productImage3"));
                    homeModel.setShippingCharge(jsonObject.getString("shippingCharge"));
                    homeModel.setOffers(jsonObject.getString("offers"));
                    homeModel.setProductAvailability(jsonObject.getString("productAvailability"));
                    SubServiceList.add(homeModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TodaysDealAdapter adapter = new TodaysDealAdapter(SubServiceList, this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }

    }
}
package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.sudnya.ecomm.Adapter.SearchAdapter;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.HomeModel;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private static SearchActivity instance;
    RecyclerView recyclerView;
    List<HomeModel> SubServiceList = new ArrayList<>();
    TextView txtAlert;
    String Email, Id, Name;

    public static SearchActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search Products");
    //        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }
         txtAlert = (TextView) findViewById(R.id.txtAlert);
        txtAlert.setVisibility(View.VISIBLE);
        instance = this;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //  recyclerView.setBackgroundColor(GenerateRandom_Color.generateRandomColor());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String find) {
        SubServiceList.clear();
        txtAlert.setVisibility(View.GONE);
        //    Toast.makeText(getApplicationContext(), find, Toast.LENGTH_LONG).show();
        String urls = UrlLink.Search_URL + find;
        Log.d(TAG, urls);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urls,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Find", String.valueOf(response));
                        SearchData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        return true;
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
                superHero.setProductDescription(json.getString("productDescription"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SubServiceList.add(superHero);

        }
        SearchAdapter adapter = new SearchAdapter(SubServiceList, this);
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
        if (UrlLink.getInstance(this).isLoggedIn()) {
            User user = UrlLink.getInstance(this).getUser();
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
}

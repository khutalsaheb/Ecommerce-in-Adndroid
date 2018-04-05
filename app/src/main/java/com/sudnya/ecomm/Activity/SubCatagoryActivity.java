package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Adapter.ProductAdapter;
import com.sudnya.ecomm.Adapter.SubcatAdapter;
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

public class SubCatagoryActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    public static final String KEY_NAME = "subcategory";
    public static final String KEY_ID = "categoryid";
    private static final String TAG = SubCatagoryActivity.class.getSimpleName();
    private static SubCatagoryActivity instance;
    public String productid;
    String ss, Getid = "", selected;
    List<HomeModel> SubServiceList = new ArrayList<>();
    RecyclerView recyclerView;
    String name = "";
    private ArrayList<String> getSubcatagory;
    private Spinner spinner;
    private JSONArray result;
    String Email,Id,Name;

    public static SubCatagoryActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recyclerview);
        instance = this;
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Catagory");
     //       getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            ss = null;
            Getid = null;
        } else {
            ss = bundle.getString(ProductAdapter.KEY_NAME);
            Getid = bundle.getString(ProductAdapter.KEY_ID);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //  recyclerView.setBackgroundColor(GenerateRandom_Color.generateRandomColor());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        SubServiceList = new ArrayList<>();
        getSubcatagory = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        GetSpinnerData();

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        productid = getName(position);
       String urls = UrlLink.Product_URL + productid;
        Log.d(TAG, urls);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urls,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                     //   getSubcatagory.clear();
                        Log.d(TAG, String.valueOf(response));
                        parseData(response);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void GetSpinnerData() {
        final String url = UrlLink.Spinner_URL + Getid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray("result");
                            getStudents(result);

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        Log.d(TAG, String.valueOf(j));
        for (int i = 0; i < j.length(); i++) {
            JSONObject json;
            try {
                json = j.getJSONObject(i);
                getSubcatagory.add(json.getString(KEY_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spinner.setAdapter(new ArrayAdapter<>(SubCatagoryActivity.this, android.R.layout.simple_spinner_dropdown_item, getSubcatagory));
        }
    }

    private void parseData(JSONArray array) {
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
        SubcatAdapter adapter = new SubcatAdapter(SubServiceList, this);
        recyclerView.setAdapter(adapter);
    }

    private String getName(int position) {

        try {
            JSONObject json = result.getJSONObject(position);
            name = json.getString(KEY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "In the onResume() event");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "In the onPause() event");
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
             Name=homeModel.getName().trim();
             Id=homeModel.getId().trim();
        }


        if (!UrlLink.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else {
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

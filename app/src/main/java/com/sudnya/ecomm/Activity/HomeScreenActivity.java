package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Adapter.ProductAdapter;
import com.sudnya.ecomm.Adapter.Todays_Offer_Adapter;
import com.sudnya.ecomm.Databases.CartActivity;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.HomeModel;
import com.sudnya.ecomm.Model.Todays_Offer_Model;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;
import com.sudnya.ecomm.ServiceAPI.HomeApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String KEY_NAME = "Name";
    public static final String KEY_ID = "Id";
    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private static HomeScreenActivity instance;
    RecyclerView.Adapter adapter;
    int IOConnect = 0;
    ProgressBar prgLoading;
    TextView txtAlert;
    TextView todaysview;
    private List<Todays_Offer_Model> ListtodaysOfferModels;
    private List<HomeModel> homeModelList;
    private RecyclerView recyclerView, recyclerViewoffers, recyclerViewDiscount;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ProductAdapter adapt;

    public static HomeScreenActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        if (UrlLink.getInstance(this).isLoggedIn()) {
            User user = UrlLink.getInstance(this).getUser();
            TextView nav_user = (TextView) hView.findViewById(R.id.textView);//sign
            TextView sign = (TextView) hView.findViewById(R.id.sign);
            nav_user.setText(user.getName());
            sign.setText(user.getEmail());
        }
        isStoragePermissionGranted();
        ListtodaysOfferModels = new ArrayList<>();
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        GetCatagory();

        todaysview = (TextView) findViewById(R.id.todaysview);
        todaysview.setOnClickListener(this);
    }

    private void GetCatagory() {
        final RestAdapter restadapter = new RestAdapter.Builder()
                .setEndpoint(UrlLink.HOMEROOT_URL).build();
        HomeApi flowerapi = restadapter.create(HomeApi.class);

        flowerapi.getHome(new Callback<List<HomeModel>>() {
            @Override
            public void success(List<HomeModel> homelist, retrofit.client.Response response) {
                prgLoading.setVisibility(View.GONE);
                if ((homelist.size() > 0) && (IOConnect == 0)) {
                    recyclerView.setVisibility(View.VISIBLE);
                    homeModelList = homelist;
                    String[] items = new String[homeModelList.size()];
                    for (int i = 0; i < homeModelList.size(); i++) {
                        items[i] = homeModelList.get(i).getName();
                    }
                    adapt = new ProductAdapter(getApplicationContext(), homeModelList);
                    recyclerView.setAdapter(adapt);
                    GetTodaysOffers();
                } else {
                    txtAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Opps!!! Something is Wrong With you...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetTodaysOffers() {
        recyclerViewoffers = (RecyclerView) findViewById(R.id.recyclerViewoffers);
        recyclerViewoffers.setHasFixedSize(true);
        //   recyclerViewlayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerViewlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewoffers.setLayoutManager(recyclerViewlayoutManager);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlLink.OFFERS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  loading.dismiss();
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

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Todays_Offer_Model offer_model = new Todays_Offer_Model();
            JSONObject json;
            try {
                json = array.getJSONObject(i);
                //  offer_model.setProductImage1(json.getString("imageurl"));
                offer_model.setProductPrice(json.getString("productPrice"));
                offer_model.setSubCategory(json.getString("productCompany"));
                offer_model.setProductName(json.getString("productName"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListtodaysOfferModels.add(offer_model);
            GetDiscount();
        }
        adapter = new Todays_Offer_Adapter(ListtodaysOfferModels, this);
        recyclerViewoffers.setAdapter(adapter);
    }

    private void GetDiscount() {
        recyclerViewDiscount = (RecyclerView) findViewById(R.id.recyclertodays);
        recyclerViewDiscount.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDiscount.setLayoutManager(recyclerViewlayoutManager);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlLink.OFFERS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseDataDiscount(response);
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

    private void parseDataDiscount(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            Todays_Offer_Model offer_model = new Todays_Offer_Model();
            JSONObject json;
            try {
                json = response.getJSONObject(i);
                //    offer_model.setProductImage1(json.getString("imageurl"));
                offer_model.setProductPrice(json.getString("productPrice"));
                offer_model.setSubCategory(json.getString("productCompany"));
                offer_model.setProductName(json.getString("productName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListtodaysOfferModels.add(offer_model);
        }
        adapter = new Todays_Offer_Adapter(ListtodaysOfferModels, this);
        recyclerViewDiscount.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
         /*   Intent i = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(i);*/
        } else if (id == R.id.nav_checkout) {
            Intent i = new Intent(getApplicationContext(), ActivityCheckout.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SignIn(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }


    public void onStart() {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "In the onStart() event");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (homeModelList != null && ListtodaysOfferModels != null) {
            ListtodaysOfferModels.clear();
            homeModelList.clear();
            super.onDestroy();
        } else
            super.onDestroy();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_carts, menu);
        return true;
    }

    public void addNewModule(HomeModel dataToEditModel) {
        Intent intent = new Intent(getApplicationContext(), SubCatagoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, (dataToEditModel.getName()));
        bundle.putString(KEY_ID, (dataToEditModel.getId()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == todaysview) {
            startActivity(new Intent(getApplicationContext(), TodayDeal.class));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent i = new Intent(HomeScreenActivity.this, CartActivity.class);
                startActivity(i);
                return true;

            case R.id.checkout:
                i = new Intent(HomeScreenActivity.this, ActivityCheckout.class);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

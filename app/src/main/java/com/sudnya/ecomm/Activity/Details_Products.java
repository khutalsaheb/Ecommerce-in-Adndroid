package com.sudnya.ecomm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Adapter.MyAdapter;
import com.sudnya.ecomm.Databases.ActActivity;
import com.sudnya.ecomm.Databases.Contact;
import com.sudnya.ecomm.Databases.ContactListAdapter;
import com.sudnya.ecomm.Databases.RecyclerItemClickListener;
import com.sudnya.ecomm.Databases.SQLiteDB;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Helper.Utility;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Details_Products extends AppCompatActivity implements View.OnClickListener, RecyclerItemClickListener {
    private static final String TAG = Details_Products.class.getSimpleName();
    TextView brand, availability, extracharge, dprice, price, description;
    String ids, category, subCategory, productName, productCompany, productPrice, productPriceBeforeDiscount, productImage1, productImage2, productImage3, shippingCharge, productAvailability, valuedescription;
    ViewPager mPager;
    int currentPage = 0;
    FloatingActionButton fabAddPerson, fabAddWishlist;
    RecyclerView lvContact;
    LinearLayoutManager linearLayoutManager;
    String Email, Id, Name;
    private AlertDialog.Builder subDialog;
    private ContactListAdapter contactListAdapter;
    private Contact contact;
    private SQLiteDB sqLiteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_products);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Details");
  //          getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }
        brand = (TextView) findViewById(R.id.brand);
        availability = (TextView) findViewById(R.id.availability);
        extracharge = (TextView) findViewById(R.id.extracharge);
        dprice = (TextView) findViewById(R.id.dprice);
        price = (TextView) findViewById(R.id.price);
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        description = (TextView) findViewById(R.id.description);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ids = extras.getString("id");
            category = extras.getString("category");
            subCategory = extras.getString("subCategory");
            productName = extras.getString("productName");
            productCompany = extras.getString("productCompany");
            productPrice = extras.getString("productPrice");
            productPriceBeforeDiscount = extras.getString("productPriceBeforeDiscount");
            productImage1 = extras.getString("productImage1");
            productImage2 = extras.getString("productImage2");
            productImage3 = extras.getString("productImage3");
            shippingCharge = extras.getString("shippingCharge");
            productAvailability = extras.getString("productAvailability");
            valuedescription = extras.getString("productDescription");
        }

        toolbar.setTitle(productName);
        brand.setText(productCompany);
        availability.setText(productAvailability);
        extracharge.setText("Shipping Charge Rs. " + shippingCharge);
        dprice.setText("Rs." + productPrice);
        price.setText("Rs." + productPriceBeforeDiscount);
        description.setText(valuedescription);
        System.out.println(TAG + productCompany);
        System.out.println(TAG + productAvailability);
        System.out.println(TAG + shippingCharge);
        System.out.println(TAG + productPrice);
        System.out.println(TAG + productPriceBeforeDiscount);
        //   Picasso.with(getApplicationContext()).load(productImage1).into(img);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(productName);
        ALLDATA();
        final String[] XMEN = {productImage1, productImage2, productImage3};
        ArrayList<String> XMENArray = new ArrayList<>();

        Collections.addAll(XMENArray, XMEN);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(Details_Products.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    private void ALLDATA() {
        sqLiteDB = new SQLiteDB(this);
        lvContact = (RecyclerView) findViewById(R.id.lvContact);
        linearLayoutManager = new LinearLayoutManager(this);
        contactListAdapter = new ContactListAdapter(this);
        contactListAdapter.setOnItemClickListener(this);

        lvContact.setLayoutManager(linearLayoutManager);
        lvContact.setAdapter(contactListAdapter);
        fabAddPerson = (FloatingActionButton) findViewById(R.id.add);
        fabAddWishlist = (FloatingActionButton) findViewById(R.id.fabAddWishlist);
        fabAddPerson.setOnClickListener(this);
        fabAddWishlist.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                AddtoCart();
                break;
            case R.id.fabAddWishlist:
                addtoWishlist();
                break;
        }
    }

    private void addtoWishlist() {
        if (UrlLink.getInstance(this).isLoggedIn()) {
            User user = UrlLink.getInstance(this).getUser();
            //(user.getName());
            Email = (user.getEmail());
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
                    params.put("product_name", productName);
                    params.put("email", Email);
                    params.put("product_id", ids);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }


    }

    public void AddtoCart() {

        //subdialog
        subDialog = new AlertDialog.Builder(Details_Products.this)
                .setMessage("Please enter all the details!!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });

        //maindialog
        LayoutInflater li = LayoutInflater.from(Details_Products.this);
        View promptsView = li.inflate(R.layout.activity_act, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(Details_Products.this);
        mainDialog.setView(promptsView);
        final EditText personName;
        final Button btnDelete;
        final EditText phone, price, total;
        personName = (EditText) promptsView.findViewById(R.id.personText);
        phone = (EditText) promptsView.findViewById(R.id.phoneText);
        price = (EditText) promptsView.findViewById(R.id.personprice);
        total = (EditText) promptsView.findViewById(R.id.persontotal);
        btnDelete = (Button) promptsView.findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.GONE);
        personName.setText(productName);
        price.setText(productPrice);
        phone.setText("1");
        total.setText(productPrice);
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = mainDialog.create();
        dialog.show();

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!Utility.isBlankField(personName) && !Utility.isBlankField(price) && !Utility.isBlankField(phone) && !Utility.isBlankField(total)) {
                    int LastPrice = (Integer.parseInt(productPrice) + Integer.parseInt(shippingCharge));
                    int LastQuantity = Integer.parseInt((phone.getText().toString()));
                    int Temp = LastPrice * LastQuantity;
                    contact = new Contact();
                    contact.setName(productName);
                    contact.setPhone(phone.getText().toString());
                    contact.setPrice(productPrice);
                    contact.setTotal((String.valueOf(Temp)));
                    sqLiteDB.create(contact);
                    Toast.makeText(getApplicationContext(), "Successfully Added to Cart !!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData() {
        sqLiteDB = new SQLiteDB(this);
        List<Contact> contactList = new ArrayList<>();
        Cursor cursor = sqLiteDB.retrieve();
        Contact contact;

        if (cursor.moveToFirst()) {
            do {
                contact = new Contact();
                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setPrice(cursor.getString(3));
                contact.setTotal(cursor.getString(4));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        contactListAdapter.clear();
        contactListAdapter.addAll(contactList);
    }

    @Override
    public void onItemClick(int position, View view) {
        ActActivity.start(this, contactListAdapter.getItem(position));
    }
}
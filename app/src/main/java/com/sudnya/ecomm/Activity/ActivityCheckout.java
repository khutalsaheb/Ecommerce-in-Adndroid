package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Databases.SQLiteDB;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityCheckout extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ActivityCheckout.class.getSimpleName();
    public String finalvalue, finallist, Email;
    double total = 0.0;
    String Menu_name, Quantity, Price;
    String OrderList = " ";
    LinearLayout linearLayout;
    TextView Order, Total;
    Button btnSend;
    EditText edtName, edtAddress, edtpincode, edtOrderList, edtPhone, edtComment, edtcity;
    private SQLiteDB sqLiteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout");
     //       getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }

        if (!UrlLink.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        Total = (TextView) findViewById(R.id.total);
        Order = (TextView) findViewById(R.id.count);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtcity = (EditText) findViewById(R.id.edtcity);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtpincode = (EditText) findViewById(R.id.edtpincode);
        edtOrderList = (EditText) findViewById(R.id.edtOrderList);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        SQLiteDB sqLiteDB = new SQLiteDB(this);
        ArrayList<ArrayList<Object>> contactList;

        contactList = sqLiteDB.getAllData();

        for (int i = 0; i < contactList.size(); i++) {
            ArrayList<Object> row = contactList.get(i);
            total = total + Double.parseDouble(row.get(4).toString());
            finalvalue = String.valueOf(total);
            System.out.println(TAG + finalvalue);
            Menu_name = row.get(1).toString();
            Quantity = row.get(2).toString();
            Price = row.get(3).toString();
            OrderList += (Menu_name + "=>" + Quantity + "=" + Price + " " + ",\n");
            finallist = String.valueOf(OrderList);

        }

        if (finalvalue == null || finallist == null) {
            Toast.makeText(getApplicationContext(), "No Order In Your Cart", Toast.LENGTH_SHORT).show();
            Total.setText("Total Amount= 00.00");
            Order.setText("Shopping List is EMPTY...!!");
            linearLayout.setVisibility(View.GONE);
        } else {
            Total.setText("Total Amount= " + finalvalue);
            edtOrderList.setText(finallist);
            linearLayout.setVisibility(View.VISIBLE);
        }
        if (UrlLink.getInstance(this).isLoggedIn()) {
            User user = UrlLink.getInstance(this).getUser();
            //(user.getName());
            Email = (user.getEmail());
        }


    }

    @Override
    public void onClick(View view) {
        if (view == btnSend) {
            if (!UrlLink.getInstance(this).isLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            // edtName, edtAddress, edtpincode, edtOrderList, edtPhone, edtComment, edtcity;
            final String username = edtName.getText().toString().trim();
            final String address = edtAddress.getText().toString().trim();
            final String pincode = edtpincode.getText().toString().trim();
            final String contactno = edtPhone.getText().toString().trim();
            final String comment = edtComment.getText().toString().trim();
            final String city = edtcity.getText().toString().trim();


            //first we will do the validations

            if (TextUtils.isEmpty(username)) {
                edtName.setError("Please enter username");
                edtName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(address)) {
                edtAddress.setError("Please enter your email");
                edtAddress.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(city)) {
                edtcity.setError("Please enter your city");
                edtcity.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pincode)) {
                edtpincode.setError("Please enter your pincode");
                edtpincode.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(contactno)) {
                edtPhone.setError("Enter a contact number");
                edtPhone.requestFocus();
                return;
            }


            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLink.URL_ADDTOCART,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                System.out.println("response=" + response);
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);

                                //if no error in response
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                    sqLiteDB = new SQLiteDB(ActivityCheckout.this);
                                    sqLiteDB.clearTable();
                                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));

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
                //  (name, email, address, contactno,city,pincode)
                @Override

                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", username);
                    params.put("email", Email);
                    params.put("address", address);
                    params.put("contactno", contactno);
                    params.put("city", city);
                    params.put("orders", finallist);
                    params.put("comment", comment);
                    params.put("pincode", pincode);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }


    }


}

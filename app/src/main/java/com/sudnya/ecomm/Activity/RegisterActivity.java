package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    EditText etname, etpassword, etconfirmpassword, etemail, etmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Registration");
            //  getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }

        //if the user is already logged in we will directly start the profile activity
        if (UrlLink.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        etname = (EditText) findViewById(R.id.name);
        etemail = (EditText) findViewById(R.id.email);
        etconfirmpassword = (EditText) findViewById(R.id.confirmpassword);
        etmobile = (EditText) findViewById(R.id.mobile);
        etpassword = (EditText) findViewById(R.id.password);

    }

    public void Already(View view) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    public void RegisterMe(View view) {
        final String username = etname.getText().toString().trim();
        final String email = etemail.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();
        final String confimpassword = etconfirmpassword.getText().toString().trim();
        final String mobile = etmobile.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etname.setError("Please enter username");
            etname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etemail.setError("Please enter your email");
            etemail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            etmobile.setError("Please enter your mobile Number");
            etmobile.requestFocus();
            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Enter a valid email");
            etemail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etpassword.setError("Enter a password");
            etpassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confimpassword)) {
            etconfirmpassword.setError("Enter a confirm password");
            etconfirmpassword.requestFocus();
            return;
        }
        if (!password.equals(confimpassword)) {
            etconfirmpassword.setError("password not match try again");
            etconfirmpassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLink.URL_REGISTER,
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

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("contactno")
                                        //         userJson.getString("password")
                                );

                                //storing the user in shared preferences
                                UrlLink.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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
                params.put("name", username);
                params.put("email", email);
                params.put("password", password);
                params.put("contactno", mobile);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "In the onStart() event");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "In the onResume() event");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "In the onPause() event");
    }
}
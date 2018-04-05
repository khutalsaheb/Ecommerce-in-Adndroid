package com.sudnya.ecomm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sudnya.ecomm.Helper.UrlLink;
import com.sudnya.ecomm.Model.User;
import com.sudnya.ecomm.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public EditText etname, etpassword, etconfirmpassword, etemail, etmobile;
    private TextView updateprofile, updatepassword, updateaddress, logout;
    private LinearLayout updatepro, updatepass, updateadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
      //      getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.wishlist));
        }
        if (!UrlLink.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        updateprofile = (TextView) findViewById(R.id.profile);
        updatepassword = (TextView) findViewById(R.id.passwordchange);
        updateaddress = (TextView) findViewById(R.id.addresschange);
        updatepro = (LinearLayout) findViewById(R.id.email_login_form);
        updatepass = (LinearLayout) findViewById(R.id.password_login_form);
        updateadd = (LinearLayout) findViewById(R.id.address_login_form);
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(this);
        updateprofile.setOnClickListener(this);
        updatepassword.setOnClickListener(this);
        updateaddress.setOnClickListener(this);

        etname = (EditText) findViewById(R.id.name);
        etemail = (EditText) findViewById(R.id.email);
        etconfirmpassword = (EditText) findViewById(R.id.confirmpassword);
        etmobile = (EditText) findViewById(R.id.mobile);
        etpassword = (EditText) findViewById(R.id.password);

        User user = UrlLink.getInstance(this).getUser();

        //setting the values to the textviews
        //  textViewId.setText(String.valueOf(user.getId()));
        etname.setText(user.getName());
        etemail.setText(user.getEmail());
        etmobile.setText(user.getContactno());


    }

    @Override
    public void onClick(View view) {
        if (view == updateprofile) {
            updatepro.setVisibility(View.VISIBLE);
            updatepass.setVisibility(View.GONE);
            updateadd.setVisibility(View.GONE);
            updatepro.setVisibility(View.VISIBLE);
        }
        if (view == updatepassword) {
            updatepro.setVisibility(View.GONE);
            updatepass.setVisibility(View.VISIBLE);
            updateadd.setVisibility(View.GONE);

        }
        if (view == updateaddress) {
            updatepro.setVisibility(View.GONE);
            updatepass.setVisibility(View.GONE);
            updateadd.setVisibility(View.VISIBLE);

        }
        if (view == logout) {
            finish();
            UrlLink.getInstance(getApplicationContext()).logout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.sudnya.ecomm.Helper;

/**
 * @ Created by Dell on 07-Sep-17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sudnya.ecomm.Activity.HomeScreenActivity;
import com.sudnya.ecomm.Model.User;

public class UrlLink {
    // public static final String DATA_URL = "http://api2.mytrendin.com/json/";
    public static final String HOMEROOT_URL = "http://192.168.1.19/ShoppingPortalProVersion/shopping/Android/";
    public static final String Search_URL = HOMEROOT_URL + "find.php?productName=";
    public static final String Product_URL = HOMEROOT_URL + "product.php?subCategory=";
    public static final String Spinner_URL = HOMEROOT_URL + "Spinner.php?catagory_id=";
    public static final String OFFERS_URL = HOMEROOT_URL + "offers.php";
    public static final String PRODUCTS_URL = HOMEROOT_URL + "products.php";

    private static final String ROOT_URL = HOMEROOT_URL + "Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_ADDTOCART = ROOT_URL + "addtocart";
    public static final String URL_WISHLIST = ROOT_URL + "wishlist";


    private static final String SHARED_PREF_NAME = "sudnyaecom";
    private static final String KEY_USERNAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE = "contactno";
    private static final String KEY_ID = "keyid";

    private static UrlLink mInstance;
    private static Context mCtx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private UrlLink(Context context) {
        mCtx = context;
    }

    public static synchronized UrlLink getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UrlLink(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_MOBILE, user.getContactno());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, HomeScreenActivity.class));
    }

}

package com.sudnya.ecomm.Helper;

import android.widget.EditText;

public class Utility {
    public static boolean isBlankField(EditText etPersonData) {
        return etPersonData.getText().toString().trim().equals("");
    }
}

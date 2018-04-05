package com.sudnya.ecomm.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.sudnya.ecomm.Databases.ContactField.TABLE_NAME;

public class SQLiteDB extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ContactField.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ContactField.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactField.COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
                    ContactField.COLUMN_PRICE + TEXT_TYPE + COMMA_SEP +
                    ContactField.COLUMN_TOTAL + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void create(Contact contact) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactField.COLUMN_NAME, contact.getName());
        values.put(ContactField.COLUMN_PHONE, contact.getPhone());
        values.put(ContactField.COLUMN_PRICE, contact.getPrice());
        values.put(ContactField.COLUMN_TOTAL, contact.getTotal());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TABLE_NAME,
                null,
                values);
    }

    public Cursor retrieve() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ContactField.COLUMN_ID,
                ContactField.COLUMN_NAME,
                ContactField.COLUMN_PHONE,
                ContactField.COLUMN_PRICE,
                ContactField.COLUMN_TOTAL};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ContactField.COLUMN_NAME + " ASC";

        Cursor c = db.query(
                TABLE_NAME,                    // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        return c;
    }

    public ArrayList<ArrayList<Object>> getAllData() {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<>();

        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ContactField.COLUMN_ID,
                ContactField.COLUMN_NAME,
                ContactField.COLUMN_PHONE,
                ContactField.COLUMN_PRICE,
                ContactField.COLUMN_TOTAL};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ContactField.COLUMN_NAME + " ASC";

        try {
            cursor = db.query(
                    TABLE_NAME,                    // The table to query
                    projection,                                 // The columns to return
                    null,                                       // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    sortOrder                                   // The sort order
            );
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<>();

                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }


    public void update(Contact contact) {
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ContactField.COLUMN_NAME, contact.getName());
        values.put(ContactField.COLUMN_PHONE, contact.getPhone());
        values.put(ContactField.COLUMN_PRICE, contact.getPrice());
        values.put(ContactField.COLUMN_TOTAL, contact.getTotal());

        // Which row to update, based on the ID
        String selection = ContactField.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(contact.getId())};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id) {
        SQLiteDatabase db = getReadableDatabase();

        try {        // Define 'where' part of query.
            String selection = ContactField.COLUMN_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(id)};
            // Issue SQL statement.
            db.delete(TABLE_NAME, selection, selectionArgs);
        } finally {
            db.close();
        }
    }


    public void clearTable() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}

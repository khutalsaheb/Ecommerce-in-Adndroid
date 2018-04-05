package com.sudnya.ecomm.Databases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sudnya.ecomm.R;


public class ActActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText personName;
    private EditText phone, price, total;

    private Button btnAdd, btnEdit, btnDelete;

    private SQLiteDB sqLiteDB;
    private Contact contact;

    public static void start(Context context) {
        Intent intent = new Intent(context, ActActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, Contact contact) {
        Intent intent = new Intent(context, ActActivity.class);
        intent.putExtra(ActActivity.class.getSimpleName(), contact);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);

        personName = (EditText) findViewById(R.id.personText);
        phone = (EditText) findViewById(R.id.phoneText);
        price = (EditText) findViewById(R.id.personprice);
        total = (EditText) findViewById(R.id.persontotal);
        phone.setEnabled(false);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        contact = getIntent().getParcelableExtra(ActActivity.class.getSimpleName());
        if (contact != null) {
            btnAdd.setVisibility(View.GONE);

            personName.setText(contact.getName());
            phone.setText(contact.getPhone());
            price.setText(contact.getPrice());
            total.setText(contact.getTotal());
        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        sqLiteDB = new SQLiteDB(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            contact = new Contact();
            contact.setName(personName.getText().toString());
            contact.setPhone(phone.getText().toString());
            contact.setPrice(price.getText().toString());
            contact.setTotal(total.getText().toString());
            sqLiteDB.create(contact);

            Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
            finish();
        } else if (v == btnEdit) {
            contact.setName(personName.getText().toString());
            contact.setPhone(phone.getText().toString());
            contact.setPrice(price.getText().toString());
            contact.setTotal(total.getText().toString());

            sqLiteDB.update(contact);

            Toast.makeText(this, "Edited!", Toast.LENGTH_SHORT).show();
            finish();
        } else if (v == btnDelete) {
            sqLiteDB.delete(contact.getId());

            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

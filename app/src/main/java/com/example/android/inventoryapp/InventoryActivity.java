package com.example.android.inventoryapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        String[] projection = new String[] {
                ProductEntry.COLUUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        Cursor cursor = getContentResolver().query(ProductEntry.CONTENT_URI, projection, null, null, null);


        int count = cursor.getCount();

        TextView textView = (TextView) findViewById(R.id.db_count);
        textView.setText("Number of rows in the database: " + String.valueOf(count));

    }

    // TODO: Add AsyncLoader
    // TODO: Add CursorAdaptor and list
}

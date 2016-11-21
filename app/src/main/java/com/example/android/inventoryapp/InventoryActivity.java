package com.example.android.inventoryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = new ProductDbHelper(this).getReadableDatabase();

        String[] projection = new String[] {
                ProductEntry.COLUUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projection, null, null, null, null, null);

        int count = cursor.getCount();

        TextView textView = (TextView) findViewById(R.id.db_count);
        textView.setText("Number of rows in the database: " + String.valueOf(count));

    }
}

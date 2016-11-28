package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_product_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editorIntent = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(editorIntent);
            }
        });

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

        if (cursor != null) {
            int count = cursor.getCount();

            String result = "";

            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(ProductEntry.COLUUMN_PRODUCT_NAME);
                int priceIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
                int quantityIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

                String name = cursor.getString(nameIndex);
                // Get the price back to decimal format
                String price = Double.toString(cursor.getDouble(priceIndex) / 100);
                String quantity = Integer.toString(cursor.getInt(quantityIndex));

                result += name + " - " + price + " - " + quantity + "\n\n";
            }

            TextView textView = (TextView) findViewById(R.id.db_count);
            textView.setText(result);
            cursor.close();
        }
    }

    // TODO: Add AsyncLoader
    // TODO: Add CursorAdaptor and list
}

package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Change action bar title
        setTitle(getString(R.string.add_activity));

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reference items
                EditText nameField = (EditText) findViewById(R.id.product_name_edit_text);
                EditText priceField = (EditText) findViewById(R.id.price_edit_text);
                EditText quantityField = (EditText) findViewById(R.id.quantity_edit_text);

                // Get values
                String name = nameField.getText().toString();
                // Save price multiplied by 100 to save without decimals for accuracy
                int price = (int) (parseDouble(priceField.getText().toString()) * 100);
                Log.i("EditorActivity", "Price is $" + priceField.getText().toString());
                int quantity = parseInt(quantityField.getText().toString());

                addProduct(name, price, quantity);
            }
        });
    }

    // Calls to insert product to database
    private void addProduct(String name, int price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
        getContentResolver().notifyChange(uri, null);

        finish();
    };
}
package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;

    private static final int SINGLE_PRODUCT_LOADER = 3;

    // reference EditText widgets
    private EditText mEditName;

    private EditText mEditPrice;

    private EditText mEditQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mUri = getIntent().getData();

        // Initialize edit texts
        mEditName = (EditText) findViewById(R.id.product_name_edit_text);
        mEditPrice = (EditText) findViewById(R.id.price_edit_text);
        mEditQuantity = (EditText) findViewById(R.id.quantity_edit_text);

        // Change action bar title
        if (mUri != null) {
            setTitle("Product Info");

            getLoaderManager().initLoader(SINGLE_PRODUCT_LOADER, null, this);
        } else {
            setTitle(getString(R.string.add_activity));
        }

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
                int quantity = parseInt(quantityField.getText().toString());

                if (mUri == null) {
                    addProduct(name, price, quantity);
                } else {
                    editProduct(name, price, quantity);
                }
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

        finish();
    };

    private void editProduct(String name, int price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        getContentResolver().update(mUri, values, null, null);

        finish();
    };

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String[] projection = new String[] {
                ProductEntry._ID,
                ProductEntry.COLUUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE
        };

        switch (loaderId) {
            case SINGLE_PRODUCT_LOADER:
                return new CursorLoader(
                        getApplicationContext(),
                        mUri,
                        projection,
                        null,
                        null,
                        null
                );

            default:
                    return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToNext()) {
            // Extract data from cursor
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUUMN_PRODUCT_NAME));
            String price = CursorHelper.intToMoneyString(cursor, cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
            String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY)));

            // set edit text values
            mEditName.setText(name);
            mEditPrice.setText(price);
            mEditQuantity.setText(quantity);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEditName.setText("");
        mEditPrice.setText("");
        mEditQuantity.setText("");
    }
}
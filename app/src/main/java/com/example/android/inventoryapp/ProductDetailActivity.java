package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductPreviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;

    // Loader id
    private static final int PREVIEW_LOADER = 4;

    // Set up text fields
    private TextView mNameView;

    private TextView mPriceView;

    private TextView mQuantityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_preview);

        mNameView = (TextView) findViewById(R.id.name_text_view);

        mPriceView = (TextView) findViewById(R.id.price_text_view);

        mQuantityView = (TextView) findViewById(R.id.quantity_text_view);

        // get data from intent
        mUri = getIntent().getData();
        setTitle(R.string.product_detail);

        getLoaderManager().initLoader(PREVIEW_LOADER, null, this);
    }

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
            case PREVIEW_LOADER:
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
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUUMN_PRODUCT_NAME));
            String price = CursorHelper.intToMoneyString(cursor, cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
            String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY)));

            mNameView.setText(name);
            mPriceView.setText(price);
            mQuantityView.setText(quantity);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameView.setText("");
        mPriceView.setText("");
        mQuantityView.setText("");
    }
}

package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;

    // data variables
    private int mCurrentQuantity;
    private double mCurrentPrice;
    private double mCurrentSales;

    // Loader id
    private static final int PREVIEW_LOADER = 4;

    // Set up text fields
    private TextView mNameView;

    private TextView mPriceView;

    private TextView mQuantityView;

    private TextView mTotalSalesView;

    // Set up buttons
    private Button mReceiveShipmentButton;

    private Button mRecordSaleButton;

    private Button mOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize TextViews
        mNameView = (TextView) findViewById(R.id.name_text_view);

        mPriceView = (TextView) findViewById(R.id.price_text_view);

        mQuantityView = (TextView) findViewById(R.id.quantity_text_view);

        mTotalSalesView = (TextView) findViewById(R.id.total_sales_text_view);

        // Initialize Buttons
        mReceiveShipmentButton = (Button) findViewById(R.id.shipment_button);

        mRecordSaleButton = (Button) findViewById(R.id.record_sale);

        mOrderButton = (Button) findViewById(R.id.order_button);

        // get data from intent
        mUri = getIntent().getData();
        if (mUri == null) {
            finish();
        }

        setTitle(R.string.product_detail);

        // On Click listener for Receiving shipments
        mReceiveShipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.number_picker_dialog, null);
                // Setup number picker
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(500);
                numberPicker.setWrapSelectorWheel(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setView(dialogView);

                builder.setTitle(getString(R.string.receive_dialog_title));
                builder.setPositiveButton(R.string.update_quantity_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ContentValues updateValues = new ContentValues();
                        // clear focus tip from Dvd Franco
                        // http://stackoverflow.com/questions/3691099/android-numberpicker-not-saving-edittext-changes
                        numberPicker.clearFocus();
                        updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, numberPicker.getValue() + mCurrentQuantity);

                        int rowsAffected = getContentResolver().update(mUri, updateValues, null, null);

                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Quantity has been updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Quantity could not be updated", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        // On Click listener for marking a sale
        mRecordSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.number_picker_dialog, null);
                // Setup number picker
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(500);
                numberPicker.setWrapSelectorWheel(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setView(dialogView);

                builder.setTitle(getString(R.string.record_sale_dialog_title));
                builder.setPositiveButton(R.string.update_quantity_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Sets correct value
                        numberPicker.clearFocus();
                        ContentValues updateValues = new ContentValues();

                        int amountToSell = numberPicker.getValue();

                        if (mCurrentQuantity - amountToSell < 0) {
                            Toast.makeText(getApplicationContext(), "Insufficient quantity to complete order", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }

                        updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mCurrentQuantity - amountToSell);

                        // Multiple sale times 100 to get in int form
                        int newSalesAmount = (int) (mCurrentSales + (amountToSell * mCurrentPrice)) * 100;
                        updateValues.put(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL, newSalesAmount);

                        int rowsAffected = getContentResolver().update(mUri, updateValues, null, null);

                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Sale has been recorded", Toast.LENGTH_SHORT).show();
                            getContentResolver().notifyChange(mUri, null);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sale could not be recorded", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        getLoaderManager().initLoader(PREVIEW_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String[] projection = new String[]{
                ProductEntry._ID,
                ProductEntry.COLUUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PROCUCT_SALES_TOTAL,
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
            mCurrentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
            mCurrentPrice = CursorHelper.intToMoneyDecimal(cursor, cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
            mCurrentSales = CursorHelper.intToMoneyDecimal(cursor, cursor.getColumnIndex(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL));

            String priceString = CursorHelper.doubleToMoneyString(getApplicationContext(), mCurrentPrice);
            String quantityString = String.valueOf(mCurrentQuantity);
            String totalSalesString = CursorHelper.doubleToMoneyString(getApplicationContext(), mCurrentSales);

            mNameView.setText(name);
            mPriceView.setText(priceString);
            mQuantityView.setText(quantityString);
            mTotalSalesView.setText(totalSalesString);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameView.setText("");
        mPriceView.setText("");
        mQuantityView.setText("");
        mTotalSalesView.setText("");
    }
}

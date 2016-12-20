package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;

    // data variables
    private String mProductName;
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

    private ImageView mProductImageView;

    // Set up buttons
    private Button mReceiveShipmentButton;

    private Button mRecordSaleButton;

    private Button mOrderButton;

    // Number picker for dialog reuse
    private NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize TextViews
        mNameView = (TextView) findViewById(R.id.name_text_view);

        mPriceView = (TextView) findViewById(R.id.price_text_view);

        mQuantityView = (TextView) findViewById(R.id.quantity_text_view);

        mTotalSalesView = (TextView) findViewById(R.id.total_sales_text_view);

        mProductImageView = (ImageView) findViewById(R.id.product_image);

        // Initialize Buttons
        mReceiveShipmentButton = (Button) findViewById(R.id.shipment_button);

        mRecordSaleButton = (Button) findViewById(R.id.record_sale);

        mOrderButton = (Button) findViewById(R.id.order_button);

        // get data from intent

        mUri = getIntent().getData();

         if (mUri == null) {
            // Close the activity
            finish();
        }

        setTitle(R.string.product_detail);

        // On Click listener for Receiving shipments
        mReceiveShipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(getString(R.string.receive_dialog_title), R.string.update_quantity_button, receiveShipmentPositiveMethod);
            }
        });

        // On click for ordering more product
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Select quantity to order", R.string.update_quantity_button, orderPositiveMethod);
            }
        });

        // On Click listener for marking a sale
        mRecordSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Enter quantity sold", R.string.update_quantity_button, recordSalePositiveMethod);
            }
        });

        getLoaderManager().restartLoader(PREVIEW_LOADER, null, this);
    }

    /**
     * Creates a number picker dialog that requires the following parameters
     * @param title of the dialog
     * @param positiveButtonLabel - what is on the positive button
     * @param onClickMethod what to do when the user chooses the positive button
     */
    private void createDialog(String title, int positiveButtonLabel, Dialog.OnClickListener onClickMethod) {
        View dialogView = getLayoutInflater().inflate(R.layout.number_picker_dialog, null);
        // Setup number picker
        mNumberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(500);
        mNumberPicker.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
        builder.setView(dialogView);
        builder.setTitle(title);

        builder.setPositiveButton(positiveButtonLabel, onClickMethod);

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private Dialog.OnClickListener receiveShipmentPositiveMethod = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            ContentValues updateValues = new ContentValues();
            // clear focus tip from Dvd Franco
            // http://stackoverflow.com/questions/3691099/android-numberpicker-not-saving-edittext-changes
            mNumberPicker.clearFocus();
            updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mNumberPicker.getValue() + mCurrentQuantity);

            int rowsAffected = getContentResolver().update(mUri, updateValues, null, null);

            if (rowsAffected > 0) {
                Toast.makeText(getApplicationContext(), "Quantity has been updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Quantity could not be updated", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        }
    };

    private Dialog.OnClickListener orderPositiveMethod = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(getApplicationContext(), "Clicked positive button", Toast.LENGTH_SHORT).show();

            // Create email intent
            String[] addresses = new String[] {
                    getString(R.string.order_email_address)
            };

            String emailBody = "Product: " + mProductName +
                    "\nOrder Qty: " + mNumberPicker.getValue();

            composeEmail(addresses, getString(R.string.order_subject), emailBody);
        }
    };

    private Dialog.OnClickListener recordSalePositiveMethod = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            mNumberPicker.clearFocus();
            ContentValues updateValues = new ContentValues();

            int amountToSell = mNumberPicker.getValue();

            if (mCurrentQuantity - amountToSell < 0) {
                Toast.makeText(getApplicationContext(), "Insufficient quantity to complete order", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, mCurrentQuantity - amountToSell);

            // Multiple sale times 100 to get in int form
            double newSalesAmount = (double) (mCurrentSales + (amountToSell * mCurrentPrice)) * 100;
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
    };

    /**
     * creates and opens an email intent for orders
     * @param addresses recipients
     * @param subject line
     * @param body the main email body
     */
    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        // http://stackoverflow.com/questions/27528236/mailto-android-unsupported-action-error
        // Handle unavailable or non-registered email account on system from Sam @ stackoverflow
        ComponentName emailApp = intent.resolveActivity(getPackageManager());
        ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
        boolean hasEmailApp = emailApp != null && !emailApp.equals(unsupportedAction);
        if (hasEmailApp) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No registered email client available", Toast.LENGTH_SHORT).show();
        }
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
            mProductName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUUMN_PRODUCT_NAME));
            mCurrentQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
            mCurrentPrice = CursorHelper.intToDecimal(cursor, cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
            mCurrentSales = CursorHelper.intToDecimal(cursor, cursor.getColumnIndex(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL));

            String priceString = CursorHelper.doubleToMoneyString(getApplicationContext(), mCurrentPrice);
            String quantityString = String.valueOf(mCurrentQuantity);
            String totalSalesString = CursorHelper.doubleToMoneyString(getApplicationContext(), mCurrentSales);

            // Get Image if available
            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE));
            Bitmap productImage = ImageHelper.convertBlobToBitmap(imageByteArray);

            // set Image
            mProductImageView.setImageBitmap(productImage);

            mNameView.setText(mProductName);
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

    // Menu setup
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mUri == null) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.action_edit:
                // open editor activity
                Intent intent = new Intent(this, EditorActivity.class);
                intent.setData(mUri);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}

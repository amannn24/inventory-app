package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by Chris on 11/28/2016.
 */

public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link #ProductCursorAdapter}
     * @param context
     * @param c       the cursor that holds the data
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);

        // Get column indices
        int idIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameIndex = cursor.getColumnIndex(ProductEntry.COLUUMN_PRODUCT_NAME);
        int priceIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int salesTotalIndex =  cursor.getColumnIndex(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL);

        // Record a sale button
        ImageButton recordSaleButton = (ImageButton) view.findViewById(R.id.record_sale_button);

        // Get data from cursor
        int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        final double price = CursorHelper.intToDecimal(cursor, priceIndex);
        final double salesTotal = CursorHelper.intToDecimal(cursor, salesTotalIndex);

        // String formatting for text views
        String formattedPrice = CursorHelper.doubleToMoneyString(context, price);
        final int quantity = cursor.getInt(quantityIndex);
        final String quantityString = Integer.toString(quantity);

        final Uri uri = Uri.parse(ProductEntry.CONTENT_URI + "/" + id);

        // Create popup dialog to record a sale on click
        recordSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.number_picker_dialog, null);
                // Setup number picker
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.number_picker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(500);
                numberPicker.setWrapSelectorWheel(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                builder.setTitle(context.getString(R.string.record_sale_dialog_title));
                builder.setPositiveButton(R.string.update_quantity_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // Sets correct value
                        numberPicker.clearFocus();
                        ContentValues updateValues = new ContentValues();

                        int amountToSell = numberPicker.getValue();

                        if (quantity - amountToSell < 0) {
                            Toast.makeText(context, "Insufficient quantity to complete order", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }

                        updateValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - amountToSell);

                        // Multiple sale times 100 to get in int form
                        double newSalesAmount = (double) (salesTotal + (amountToSell * price)) * 100;
                        updateValues.put(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL, newSalesAmount);

                        int rowsAffected = context.getContentResolver().update(uri, updateValues, null, null);

                        if (rowsAffected > 0) {
                            Toast.makeText(context, "Sale has been recorded", Toast.LENGTH_SHORT).show();
                            context.getContentResolver().notifyChange(uri, null);
                        } else {
                            Toast.makeText(context, "Sale could not be recorded", Toast.LENGTH_SHORT).show();
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

        nameTextView.setText(name);
        priceTextView.setText(formattedPrice);
        quantityTextView.setText(quantityString);
    }
}

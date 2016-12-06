package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by Chris on 11/28/2016.
 */

public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link #ProductCursorAdapter}
     *
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
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);

        int nameIndex = cursor.getColumnIndex(ProductEntry.COLUUMN_PRODUCT_NAME);
        int priceIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        double salesTotal = CursorHelper.intToMoneyDecimal(cursor, cursor.getColumnIndex(ProductEntry.COLUMN_PROCUCT_SALES_TOTAL));

        // Record a sale button
        Button recordSaleButton = (Button) view.findViewById(R.id.record_sale_button);

        String name = cursor.getString(nameIndex);

        // Get the price back to decimal format with zeros padding
        String formattedPrice = CursorHelper.intToMoneyString(context, cursor, priceIndex);

        String quantity = Integer.toString(cursor.getInt(quantityIndex));

        nameTextView.setText(name);
        priceTextView.setText(formattedPrice);
        quantityTextView.setText(quantity);
    }
}

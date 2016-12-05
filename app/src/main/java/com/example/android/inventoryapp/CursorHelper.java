package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;

import java.util.Locale;

/**
 * Created by Chris on 11/30/2016.
 */

public class CursorHelper {
    /**
     * @param cursor cursor with a double stored inside
     * @param index the column index
     * @return cursor double divided by 100 to get decimal places
     */
    public static final Double intToMoneyDecimal(Cursor cursor, int index) {
        return cursor.getDouble(index) / 100;
    }

    /**
     *
     * @param context used to get string formatting resource
     * @param cursor the data
     * @param index the column index of the price in the cursor
     * @return a formatted money string
     */
    public static final String intToMoneyString(Context context, Cursor cursor, int index) {
        double priceDouble = cursor.getDouble(index) / 100;
        String price = String.format(Locale.US, "%1.2f", priceDouble);
        return String.format(context.getString(R.string.price_text_view), price);
    }
}

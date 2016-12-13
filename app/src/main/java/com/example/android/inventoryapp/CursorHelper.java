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
    public static final Double intToDecimal(Cursor cursor, int index) {
        return cursor.getDouble(index) / 100;
    }

    /**
     * @param value as a double
     * @return decimal to 2 places: 2 => 2.00
     */
    public static final String doubleToDecimalString(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    /**
     *
     * @param context used to get string formatting resource
     * @param priceDouble price to be formatted
     * @return a formatted money string: 5.4 => $5.40
     */
    public static final String doubleToMoneyString(Context context, double priceDouble) {
        String price = doubleToDecimalString(priceDouble);
        return String.format(context.getString(R.string.money_text_view), price);
    }


}

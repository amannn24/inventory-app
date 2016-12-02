package com.example.android.inventoryapp;

import android.database.Cursor;

/**
 * Created by Chris on 11/30/2016.
 */

public class CursorHelper {
    /**
     * @param cursor cursor with a double stored inside
     * @param index the column index
     * @return string formatted to 2 decimal places
     */
    public static final String intToMoneyString(Cursor cursor, int index) {
        return Double.toString(cursor.getDouble(index) / 100);
    }
}

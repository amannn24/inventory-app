package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Chris on 11/19/2016.
 */

public class ProductContract {

    // Private constructor to prevent instantiation
    private ProductContract() {

        class ItemEntry implements BaseColumns {

            // Setup table constants
            public static final String TABLE_NAME = "items";

            // Setup column constants
            /**
             * name (TEXT NOT NULL)
             * price (INTEGER NOT NULL)
             * quantity (INTEGER DEFAULT 0)
             * supplier_id - id (INTEGER NOT NULL)
             * image - string of location for uri (TEXT)
             */
            public static final String COLUUMN_PRODUCT_NAME = "name";
            public static final String COLUMN_PRODUCT_PRICE = "price";
            public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
            public static final String COLUMN_PRODUCT_SUPPLIER_ID = "supplier_id";
            public static final String COLUMN_PRODUCT_IMAGE = "image";

        }
    }

}

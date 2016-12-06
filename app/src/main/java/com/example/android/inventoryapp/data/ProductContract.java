package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class for creating a products table and the URIs for
 * a content provider
 */

public class ProductContract {

    // Private constructor to prevent instantiation
    private ProductContract() {

    }

    // URI constants
    public static final String CONTENT_AUTHORITY = "com.example.android.products";

    // Selected table in uri
    public static final String PRODUCTS_PATH = "products";

    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // The MIME type of the {@link #CONTENT_URI} for a list of products
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCTS_PATH;

    // The MIME type of the {@link #CONTENT_URI} for a single product
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCTS_PATH;

    public static final class ProductEntry implements BaseColumns {

        // Standard content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PRODUCTS_PATH);

        // Setup table constants
        public static final String TABLE_NAME = "products";

        // Setup column constants
        /**
         * name (TEXT NOT NULL)
         * price (INTEGER NOT NULL)
         * quantity (INTEGER DEFAULT 0)
         * image - string of location for uri (TEXT)
         */

        public static final String _ID = BaseColumns._ID;
        public static final String COLUUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PROCUCT_SALES_TOTAL = "sales";
        public static final String COLUMN_PRODUCT_IMAGE = "image";
    }
}

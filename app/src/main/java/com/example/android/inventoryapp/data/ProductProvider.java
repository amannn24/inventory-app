package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Chris on 11/21/2016.
 */

public class ProductProvider extends ContentProvider {

    // Reference to database helper class
    private ProductDbHelper mDbHelper;

    // For uri match of list of products in the products table
    private static final int PRODUCTS = 100;

    // For uri match of single product in the products table
    private static final int PRODUCT_ID = 101;

    // Creates a matcher that will handle different URIs
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Will be called as soon as anthing from this class is called
    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCTS_PATH, PRODUCTS);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCTS_PATH + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContract.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri + " with match: " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}

package com.squareup.picasso3.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.RestrictTo;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import androidx.annotation.Nullable;

@RestrictTo(LIBRARY)
public final class PicassoContentProvider extends ContentProvider {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    @Override
    public boolean onCreate() {
        context = getContext();
        return true;
    }

    @Override
    public Cursor query(@Nullable() Uri uri, @Nullable() String[] projection, @Nullable() String selection, @Nullable() String[] selectionArgs, @Nullable() String sortOrder) {
        return null;
    }

    @Override
    public String getType(@Nullable() Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@Nullable() Uri uri, @Nullable() ContentValues values, @Nullable() String selection, @Nullable() String[] selectionArgs) {
        return 0;
    }
}

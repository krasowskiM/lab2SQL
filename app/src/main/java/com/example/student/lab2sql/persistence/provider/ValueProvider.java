package com.example.student.lab2sql.persistence.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.student.lab2sql.activity.ActivitySql;
import com.example.student.lab2sql.persistence.DbHelper;

public class ValueProvider extends ContentProvider {
    private static final String ID = "com.example.student.lab2sql.persistence.provider.ValueProvider";
    private static final int WHOLE_TABLE = 1;
    private static final int CHOSEN_ROW = 2;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private DbHelper dbHelper;

    public static final Uri CONTENT_URI = Uri.parse("content://" + ID + "/" + DbHelper.TABLE_NAME);

    static {
        URI_MATCHER.addURI(ID, DbHelper.TABLE_NAME, WHOLE_TABLE);
        URI_MATCHER.addURI(ID, DbHelper.TABLE_NAME + "/#", CHOSEN_ROW);
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        long addedId = 0;
        switch (uriType) {
            case WHOLE_TABLE:
                addedId = writableDatabase.insert(
                        DbHelper.TABLE_NAME,
                        null,
                        contentValues
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DbHelper.TABLE_NAME + "/" + addedId);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriType) {
            case WHOLE_TABLE:
                cursor = writableDatabase.query(
                        false,
                        DbHelper.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1,
                        null,
                        null);
                break;
            case CHOSEN_ROW:
                cursor = writableDatabase.query(false, DbHelper.TABLE_NAME,
                        strings, addIdToSelection(s, uri), strings1,
                        null, null, s1, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        int deletedCount = 0;
        switch (uriType) {
            case WHOLE_TABLE:
                deletedCount = writableDatabase.delete(
                        DbHelper.TABLE_NAME,
                        s,
                        strings);
                break;
            case CHOSEN_ROW:
                deletedCount = writableDatabase.delete(
                        DbHelper.TABLE_NAME,
                        addIdToSelection(s, uri),
                        strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        int updatedCount;
        switch (uriType){
            case WHOLE_TABLE:
                updatedCount = writableDatabase.update(
                        DbHelper.TABLE_NAME,
                        contentValues,
                        s,
                        strings);
                break;
            case CHOSEN_ROW:
                updatedCount = writableDatabase.update(
                        DbHelper.TABLE_NAME,
                        contentValues,
                        addIdToSelection(s, uri),
                        strings);
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedCount;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private String addIdToSelection(String selection, Uri uri) {
        if (selection != null && !selection.isEmpty()) {
            selection = selection + " and " + DbHelper.ID + "="
                    + uri.getLastPathSegment();
        } else {
            selection = DbHelper.ID + "=" + uri.getLastPathSegment();
        }
        return selection;
    }
}

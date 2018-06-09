package com.example.student.lab2sql.activity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.student.lab2sql.R;
import com.example.student.lab2sql.persistence.DbHelper;
import com.example.student.lab2sql.persistence.provider.ValueProvider;

import java.util.SimpleTimeZone;

public class ActivitySql extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter cursorAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        listView = findViewById(R.id.values_list);
        fillListView();

        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValue();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DbHelper.ID, DbHelper.COLUMN_1};
        return new CursorLoader(this, ValueProvider.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void addValue() {
        ContentValues values = new ContentValues();
        EditText valueEditText = findViewById(R.id.value_edit);
        values.put(DbHelper.COLUMN_1, valueEditText.getText().toString());
        Uri newUri = getContentResolver().insert(ValueProvider.CONTENT_URI, values);
    }

    private void fillListView() {
        getLoaderManager().initLoader(0, null, this);
        String[] mappingFrom = new String[]{DbHelper.COLUMN_1};
        int[] mappingTo = new int[]{R.id.value};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, null,
                mappingFrom, mappingTo, 0);
        listView.setAdapter(cursorAdapter);
    }
}

package com.example.userdatabaseexample.cp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import com.example.userdatabaseexample.R;

public class CPActivity extends FragmentActivity {

  public final static Uri CONTACT_URI = Uri.parse("content://ru.startandroid.providers.AdressBook/contacts");
  public final static int CONTACT_LOADER_ID = 1;

  final String CONTACT_NAME = "name";
  final String CONTACT_EMAIL = "email";

  private SimpleCursorAdapter adapter;

  private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      if (id == CONTACT_LOADER_ID) {
        return new CursorLoader(getApplicationContext(), CONTACT_URI, null, null, null, null);
      }
      return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
      if (loader.getId() == CONTACT_LOADER_ID) {
        adapter.swapCursor(data);
      }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cp);

    getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, null, loaderCallbacks); // should be called in onCreate()

    String from[] = {"name", "email"};
    int to[] = {android.R.id.text1, android.R.id.text2};
    adapter = new SimpleCursorAdapter(
        this, android.R.layout.simple_list_item_2, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    ListView lvContact = (ListView) findViewById(R.id.lvContact);
    lvContact.setAdapter(adapter);

    getSupportLoaderManager().getLoader(CONTACT_LOADER_ID).startLoading();
  }

  public void onClickInsert(View v) {
    ContentValues cv = new ContentValues();
    cv.put(CONTACT_NAME, "name");
    cv.put(CONTACT_EMAIL, "email");
    Uri newUri = getContentResolver().insert(CONTACT_URI, cv);

    long id = ContentUris.parseId(newUri);
    cv.put(CONTACT_NAME, "name " + id);
    cv.put(CONTACT_EMAIL, "email " + id);
    getContentResolver().update(newUri, cv, null, null);
  }

  public void onClickDelete(View v) {
    Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
    if ((cursor == null) || (cursor.getCount() == 0)) {
      return;
    }
    cursor.moveToLast();
    long lastId = cursor.getLong(cursor.getColumnIndex("_id"));
    Uri uri = ContentUris.withAppendedId(CONTACT_URI, lastId);
    getContentResolver().delete(uri, null, null);
  }

}

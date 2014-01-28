package com.example.userdatabaseexample.cp;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.userdatabaseexample.R;
import com.example.userdatabaseexample.util.Lists;

import java.util.List;

public class CPActivity extends Activity {

  final String LOG_TAG = "myLogs";

  final Uri CONTACT_URI = Uri.parse("content://ru.startandroid.providers.AdressBook/contacts");

  final String CONTACT_NAME = "name";
  final String CONTACT_EMAIL = "email";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cp);

    Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
        null, null);
    startManagingCursor(cursor);

    String from[] = {"name", "email"};
    int to[] = {android.R.id.text1, android.R.id.text2};

    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);

    ListView lvContact = (ListView) findViewById(R.id.lvContact);
    lvContact.setAdapter(adapter);
  }

  public void onClickInsert(View v) {
    ContentValues cv = new ContentValues();
    cv.put(CONTACT_NAME, "name 4");
    cv.put(CONTACT_EMAIL, "email 4");
    Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
    Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
  }

  public void onClickUpdate(View v) {
    ContentValues cv = new ContentValues();
    cv.put(CONTACT_NAME, "name 5");
    cv.put(CONTACT_EMAIL, "email 5");
    Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
    int cnt = getContentResolver().update(uri, cv, null, null);
    Log.d(LOG_TAG, "update, count = " + cnt);
  }

  public void onClickDelete(View v) {
    Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
    int cnt = getContentResolver().delete(uri, null, null);
    Log.d(LOG_TAG, "delete, count = " + cnt);
  }

  public void onClickError(View v) {
    Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
    try {
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    } catch (Exception ex) {
      Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
    }

  }

  public void btnGetAllClick(View v) {
    List<User> users = Lists.newArrayList();
    String columns[] = {"_id", "name", "email"};
    Cursor cursor = getContentResolver().query(CONTACT_URI, columns, null, null, "_id ASC");
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      String name = cursor.getString(cursor.getColumnIndex("name"));
      String email = cursor.getString(cursor.getColumnIndex("email"));
      long id = cursor.getLong(cursor.getColumnIndex("_id"));
      users.add(new User(name, email, id));
      cursor.moveToNext();
    }
    Log.i(LOG_TAG, users.toString());
  }

  private class User {
    private String name;
    private String email;
    private long id;

    private User(String name, String email, long id) {
      this.name = name;
      this.email = email;
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public String getEmail() {
      return email;
    }

    public long getId() {
      return id;
    }

    @Override
    public String toString() {
      return "User{" +
          "id=" + id +
          ", email='" + email + '\'' +
          ", name='" + name + '\'' +
          '}';
    }
  }

}

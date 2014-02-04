package com.example.userdatabaseexample.cp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public class MyLoader extends AsyncTaskLoader<Cursor> {

  final ForceLoadContentObserver mObserver;

  public MyLoader(Context context) {
    super(context);
    mObserver = new ForceLoadContentObserver();
  }

  @Override
  public Cursor loadInBackground() {
    MyContactsProvider.DBHelper dbHelper = new MyContactsProvider.DBHelper(getContext());
    Cursor cursor = dbHelper.getReadableDatabase().query("contacts", null, null, null, null, null, "_id ASC");
    cursor.setNotificationUri(getContext().getContentResolver(), CPActivity.CONTACT_URI);
    if (cursor != null) {
      // Ensure the cursor window is filled
      cursor.getCount();
      cursor.registerContentObserver(mObserver);
    }
    return cursor;
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }
}

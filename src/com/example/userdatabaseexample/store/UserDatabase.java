package com.example.userdatabaseexample.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import com.example.userdatabaseexample.mapper.CursorMapper;
import com.example.userdatabaseexample.models.Place;
import com.example.userdatabaseexample.models.User;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class UserDatabase extends SQLiteOpenHelper {
  private static final String USER_TABLE = "user";
  private static final String PLACE_TABLE = "place";
  private static final CursorMapper<User> userMapper = CursorMapper.create(User.class);
  private static final CursorMapper<Place> placeMapper = CursorMapper.create(Place.class);

  private static final int CURRENT_VERSION = 3;
  private static final String DATABASE_NAME = "user.db";

  private static UserDatabase singleton;

  private UserDatabase(Context context) {
    super(context, DATABASE_NAME, null, CURRENT_VERSION);
  }

  public synchronized static UserDatabase get(Context context) {
    if (singleton == null) {
      singleton = new UserDatabase(context.getApplicationContext());
    }
    return singleton;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    onUpgrade(sqLiteDatabase, 0, CURRENT_VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int currentVersion, int newVersion) {
    for (int version = currentVersion + 1; version <= newVersion; version++) {
      if (version == 1) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + USER_TABLE + " (" +
                "_id VARCHAR(40), " +
                "name VARCHAR(40), " +
                "second_name VARCHAR(40), " +
                "created_at DATETIME NOT NULL " +
                ")");
      }
      if (version == 2) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + PLACE_TABLE + " (" +
                "_id VARCHAR(40), " +
                "description TEXT " +
                ")");
      }
      if (version == 3) {
        sqLiteDatabase.execSQL(
            "ALTER TABLE " + PLACE_TABLE + " " +
                "ADD COLUMN score INTEGER DEFAULT 0");
        // there can be something like:
        // moveDataFromOneTableToAnotherTable(SQLiteDatabase db)
      }
    }
  }

  public SQLiteDatabase getReadableDatabase() {
    try {
      return super.getReadableDatabase();
    } catch (SQLiteException e) {
      // This might happen because of a stupid reason:
      // "Can't upgrade read-only database from version 8 to 9".
      // So open it as writable so it can be upgraded and then
      // reopen it as read-only.
      getWritableDatabase().close();
      return super.getReadableDatabase();
    }
  }

  public static String formatDate(Date date) {
    return getDateFormat().format(date);
  }

  public static Date parseDate(String date) {
    try {
      return getDateFormat().parse(date);
    } catch (ParseException e) {
      return new Date();
    }
  }

  private static SimpleDateFormat getDateFormat() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    dateFormat.setLenient(false);
    return dateFormat;
  }

  public static byte[] asByteArray(UUID uuid) {
    byte[] bytes = new byte[16];
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    // iOS is Little Endian.
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.putLong(uuid.getMostSignificantBits());
    buffer.putLong(uuid.getLeastSignificantBits());
    return buffer.array();
  }

  public static String uuidAsString() {
    return Base64.encodeToString(
        asByteArray(UUID.randomUUID()),
        Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
  }

  public void addUser(User user) {
    ContentValues values = new ContentValues();
    String now = formatDate(new Date());
    values.put("_id", uuidAsString());
    values.put("name", user.getName());
    values.put("second_name", user.getSecondName());
    values.put("created_at", now);
    getWritableDatabase().insert(USER_TABLE, null, values);
  }

  public User getUser(String id) {
    Cursor cursor = getReadableDatabase().query(
        USER_TABLE,
        new String[]{"_id"},
        "_id = ?",
        new String[]{id},
        null /* group by */,
        null /* having */,
        null /* order by */);
    return userMapper.mapSingle(cursor);
  }

  public List<User> getAllUsers() {
    Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + USER_TABLE, null);
    return userMapper.map(cursor);
  }

  public void removeUser(User user){
    getWritableDatabase().delete(
        USER_TABLE,
        "_id = ?",
        new String[]{user.getId()});
  }

  public void removeAllUsers(){
    getWritableDatabase().delete(USER_TABLE, null, null);
  }

  public void addPlace(Place place) {
    ContentValues values = new ContentValues();
    values.put("_id", uuidAsString());
    values.put("description", place.getDescriprion());
    values.put("score", place.getScore());
    getWritableDatabase().insert(PLACE_TABLE, null, values);
  }

  public Place getPlace(String id) {
    Cursor cursor = getReadableDatabase().query(
        USER_TABLE,
        new String[]{"_id"},
        "_id = ?",
        new String[]{id},
        null /* group by */,
        null /* having */,
        null /* order by */);
    return placeMapper.mapSingle(cursor);
  }

  public void removePlace(Place place){
    getWritableDatabase().delete(
        PLACE_TABLE,
        "_id = ?",
        new String[]{place.getId()});
  }

  public void removeAllPlaces(){
    getWritableDatabase().delete(PLACE_TABLE, null, null);
  }

  public List<Place> getAllPlaces() {
    Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + PLACE_TABLE, null);
    return placeMapper.map(cursor);
  }

}

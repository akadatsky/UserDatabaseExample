package com.example.userdatabaseexample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.userdatabaseexample.cp.CPActivity;
import com.example.userdatabaseexample.models.Place;
import com.example.userdatabaseexample.models.User;
import com.example.userdatabaseexample.store.UserDatabase;

import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

  private UserDatabase userDatabase;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    userDatabase = UserDatabase.get(this);

    Button openCp = (Button) findViewById(R.id.openCp);
    openCp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, CPActivity.class));
      }
    });

    Button makeDb = (Button) findViewById(R.id.makeDb);
    makeDb.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new DatabaseWorkTask().execute();
      }
    });

    Button clear = (Button) findViewById(R.id.clear);
    clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new ClearTablesTask().execute();
      }
    });

  }

  private class DatabaseWorkTask extends AsyncTask<Void, Void, Void> {

    private List<User> users;
    private List<Place> places;

    @Override
    protected Void doInBackground(Void... params) {

      Random random = new Random();

      userDatabase.addUser(new User("aaa" + random.nextInt(1000), "bbb" + random.nextInt(1000)));
      users = userDatabase.getAllUsers();

      userDatabase.addPlace(new Place("some description_" + random.nextInt(1000), random.nextInt(100)));
      places = userDatabase.getAllPlaces();
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      ListView usersView = (ListView) findViewById(R.id.users);
      usersView.setAdapter(
          new ArrayAdapter<User>(MainActivity.this, android.R.layout.simple_list_item_1, users));
    }
  }

  private class ClearTablesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      userDatabase.removeAllPlaces();
      userDatabase.removeAllUsers();
      return null;
    }
  }

}

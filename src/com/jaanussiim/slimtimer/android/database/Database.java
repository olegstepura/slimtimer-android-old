/*
 * Copyright 2011 JaanusSiim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaanussiim.slimtimer.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

enum SettingKey {
  KEY_USERNAME(1), KEY_PASSWORD(2);

  private final Integer index;

  SettingKey(Integer index) {
    this.index = index;
  }

  public Integer getIndex() {
    return index;
  }
}

public class Database {
  private static final String T = "Database";

  private static final String DATABASE_NAME = "m_timer_database";
  private static final int DATABASE_VERSION = 3;

  private final Context ctx;
  private DatabaseHelper databaseHelper;
  private SQLiteDatabase database;

  private static class DatabaseHelper extends SQLiteOpenHelper {
    private Context ctx;

    DatabaseHelper(final Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      ctx = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
      Log.d(T, "onCreate()");
      onUpgrade(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // run migration scripts in (oldVersion, newVersion] in order,
      // i.e., calling with 0,2 runs
      // 001.sql, 002.sql
      // calling with 5,6 runs
      // 006.sql
      for (int i = oldVersion + 1; i <= newVersion; i++) {
        // %03%d = 001, 002, ..., 010, ..., 099, 100, 101, 102, ..., 999
        executeScript(db, String.format("db/%03d.sql", i));
      }
    }

    private void executeScript(SQLiteDatabase db, String filename) {
      Log.d(T, "Executing SQL script " + filename);
      try {
        InputStream is = ctx.getAssets().open(filename);
        ByteArrayOutputStream os = new ByteArrayOutputStream(512);

        int chr;
        while ((chr = is.read()) != -1) {
          if (((char) chr) == ';') {
            db.execSQL(os.toString("utf-8"));
            os.reset();
          } else {
            os.write(chr);
          }
        }
      } catch (IOException e) {
        Log.d(T, "SQL execute failed with IOE: " + e);
      }
    }
  }

  public Database(final Context ctx) {
    this.ctx = ctx;
  }

  public void open() {
    if (databaseHelper != null) {
      return;
    }

    databaseHelper = new DatabaseHelper(ctx);
    database = databaseHelper.getWritableDatabase();
  }

  public void close() {
    databaseHelper.close();
  }

  ////////////////////////////////////////////////////////////
  //Credentials handling
  ////////////////////////////////////////////////////////////
  public void putCredentials(String username, String password) {
    putSetting(SettingKey.KEY_USERNAME, username);
    putSetting(SettingKey.KEY_PASSWORD, password);
  }

  public String getUsername() {
    return loadSetting(SettingKey.KEY_USERNAME).getValue();
  }

  public String getPassword() {
    return loadSetting(SettingKey.KEY_PASSWORD).getValue();
  }

  ////////////////////////////////////////////////////////////
  //Settings handling
  ////////////////////////////////////////////////////////////
  private void putSetting(SettingKey key, String value) {
    SettingModel setting = loadSetting(key);
    if (setting == null) {
      setting = new SettingModel(key, value);
    }
    setting.setValue(value);
    persistSetting(setting);
  }

  private void persistSetting(SettingModel setting) {
    final ContentValues values = new ContentValues();
    values.put(SettingModel.COLUMN_KEY, setting.getKeyIndex());
    values.put(SettingModel.COLUMN_VALUE, setting.getValue());
    long id = database.insert(SettingModel.TABLE_NAME, null, values);
    assert id != -1;
  }

  private SettingModel loadSetting(SettingKey key) {
    final String[] selectKeys = new String[] { SettingModel.COLUMN_VALUE};
    final Cursor c = database.query(SettingModel.TABLE_NAME, selectKeys, "setting_key = ?", new String[]{Integer.toString(key.getIndex())}, null, null, null);
    if (c == null) {
      return new SettingModel(key, null);
    }

    String value = null;
    if (c.moveToFirst()) {
      value = getString(c, SettingModel.COLUMN_VALUE);
    }
    c.close();

    return new SettingModel(key, value);
  }

  ////////////////////////////////////////////////////////////
  //Generic database access
  ////////////////////////////////////////////////////////////
  private String getString(Cursor c, String columnName) {
    final int index = c.getColumnIndex(columnName);

    if (index == -1) {
      return null;
    }

    return c.getString(index);
  }
}

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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseBase {
  private static final String T = "DatabaseBase";
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

  public DatabaseBase(final Context ctx) {
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

  public SQLiteDatabase getDatabase() {
    return database;
  }

  public String getString(Cursor c, String columnName) {
    final int index = c.getColumnIndex(columnName);

    if (index == -1) {
      return null;
    }

    return c.getString(index);
  }
}

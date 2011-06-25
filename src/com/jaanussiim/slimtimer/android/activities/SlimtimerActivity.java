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

package com.jaanussiim.slimtimer.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.jaanussiim.slimtimer.android.database.Database;

import static com.jaanussiim.slimtimer.android.Constants.*;

public class SlimtimerActivity extends Activity {
  private static Database databaseInstance;
  private Database database;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (database == null) {
      database = getSharedDatabase(this);
    }

    database.open();
    moveUsernamePassword(database);

    Intent nextActivityStart = null;

    if (database.hasCredentials()) {

    } else {
      nextActivityStart = new Intent(SlimtimerActivity.this, LoginActivity.class);
    }

    startActivity(nextActivityStart);
    finish();
  }

  //TODO jaanus: check this. I don't like static
  public static Database getSharedDatabase(Context ctx) {
    if (databaseInstance == null) {
      databaseInstance = new Database(ctx);
    }

    return databaseInstance;
  }

  void moveUsernamePassword(Database database) {
    SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    final String username = preferences.getString(PREFERENCES_EMAIL_KEY, "");
    final String password = preferences.getString(PREFERENCES_PASSWORD_KEY, "");

    if ("".equals(username)) {
      return;
    }

    database.putCredentials(username, password);

    SharedPreferences.Editor editor = preferences.edit();
    editor.remove(PREFERENCES_EMAIL_KEY);
    editor.remove(PREFERENCES_PASSWORD_KEY);
    editor.commit();
  }

  void setTestDatabase(Database database) {
    this.database = database;
  }
}

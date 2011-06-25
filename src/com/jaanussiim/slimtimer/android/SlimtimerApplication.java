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

package com.jaanussiim.slimtimer.android;

import android.app.Application;
import android.util.Log;
import com.jaanussiim.slimtimer.android.database.Database;

public class SlimtimerApplication extends Application {
  private static final String T = "SlimtimerApplication";
  private Database database;

  @Override
  public void onCreate() {
    Log.d(T, "onCreate");
    database = new Database(this);
    database.open();
  }

  @Override
  public void onTerminate() {
    Log.d(T, "onTerminate");
    database.close();
  }

  public Database getDatabase() {
    return database;
  }
}

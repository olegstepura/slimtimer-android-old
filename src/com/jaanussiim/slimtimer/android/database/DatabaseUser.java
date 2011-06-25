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

public class DatabaseUser extends DatabaseSettings {
  public DatabaseUser(final Context ctx) {
    super(ctx);
  }

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

}

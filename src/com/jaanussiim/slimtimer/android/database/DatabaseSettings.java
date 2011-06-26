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

import static com.jaanussiim.slimtimer.android.database.Setting.COLUMN_KEY;
import static com.jaanussiim.slimtimer.android.database.Setting.COLUMN_VALUE;
import static com.jaanussiim.slimtimer.android.database.Setting.TABLE_NAME;

public class DatabaseSettings extends DatabaseBase {
  public DatabaseSettings(final Context ctx) {
    super(ctx);
  }

  protected void putBooleanSetting(SettingKey key, boolean value) {
    putStringSetting(key, Boolean.toString(value));
  }

  protected void putStringSetting(SettingKey key, String value) {
    Setting setting = loadSetting(key);
    if (setting == null) {
      setting = new Setting(key, value);
    }
    setting.setValue(value);
    persistSetting(setting);
  }

  private void persistSetting(Setting setting) {
    final ContentValues values = new ContentValues();
    values.put(COLUMN_KEY, setting.getKeyIndex());
    values.put(COLUMN_VALUE, setting.getValue());
    getDatabase().insert(TABLE_NAME, null, values);
  }

  protected void removeSetting(SettingKey key) {
    getDatabase().delete(TABLE_NAME, COLUMN_KEY + " = ?", new String[]{Long.toString(key.getIndex())});
  }

  protected Setting loadSetting(SettingKey key) {
    final String[] selectKeys = new String[]{COLUMN_VALUE};
    final Cursor c = getDatabase().query(TABLE_NAME, selectKeys, "setting_key = ?", new String[]{Integer.toString(key.getIndex())}, null, null, null);
    if (c == null) {
      return new Setting(key, null);
    }

    String value = null;
    if (c.moveToFirst()) {
      value = getString(c, COLUMN_VALUE);
    }
    c.close();

    return new Setting(key, value);
  }

}

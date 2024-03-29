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

public class Setting {
  public static final String TABLE_NAME = "settings";
  public static final String COLUMN_KEY = "setting_key";
  public static final String COLUMN_VALUE = "setting_value";
  private SettingKey key;
  private String value;

  public Setting(SettingKey key, String value) {
    this.key = key;
    this.value = value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public Integer getKeyIndex() {
    return key.getIndex();
  }

  public boolean getBooleanValue() {
    if (value == null) {
      return false;
    }

    return Boolean.parseBoolean(value);
  }
}

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

import android.app.Activity;
import com.jaanussiim.slimtimer.android.testutils.DatabaseHelper;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DatabaseSettingsTest {
  private DatabaseHelper database;

  @Before
  public void setUp() {
    database = new DatabaseHelper();
    database.open();
  }

  @Test
  public void settingKeyIndexesUnique() {
    List<Integer> indexes = new ArrayList<Integer>();
    List<Integer> duplicates = new ArrayList<Integer>();
    for (SettingKey key : SettingKey.values()) {
      Integer index = key.getIndex();
      if (indexes.contains(index)) {
        duplicates.add(index);
      } else {
        indexes.add(index);
      }
    }

    final Integer[] duplicatesArray = duplicates.toArray(new Integer[duplicates.size()]);
    assertArrayEquals("Found duplicate setting key definitions: " + Arrays.toString(duplicatesArray), new Integer[0], duplicatesArray);
  }

  @Test
  public void stringValueHandling() {
    final String testUsername = "username";
    final String testPassword = "password";
    database.putCredentials(testUsername, testPassword);

    assertEquals("Should have loaded saved username", testUsername, database.getUsername());
    assertEquals("Should have loaded saved password", testPassword, database.getPassword());
  }

  @Test
  public void onUsernameValueNullReturned() {
    assertNull("Should have had null", database.getUsername());
  }

  @After
  public void tearDown() {
    database.close();
  }
}

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

import android.content.Context;
import android.content.SharedPreferences;
import com.jaanussiim.slimtimer.android.database.Database;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jaanussiim.slimtimer.android.Constants.*;
import static com.jaanussiim.slimtimer.android.testutils.ActivityTestUtils.namedActivityPushed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class SlimtimerActivityTest {
  private SlimtimerActivity slimtimerActivity;
  private Database database;

  @Before
  public void setUp() {
    slimtimerActivity = new SlimtimerActivity();
    database = new Database(slimtimerActivity);
    database.open();
    slimtimerActivity.setTestDatabase(database);
  }

  @Test
  public void previousUsernamePasswordRemovedFromPreferences() {
    final String testUsername = "this.is.my.username@somewhere.com";
    final String testPassword = "this.is.my.password";

    SharedPreferences preferences = slimtimerActivity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();
    editor.putString(PREFERENCES_EMAIL_KEY, testUsername);
    editor.putString(PREFERENCES_PASSWORD_KEY, testPassword);
    editor.commit();

    slimtimerActivity.onCreate(null);

    assertEquals("Username should be removed from preferences", "", preferences.getString(PREFERENCES_EMAIL_KEY, ""));
    assertEquals("Password should be removed from preferences", "", preferences.getString(PREFERENCES_PASSWORD_KEY, ""));

    assertFalse(database.hasCredentials());
  }

  @Test
  public void withCredentialsButDontRememberLoginLoginActivityPushed() {
    database.putCredentials("username", "password");
    database.setRememberLogin(false);
    slimtimerActivity.onCreate(null);
    namedActivityPushed(slimtimerActivity, LoginActivity.class.getName());
  }

  @Test
  public void withoutCredentialsLoginActivityPushed() {
    slimtimerActivity.onCreate(null);
    namedActivityPushed(slimtimerActivity, LoginActivity.class.getName());
  }
}

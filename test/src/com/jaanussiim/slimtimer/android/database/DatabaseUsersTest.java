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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
public class DatabaseUsersTest {
  private DatabaseHelper database;

  @Before
  public void setUp() {
    database = new DatabaseHelper();
    database.open();
  }

  @Test
  public void withEmptyDatabaseNoCredentials() {
    assertFalse("Should have no credentials", database.hasCredentials());
  }

  @After
  public void tearDown() {
    database.close();
  }
}

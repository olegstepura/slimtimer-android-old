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

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jaanussiim.slimtimer.android.testutils.ActivityTestUtils.namedActivityPushed;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
  private LoginActivity activity;

  @Before
  public void setUp() {
    activity = new LoginActivity();
  }

  @Test
  public void onSuccessfulLoginMainActivityPushed() {
    activity.loginSuccess();
    namedActivityPushed(activity, MainViewActivity.class.getName());
  }
}

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

package com.jaanussiim.slimtimer.android.testutils;

import android.app.Activity;
import android.content.Intent;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ActivityTestUtils {
  public static void namedActivityPushed(final Activity fromActivity, final String nameOfPushedActivity) {
    final ShadowActivity slimtimerShadow = shadowOf(fromActivity);
    final Intent startedIntent = slimtimerShadow.getNextStartedActivity();
    assertNotNull("Intent not started for activity " + nameOfPushedActivity + " creation?", startedIntent);
    final ShadowIntent shadowIntent = shadowOf(startedIntent);
    assertThat(shadowIntent.getComponent().getClassName(), equalTo(nameOfPushedActivity));
  }
}

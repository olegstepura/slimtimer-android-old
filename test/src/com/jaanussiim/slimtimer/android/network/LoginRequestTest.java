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

package com.jaanussiim.slimtimer.android.network;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jaanussiim.slimtimer.android.Constants;

import java.net.HttpURLConnection;

import static com.jaanussiim.slimtimer.android.Constants.SERVER_URL;
import static java.net.HttpURLConnection.HTTP_BAD_GATEWAY;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class LoginRequestTest implements LoginRequestListener {
  private LoginRequest request;
  private boolean loginSuccess;
  private int loginErrorCode;

  @Before
  public void setUp() {
    request = new LoginRequest("username", "password");
    request.setListener(this);
    loginSuccess = false;
    loginErrorCode = -1;
  }

  @Test
  public void requestUrl() {
    assertEquals("Wrong url", SERVER_URL + "/users/token", request.getRequestUrl());
  }

  @Test
  public void requestBody() {
    String testBody = "user:\n  email: username\n  password: password\napi_key: " + Constants.SLIMTIMER_API_KEY;
    assertEquals("Wrong login request body created", testBody, request.requestBody());
  }

  @Test
  public void failedAuthenticationHandling() {
    request.httpResponse(HTTP_INTERNAL_ERROR, "{error: \"Authentication failed\"}");
    assertEquals("Wrong error code for failed authentication", LOGIN_ERROR_AUTHENTICATION_ERROR, loginErrorCode);
  }

  @Test
  public void noNetwork() {
    request.httpResponse(NetworkRequest.NO_NETWORK, "");
    assertEquals("Wrong error code for network error", LOGIN_ERROR_NO_NETWORK, loginErrorCode);
  }

  @Test
  public void successResponse() {
    request.httpResponse(HTTP_OK, "{user_id: 12345, access_token: \"3123123213213b3123b213b23213b213\"}");
    assertTrue("Login success response not received", loginSuccess);
  }

  @Test
  public void unknownError() {
    request.httpResponse(HTTP_BAD_GATEWAY, "");
    assertEquals("Wrong error code for unknown error", LOGIN_ERROR_UNKNOWN, loginErrorCode);
  }

  public void loginSuccess() {
    loginSuccess = true;
  }

  public void loginError(final int errorCode) {
    loginErrorCode = errorCode;
  }
}

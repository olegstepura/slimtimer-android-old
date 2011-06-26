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

import android.util.Log;
import com.google.gson.Gson;
import com.jaanussiim.slimtimer.android.database.Database;
import org.apache.http.client.HttpClient;

import java.net.HttpURLConnection;
import java.text.MessageFormat;

import static com.jaanussiim.slimtimer.android.Constants.SERVER_URL;
import static com.jaanussiim.slimtimer.android.Constants.SLIMTIMER_API_KEY;
import static com.jaanussiim.slimtimer.android.network.LoginRequestListener.LOGIN_ERROR_AUTHENTICATION_ERROR;
import static com.jaanussiim.slimtimer.android.network.LoginRequestListener.LOGIN_ERROR_NO_NETWORK;
import static com.jaanussiim.slimtimer.android.network.LoginRequestListener.LOGIN_ERROR_UNKNOWN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginRequest extends NetworkRequest {
  private static final String T = "LoginRequest";
  private static final String REQUEST = "user:\n  email: {0}\n  password: {1}\napi_key: {2}";
  private final Database database;
  private final String username;
  private final String password;
  private LoginRequestListener listener;

  public LoginRequest(final Database database, String username, String password) {
    super(SERVER_URL + "/users/token");
    this.database = database;
    this.username = username;
    this.password = password;
    setAccepts(CONTENT_TYPE_JSON);
    setContentType(CONTENT_TYPE_YAML);
  }

  @Override
  public String requestBody() {
    return MessageFormat.format(REQUEST, username, password, SLIMTIMER_API_KEY);
  }

  public void setListener(final LoginRequestListener listener) {
    this.listener = listener;
  }

  @Override
  public void httpResponse(final int httpCode, final String responseBody) {
    super.httpResponse(httpCode, responseBody);

    if (httpCode == NO_NETWORK) {
      listener.loginError(LOGIN_ERROR_NO_NETWORK);
      return;
    }

    Gson gson = new Gson();
    LoginResponse response = gson.fromJson(responseBody, LoginResponse.class);

    if (httpCode == HTTP_INTERNAL_ERROR) {
      listener.loginError(LOGIN_ERROR_AUTHENTICATION_ERROR);
      return;
    }

    if (httpCode != HTTP_OK) {
      listener.loginError(LOGIN_ERROR_UNKNOWN);
      return;
    }

    database.putCredentials(username, password);
    listener.loginSuccess();
  }
}

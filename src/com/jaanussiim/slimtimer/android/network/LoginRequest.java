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

import java.text.MessageFormat;

import static com.jaanussiim.slimtimer.android.Constants.SERVER_URL;
import static com.jaanussiim.slimtimer.android.Constants.SLIMTIMER_API_KEY;

public class LoginRequest extends NetworkRequest {
  private static final String REQUEST = "user:\n  email: {0}\n  password: {1}\napi_key: {2}";
  private final String username;
  private final String password;

  public LoginRequest(String username, String password) {
    super(SERVER_URL + "/users/token");
    this.username = username;
    this.password = password;
    setAccepts(CONTENT_TYPE_JSON);
    setContentType(CONTENT_TYPE_YAML);
  }

  @Override
  public String requestBody() {
    return MessageFormat.format(REQUEST, username, password, SLIMTIMER_API_KEY);
  }
}

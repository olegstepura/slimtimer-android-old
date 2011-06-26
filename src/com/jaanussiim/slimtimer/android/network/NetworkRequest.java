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
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NetworkRequest implements Runnable {
  private static final String T = "NetworkRequest";

  public static final String CONTENT_TYPE_JSON = "application/json";
  public static final String CONTENT_TYPE_YAML = "application/x-yaml";


  private RequestListener listener;
  private final String requestUrl;

  private static final HttpParams params = new BasicHttpParams();
  private String accepts;
  private String contentType;

  static {
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpConnectionParams.setStaleCheckingEnabled(params, false);
    HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
    HttpConnectionParams.setSoTimeout(params, 20 * 1000);
    HttpConnectionParams.setSocketBufferSize(params, 8192);
  }

  public NetworkRequest(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  public void execute() {
    Thread t = new Thread(this);
    t.start();
  }

  public void run() {
    Log.d(T, "Execute request to: " + requestUrl);
    DefaultHttpClient client = new DefaultHttpClient(params);
    HttpPost post = new HttpPost(requestUrl);
    post.setHeader("Accept", accepts);
    post.setHeader("Content-Type", contentType);

    try {
      final String body = requestBody();
      System.out.println("Request body:\n=====================\n" + body + "\n=====================\n");
      ByteArrayEntity baEntity = new ByteArrayEntity(body.getBytes("utf-8"));
      post.setEntity(baEntity);
    } catch (UnsupportedEncodingException e) {
      Log.e(T, "Encoding error", e);
      return;
    }

    try {
      HttpResponse resp = client.execute(post);
      int statusCode = resp.getStatusLine().getStatusCode();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      resp.getEntity().writeTo(baos);
      String response = new String(baos.toByteArray(), "utf-8");
      httpResponse(statusCode, response);
    } catch (IOException e) {
      Log.e(T, "Post error", e);
    }
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public String requestBody() {
    return "";
  }

  public void httpResponse(int httpCode, String responseBody) {
    Log.d(T, "httpResponse(" + httpCode + ", " + responseBody + ")");
  }

  protected void setAccepts(String contentType) {
    accepts = contentType;
  }

  protected void setContentType(String contentType) {
    this.contentType = contentType;
  }
}

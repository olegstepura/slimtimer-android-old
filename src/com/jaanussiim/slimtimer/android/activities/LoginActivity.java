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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.jaanussiim.slimtimer.android.R;
import com.jaanussiim.slimtimer.android.SlimtimerApplication;
import com.jaanussiim.slimtimer.android.database.Database;
import com.jaanussiim.slimtimer.android.network.LoginRequest;
import com.jaanussiim.slimtimer.android.network.LoginRequestListener;

public class LoginActivity extends FragmentActivity implements LoginRequestListener, DialogInterface.OnCancelListener {
  private static final String T = "LoginActivity";

  private static final int DIALOG_LOGGING_IN = 0;
  private static final int DIALOG_NETWORK_ERROR = 1;
  private static final int DIALOG_AUTHENTICATION_ERROR = 2;
  private static final int DIALOG_UNKNOWN_ERROR = 3;

  private Database database;

  private TextView usernameView;
  private TextView passwordView;
  private CheckBox rememberMe;

  private LoginRequest loginRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(T, "onCreate");
    setContentView(R.layout.login_activity);

    if (database == null) {
      database = ((SlimtimerApplication) getApplication()).getDatabase();
    }

    usernameView = (TextView) findViewById(R.id.email_edit);
    passwordView = (TextView) findViewById(R.id.password_edit);
    rememberMe = (CheckBox) findViewById(R.id.remember_me);

    final Button login = (Button) findViewById(R.id.login_button);
    login.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Log.d(T, "Clicked button");

        //TODO jaanus: input validation
        database.setRememberLogin(rememberMe.isChecked());

        showDialog(DIALOG_LOGGING_IN);

        loginRequest = new LoginRequest(database, usernameView.getText().toString(), passwordView.getText().toString());
        loginRequest.setListener(LoginActivity.this);
        loginRequest.execute();
      }
    });
  }

  @Override
  protected Dialog onCreateDialog(final int id) {
    switch (id) {
      case DIALOG_LOGGING_IN: {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.logging_in_message));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(this);
        return dialog;
      }
      case DIALOG_AUTHENTICATION_ERROR:
      case DIALOG_NETWORK_ERROR:
      case DIALOG_UNKNOWN_ERROR:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_error_dialog_title);
        if (id == DIALOG_AUTHENTICATION_ERROR) {
          builder.setMessage(R.string.login_error_authentication_error);
        } else if (id == DIALOG_NETWORK_ERROR) {
          builder.setMessage(R.string.login_error_network_error);
        } else {
          builder.setMessage(R.string.login_error_network_error);
        }
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(R.string.button_title_ok, null);
        return builder.create();
      default:
        return super.onCreateDialog(id);
    }
  }

  public void loginSuccess() {
    removeDialog(DIALOG_LOGGING_IN);
    Intent nextActivityStart = new Intent(this, MainViewActivity.class);
    startActivity(nextActivityStart);
  }

  public void loginError(final int errorCode) {
    runOnUiThread(new Runnable() {
      public void run() {
        removeDialog(DIALOG_LOGGING_IN);
        switch (errorCode) {
          case LOGIN_ERROR_AUTHENTICATION_ERROR:
            showDialog(LOGIN_ERROR_AUTHENTICATION_ERROR);
            return;
          case LOGIN_ERROR_NO_NETWORK:
            showDialog(LOGIN_ERROR_NO_NETWORK);
            return;
          case LOGIN_ERROR_UNKNOWN:
            //fall through
          default:
            showDialog(DIALOG_UNKNOWN_ERROR);
        }
      }
    });
  }

  public void onCancel(final DialogInterface dialogInterface) {
    if (loginRequest != null) {
      loginRequest.cancel();
    }
  }
}

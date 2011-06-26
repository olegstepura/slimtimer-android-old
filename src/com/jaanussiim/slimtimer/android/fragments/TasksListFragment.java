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

package com.jaanussiim.slimtimer.android.fragments;

import android.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TasksListFragment extends ListFragment {
  private static final String T = "TasksListFragment";

  final String[] testStrings = {"One", "Two", "Three", "Four", "Five", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen"};

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d(T, "onActivityCreated");
    getView().setBackgroundColor(0xFF0000FF);
    setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, testStrings));
  }

  @Override
  public void onListItemClick(final ListView l, final View v, final int position, final long id) {
    Log.d(T, "Clicked on " + testStrings[position]);
  }
}

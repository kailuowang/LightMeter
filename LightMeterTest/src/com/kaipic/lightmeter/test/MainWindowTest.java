/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaipic.lightmeter.test;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.kaipic.lightmeter.com.kaipic.lightmeter.MainWindow;

/**
 * Make sure that the main launcher activity opens up properly, which will be
 * verified by {@link ActivityTestCase#testActivityTestCaseSetUpProperly}.
 */
public class MainWindowTest extends ActivityInstrumentationTestCase2<MainWindow> {
  public MainWindowTest() {
      super("com.kaipic.lightmeter", MainWindow.class);
  }
  
  public void testCreateActivity(){
	 assertNotNull(getActivity()); 
  }

  public void testGetView(){
	  MainWindow activity = getActivity();
	  assertNotNull(activity.getMainTextView());
  }
  
  public void testViewText(){
	  MainWindow activity = getActivity();
	  CharSequence actual = activity.getMainTextView().getText();
	  
	  assertEquals("0", actual.toString());
  }
  
}

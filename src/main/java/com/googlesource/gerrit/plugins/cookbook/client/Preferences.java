// Copyright (C) 2015 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.cookbook.client;

import com.google.gerrit.client.rpc.NativeMap;
import com.google.gerrit.client.rpc.NativeString;
import com.google.gerrit.client.rpc.Natives;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.HashMap;
import java.util.Map;

public class Preferences extends JavaScriptObject {
  public final Map<String, String> urlAliases() {
    Map<String, String> urlAliases = new HashMap<>();
    for (String k : Natives.keys(_urlAliases())) {
      urlAliases.put(k, urlAlias(k));
    }
    return urlAliases;
  }

  private final native String urlAlias(String n) /*-{ return this.url_aliases[n]; }-*/;
  private final native NativeMap<NativeString> _urlAliases() /*-{ return this.url_aliases; }-*/;

  final void setUrlAliases(Map<String, String> urlAliases) {
    initUrlAliases();
    for (Map.Entry<String, String> e : urlAliases.entrySet()) {
      putUrlAlias(e.getKey(), e.getValue());
    }
  }
  private final native void putUrlAlias(String k, String v) /*-{ this.url_aliases[k] = v; }-*/;
  private final native void initUrlAliases() /*-{ this.url_aliases = {}; }-*/;

  protected Preferences() {
  }
}

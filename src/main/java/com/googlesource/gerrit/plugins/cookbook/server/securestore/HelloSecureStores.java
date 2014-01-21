// Copyright (C) 2014 The Android Open Source Project
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

package com.googlesource.gerrit.plugins.cookbook.server.securestore;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.util.FS;

import com.google.common.base.Strings;
import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.server.config.SitePath;
import com.google.gerrit.server.securestore.SecureStore;
import com.google.inject.Inject;

@Export("example")
public class HelloSecureStores implements SecureStore {
  public final FileBasedConfig sec;

  @Inject
  HelloSecureStores(@SitePath File site) {
    File secureConfig =
        new File(site, "etc" + File.separator + "secure.config");
    sec = new FileBasedConfig(secureConfig, FS.DETECTED);
    try {
      sec.load();
    } catch (Exception e) {
      throw new RuntimeException("Cannot load secure.config", e);
    }
  }

  @Override
  public String get(String section, String subsection, String name) {
    String value = sec.getString(section, subsection, name);
    if (Strings.isNullOrEmpty(value)) {
      return value;
    }
    return value.replaceFirst("test-", "");
  }

  @Override
  public void set(String section, String subsection, String name, String value) {
    if (value != null) {
      sec.setString(section, subsection, name, "test-" + value);
    } else {
      sec.unset(section, subsection, name);
    }
    save();
  }

  @Override
  public void unset(String section, String subsection, String name) {
    sec.unset(section, subsection, name);
    save();
  }

  private void save() {
    try {
      sec.save();
    } catch (IOException e) {
      throw new RuntimeException("Cannot save secure.config", e);
    }
  }
}

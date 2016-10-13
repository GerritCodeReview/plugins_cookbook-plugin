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

package com.googlesource.gerrit.plugins.cookbook.pluginprovider;

import com.google.gerrit.server.plugins.InvalidPluginException;
import com.google.gerrit.server.plugins.PluginContentScanner;
import com.google.gerrit.server.plugins.PluginEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Manifest;

/**
 * Plugin scanner is responsible for returning plugin contents.
 */
public class HelloSshPluginContentScanner implements PluginContentScanner {
  private final String pluginName;

  public HelloSshPluginContentScanner(String pluginName) {
    this.pluginName = pluginName;
  }

  /**
   * Scanner for self-registration: not used in this sample.
   */
  @Override
  public Map<Class<? extends Annotation>, Iterable<ExtensionMetaData>> scan(
      String pluginName, Iterable<Class<? extends Annotation>> annotations)
      throws InvalidPluginException {
    return null;
  }

  /**
   * Returns the Plugin Manfiest content as it was packaged in a JAR file.
   */
  @Override
  public Manifest getManifest() throws IOException {
    String manifestString =
        "PluginName: " + pluginName + "\n" +
        "Implementation-Version: 1.0\n" +
        "Gerrit-ReloadMode: restart\n" +
        "Gerrit-ApiType: Plugin\n" +
        "Gerrit-SshModule: " + HelloSshModule.class.getName() + "\n";
    return new Manifest(new ByteArrayInputStream(manifestString.getBytes()));
  }

  /**
   * Read static plugin resources: not used in this sample.
   */
  @Override
  public InputStream getInputStream(PluginEntry entry) throws IOException {
    return null;
  }

  /**
   * Return plugin resource entry: not used in this sample.
   */
  @Override
  public Optional<PluginEntry> getEntry(String resourcePath) {
    return Optional.empty();
  }

  /**
   * Return full list of plugin resources: not used in this sample.
   */
  @Override
  public Enumeration<PluginEntry> entries() {
    return Collections.emptyEnumeration();
  }
}

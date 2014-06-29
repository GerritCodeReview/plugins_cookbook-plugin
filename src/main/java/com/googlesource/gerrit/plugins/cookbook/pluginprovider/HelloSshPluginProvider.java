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
import com.google.gerrit.server.plugins.Plugin.ApiType;
import com.google.gerrit.server.plugins.ServerPlugin;
import com.google.gerrit.server.plugins.ServerPluginProvider;

import org.eclipse.jgit.internal.storage.file.FileSnapshot;

import java.io.File;

/**
 * Dynamic provider of Gerrit plugins derived by *.ssh files under $GERRIT_SITE/plugins.
 *
 * Example of how to define a dynamic Gerrit plugin provider to register
 * a new plugin based on the content of *.ssh files.
 *
 * This provider allows to define a Gerrit plugin by simply dropping a .ssh file
 * (e.g. hello.ssh) under $GERRIT_SITE/plugins.
 * Once the file is created a new plugin is automatically loaded with the name
 * without extension of the .ssh file (e.g. hello) and a new 'cat' SSH command is
 * automatically available from the registered plugin.
 */
public class HelloSshPluginProvider implements ServerPluginProvider {
  private static final String SSH_EXT = ".ssh";

  @Override
  public boolean handles(File srcFile) {
    return srcFile.getName().endsWith(SSH_EXT);
  }

  @Override
  public String getPluginName(File srcFile) {
    String srcFileName = srcFile.getName();
    return srcFileName.substring(0, srcFileName.length() - SSH_EXT.length());
  }

  @Override
  public ServerPlugin get(File srcFile, FileSnapshot snapshot,
      PluginDescription pluginDescriptor) throws InvalidPluginException {
    String name = getPluginName(srcFile);
    return new ServerPlugin(name, pluginDescriptor.canonicalUrl,
        pluginDescriptor.user, srcFile, snapshot,
        new HelloSshPluginContentScanner(name), pluginDescriptor.dataDir,
        ApiType.PLUGIN, getClass().getClassLoader(), null,
        HelloSshModule.class, null);
  }
}

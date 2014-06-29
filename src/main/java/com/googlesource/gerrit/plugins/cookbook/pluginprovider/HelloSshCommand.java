// Copyright (C) 2013 The Android Open Source Project
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

import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.server.config.SitePaths;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * SSH command defined by dynamically registered plugins.
 *
 */
@CommandMetaData(name = "print", description = "Print content of the plugin file")
public final class HelloSshCommand extends SshCommand {
  private final String pluginName;
  private final File pluginDir;

  @Inject
  public HelloSshCommand(@PluginName String pluginName, SitePaths sitePaths) {
    this.pluginName = pluginName;
    this.pluginDir = sitePaths.plugins_dir;
  }

  @Override
  public void run() {
    File pluginFile = new File(pluginDir, pluginName + ".ssh");
    try {
      Files.copy(pluginFile.toPath(), out);
    } catch (IOException e) {
      throw new RuntimeException("Cannot read plugin content of " + pluginFile,
          e);
    }
  }
}

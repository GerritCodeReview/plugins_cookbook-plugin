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

import com.google.gerrit.extensions.annotations.PluginData;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.server.config.SitePaths;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;

/** SSH command defined by dynamically registered plugins. */
@CommandMetaData(name = "cat", description = "Print content of plugin file")
public final class HelloSshCommand extends SshCommand {
  private final String pluginName;
  private final Path pluginDir;
  private final Path dataDir;

  @Argument(usage = "files in data directory to print")
  private List<String> files = new ArrayList<>();

  @Inject
  public HelloSshCommand(
      @PluginName String pluginName, SitePaths sitePaths, @PluginData Path dataDir) {
    this.pluginName = pluginName;
    this.pluginDir = sitePaths.plugins_dir.normalize();
    this.dataDir = dataDir.normalize();
  }

  @Override
  public void run() {
    Path pluginPath = pluginDir.resolve(pluginName + ".ssh");
    printOne(pluginPath);
    for (String name : files) {
      Path p = dataDir.resolve(name).normalize();
      if (!p.startsWith(dataDir)) {
        throw new RuntimeException(p + " is outside data directory " + dataDir);
      }
      printOne(p);
    }
  }

  private void printOne(Path p) {
    try {
      Files.copy(p, out);
    } catch (IOException e) {
      try (PrintWriter w = new PrintWriter(err)) {
        w.write("Error reading contents of ");
        w.write(p.toAbsolutePath().toString());
        w.write(": \n");
        e.printStackTrace(w);
      }
    }
  }
}

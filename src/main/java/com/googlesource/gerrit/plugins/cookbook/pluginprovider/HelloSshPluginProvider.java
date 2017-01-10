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

import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.server.plugins.InvalidPluginException;
import com.google.gerrit.server.plugins.ServerPlugin;
import com.google.gerrit.server.plugins.ServerPluginProvider;
import com.google.inject.Inject;
import java.nio.file.Path;
import org.eclipse.jgit.internal.storage.file.FileSnapshot;

/**
 * Dynamic provider of Gerrit plugins derived by *.ssh files under $GERRIT_SITE/plugins.
 *
 * <p>Example of how to define a dynamic Gerrit plugin provider to register a new plugin based on
 * the content of *.ssh files.
 *
 * <p>This provider allows to define a Gerrit plugin by simply dropping a .ssh file (e.g. hello.ssh)
 * under $GERRIT_SITE/plugins.
 *
 * <p>Once the file is created a new plugin is automatically loaded with the name without extension
 * of the .ssh file (e.g. hello) and a new 'cat' SSH command is automatically available from the
 * registered plugin.
 *
 * <p>The 'cat' command will print the contents of the .ssh file, along with the contents of any
 * arguments, resolved against the plugin's data directory $GERRIT_SITE/data/name.
 */
public class HelloSshPluginProvider implements ServerPluginProvider {
  private static final String SSH_EXT = ".ssh";
  private final String pluginName;

  @Inject
  public HelloSshPluginProvider(@PluginName String pluginName) {
    this.pluginName = pluginName;
  }

  @Override
  public boolean handles(Path srcPath) {
    return srcPath.getFileName().toString().endsWith(SSH_EXT);
  }

  @Override
  public String getPluginName(Path srcPath) {
    String name = srcPath.getFileName().toString();
    return name.substring(0, name.length() - SSH_EXT.length());
  }

  @Override
  public ServerPlugin get(Path srcPath, FileSnapshot snapshot, PluginDescription pluginDescriptor)
      throws InvalidPluginException {
    String name = getPluginName(srcPath);
    return new ServerPlugin(
        name,
        pluginDescriptor.canonicalUrl,
        pluginDescriptor.user,
        srcPath,
        snapshot,
        new HelloSshPluginContentScanner(name),
        pluginDescriptor.dataDir,
        getClass().getClassLoader());
  }

  @Override
  public String getProviderPluginName() {
    return pluginName;
  }
}

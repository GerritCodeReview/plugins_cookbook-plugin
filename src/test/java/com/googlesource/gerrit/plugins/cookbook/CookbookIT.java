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

package com.googlesource.gerrit.plugins.cookbook;

import static com.google.common.truth.Truth.assertThat;

import com.google.gerrit.acceptance.GerritConfig;
import com.google.gerrit.acceptance.GerritPluginConfig;
import com.google.gerrit.acceptance.GerritPluginConfigs;
import com.google.gerrit.acceptance.PluginDaemonTest;
import com.google.gerrit.acceptance.RestResponse;
import com.google.gerrit.acceptance.UseLocalDisk;
import com.google.gerrit.server.config.GerritServerConfig;
import com.google.gerrit.server.config.PluginConfigFactory;
import com.google.inject.Inject;

import org.eclipse.jgit.lib.Config;
import org.junit.Test;

public class CookbookIT extends PluginDaemonTest {
  @Inject
  PluginConfigFactory config;

  @Inject
  @GerritServerConfig
  Config serverConfig;

  @Test
  public void printTest() throws Exception {
    assertThat(adminSshSession.exec("cookbook print"))
        .isEqualTo("Hello world!\n");
    assertThat(adminSshSession.hasError()).isFalse();
  }

  @Test
  public void revisionTest() throws Exception {
    createChange();
    RestResponse response =
        adminRestSession.post("/changes/1/revisions/1/cookbook~hello-revision");
    assertThat(response.getEntityContent())
        .contains("Hello admin from change 1, patch set 1!");
  }

  @Test
  @GerritConfig(name="test.test", value="test")
  public void gerritConfigAnnotationSingle() throws Exception {
    assertThat(serverConfig.getString("test", null, "test")).isEqualTo("test");
  }

  @Test
  @UseLocalDisk
  @GerritPluginConfig(pluginName = "test", name = "test.test", value = "test")
  public void pluginConfigAnnotationSingle() throws Exception {
    assertThat(config).isNotNull();
    assertThat(
        config.getGlobalPluginConfig("test").getString("test", null, "test"))
        .isEqualTo("test");
  }

  @Test
  @UseLocalDisk
  @GerritPluginConfigs({
    @GerritPluginConfig(pluginName = "test1", name = "test.test1", value = "test"),
    @GerritPluginConfig(pluginName = "test1", name = "test.test2", value = "test"),
    @GerritPluginConfig(pluginName = "test2", name = "test.test", value = "test"),
    @GerritPluginConfig(pluginName = "test3", name = "test.test", value = "test")
  })
  public void pluginConfigAnnotationMutiple() throws Exception {
    assertThat(config).isNotNull();
    assertThat(
        config.getGlobalPluginConfig("test1").getString("test", null, "test1"))
        .isEqualTo("test");
    assertThat(
        config.getGlobalPluginConfig("test1").getString("test", null, "test2"))
        .isEqualTo("test");
    assertThat(
        config.getGlobalPluginConfig("test2").getString("test", null, "test"))
        .isEqualTo("test");
    assertThat(
        config.getGlobalPluginConfig("test3").getString("test", null, "test"))
        .isEqualTo("test");
  }
}

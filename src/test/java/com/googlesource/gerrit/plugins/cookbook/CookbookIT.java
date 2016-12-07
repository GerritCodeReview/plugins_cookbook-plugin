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

import com.google.gerrit.acceptance.LightweightPluginDaemonTest;
import com.google.gerrit.acceptance.RestResponse;
import com.google.gerrit.acceptance.TestPlugin;

import org.junit.Test;

@TestPlugin(
    name = "cookbook",
    sysModule = "com.googlesource.gerrit.plugins.cookbook.Module",
    httpModule = "com.googlesource.gerrit.plugins.cookbook.HttpModule",
    sshModule = "com.googlesource.gerrit.plugins.cookbook.SshModule"
)
public class CookbookIT extends LightweightPluginDaemonTest {
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
}

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

import com.google.gerrit.acceptance.NoHttpd;
import com.google.gerrit.acceptance.PluginDaemonTest;

import org.junit.Test;

@NoHttpd
public class CookbookIT extends PluginDaemonTest {

  @Test
  public void printTest() throws Exception {
    assertThat(sshSession.exec("cookbook print")).isEqualTo("Hello world!\n");
    assertThat(sshSession.hasError()).isFalse();
  }
}

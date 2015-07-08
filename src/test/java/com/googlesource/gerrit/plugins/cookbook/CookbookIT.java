package com.googlesource.gerrit.plugins.cookbook;

import static com.google.common.truth.Truth.assertThat;
import com.google.gerrit.acceptance.PluginDaemonTest;

import org.junit.Test;

public class CookbookIT extends PluginDaemonTest {

  @Test
  public void printTest() throws Exception {
    assertThat(sshSession.exec("cookbook print")).isEqualTo("Hello world!\n");
    assertThat(sshSession.hasError()).isFalse();
  }
}

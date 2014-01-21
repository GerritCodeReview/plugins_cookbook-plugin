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

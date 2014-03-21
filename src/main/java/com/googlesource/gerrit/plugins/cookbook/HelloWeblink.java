package com.googlesource.gerrit.plugins.cookbook;

import com.google.gerrit.extensions.annotations.Listen;
import com.google.gerrit.extensions.webui.PatchSetWebLink;

@Listen
public class HelloWeblink implements PatchSetWebLink{
  private String name = "HelloLink";
  private String placeHolderUrlProjectCommit =
      "http://my.hellolink.com/project=%s/commit=%s";

  @Override
  public String getLinkName() {
    return name ;
  }

  @Override
  public String getPatchSetUrl(String project, String commit) {
    return String.format(placeHolderUrlProjectCommit, project, commit);
  }
}
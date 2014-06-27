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

package com.googlesource.gerrit.plugins.cookbook;

import com.google.gerrit.extensions.webui.PatchSetWebLink;
import com.google.gerrit.extensions.webui.ProjectWebLink;

public class HelloWeblink implements PatchSetWebLink, ProjectWebLink {
  private String name = "HelloLink";
  private String placeHolderUrlProject =
      "http://my.hellolink.com/project=%s";
  private String placeHolderUrlProjectCommit =
      placeHolderUrlProject + "/commit=%s";
  private String myImageUrl = "http://placehold.it/16x16.gif";

  @Override
  public String getLinkName() {
    return name ;
  }

  @Override
  public String getPatchSetUrl(String project, String commit) {
    return String.format(placeHolderUrlProjectCommit, project, commit);
  }

  @Override
  public String getProjectUrl(String project) {
    return String.format(placeHolderUrlProject, project);
  }

  @Override
  public String getImageUrl() {
    return myImageUrl;
  }
}

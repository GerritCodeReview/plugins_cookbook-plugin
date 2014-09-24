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

import com.google.gerrit.extensions.common.WebLinkInfo;
import com.google.gerrit.extensions.webui.WebLink;
import com.google.gerrit.server.change.FileResource;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gerrit.server.project.BranchResource;
import com.google.gerrit.server.project.ProjectResource;

class HelloWeblink {
  private static String name = "HelloLink";
  private static String myImageUrl = "http://placehold.it/16x16.gif";
  private static String placeHolderUrlProject =
      "http://my.hellolink.com/project=%s";
  private static String placeHolderUrlProjectBranch =
      "http://my.hellolink.com/project=%s-branch=%s";
  private static String placeHolderUrlProjectCommit =
      placeHolderUrlProject + "/commit=%s";
  private static String placeHolderUrlProjectCommitFile =
      placeHolderUrlProjectCommit + "/file=%s";

  private static String getLinkName() {
    return name;
  }

  private static String getImageUrl() {
    return myImageUrl;
  }

  private static String getTarget() {
    return WebLink.Target.BLANK;
  }

  public static class PatchSetWeblink implements WebLink<RevisionResource> {

    @Override
    public WebLinkInfo getWebLinkInfoFor(RevisionResource resource) {
      return new WebLinkInfo(getLinkName(),
          getImageUrl(),
          getPatchSetUrl(resource.getChange().getProject().get(),
              resource.getPatchSet().getRevision().get()),
          getTarget());
    }

    private String getPatchSetUrl(String project, String commit) {
      return String.format(placeHolderUrlProjectCommit, project, commit);
    }

  }

  public static class BranchWeblink implements WebLink<BranchResource> {

    @Override
    public WebLinkInfo getWebLinkInfoFor(BranchResource resource) {
      return new WebLinkInfo(getLinkName(), getImageUrl(), getBranchUrl(
          resource.getName(), resource.getBranchKey().get()), getTarget());
    }

    private String getBranchUrl(String projectName, String branchName) {
      return String
          .format(placeHolderUrlProjectBranch, projectName, branchName);
    }

  }

  public static class ProjectWebLink implements WebLink<ProjectResource> {

    @Override
    public WebLinkInfo getWebLinkInfoFor(ProjectResource resource) {
      return new WebLinkInfo(getLinkName(), getImageUrl(),
          getProjectUrl(resource.getName()), getTarget());
    }

    private String getProjectUrl(String project) {
      return String.format(placeHolderUrlProject, project);
    }

  }

  public static class FileWebLink implements WebLink<FileResource> {

    @Override
    public WebLinkInfo getWebLinkInfoFor(FileResource resource) {
      return new WebLinkInfo(getLinkName(),
          getImageUrl(),
          getFileUrl(resource.getRevision().getChange().getProject().get(),
              resource.getRevision().getPatchSet().getRefName(),
              resource.getPatchKey().getFileName()),
          getTarget());
    }

    private String getFileUrl(String projectName, String revision, String fileName) {
      return String.format(placeHolderUrlProjectCommitFile,
          projectName, revision, fileName);
    }

  }
}
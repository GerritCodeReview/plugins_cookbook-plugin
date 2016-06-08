// Copyright (C) 2016 The Android Open Source Project
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

import com.google.gerrit.extensions.annotations.Listen;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.reviewdb.client.Project.NameKey;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.validators.EmailIdValidationListener;


@Listen
public class EmailPolicy implements EmailIdValidationListener {

  @Override
  public boolean isCommitterEmailIdValid(NameKey project, String email,
      IdentifiedUser user) {
    if (isProjectProprietary(project)) {
      if (email.endsWith("google.com")) {
        return false;
      }
      return true;
    }
  }

  @Override
  public boolean isAuthorEmailIdValid(NameKey project, String email,
      IdentifiedUser user) {
    if (isProjectProprietary(project)) {
      if (email.endsWith("google.com")) {
        return false;
      }
      return true;
    }
  }

  private boolean isProjectProprietary(NameKey project) {
    // mock
    if(project.get().equals("kernel")) {
      return false;
    }
    return true;
  }
}

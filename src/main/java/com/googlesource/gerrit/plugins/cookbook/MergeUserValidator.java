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

import com.google.gerrit.reviewdb.client.Branch.NameKey;
import com.google.gerrit.reviewdb.client.PatchSet.Id;
import com.google.gerrit.reviewdb.client.PatchSetApproval;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.ApprovalsUtil;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.git.CodeReviewCommit;
import com.google.gerrit.server.git.CommitMergeStatus;
import com.google.gerrit.server.git.validators.MergeValidationException;
import com.google.gerrit.server.git.validators.MergeValidationListener;
import com.google.gerrit.server.project.ProjectState;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import org.eclipse.jgit.lib.Repository;

// Because we have a dedicated Module, we need to bind to the set
// there, however, if you are using this as a base for your own
// plugin, you can simply comment out the 'Listen' annotation and
// it will work as expected.
//@Listen
@Singleton
public class MergeUserValidator implements MergeValidationListener {

  private final IdentifiedUser.GenericFactory identifiedUserFactory;
  private final Provider<ReviewDb> reviewDb;
  private final ApprovalsUtil approvalsUtil;


  // Because there is 'No user on merge thread' we need to get the
  // identified user from IdentifiedUser.GenericFactory, this is
  // normally not needed and you can, in many cases, just use
  // Provider<CurrentUser>, unfortunately not this one.
  @Inject
  MergeUserValidator(IdentifiedUser.GenericFactory identifiedUserFactory,
      Provider<ReviewDb> reviewDb,
      ApprovalsUtil approvalsUtil) {
    this.identifiedUserFactory = identifiedUserFactory;
    this.reviewDb = reviewDb;
    this.approvalsUtil = approvalsUtil;
  }

  /**
   * Reject all merges if the submitter is not an administrator
   */
  @Override
  public void onPreMerge(Repository repo, CodeReviewCommit commit,
      ProjectState destProject, NameKey destBranch, Id patchSetId)
      throws MergeValidationException {
    PatchSetApproval psa =
        approvalsUtil.getSubmitter(reviewDb.get(), commit.notes(), patchSetId);
    if (psa == null) {
      throw new MergeValidationException(
          CommitMergeStatus.NO_PATCH_SET);
    }
    final IdentifiedUser submitter =
        identifiedUserFactory.create(psa.getAccountId());
    if (!submitter.getCapabilities().canAdministrateServer()) {
      throw new MergeValidationException(CommitMergeStatus.MISSING_DEPENDENCY);
    }
  }
}

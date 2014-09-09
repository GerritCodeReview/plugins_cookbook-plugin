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

import com.google.gerrit.extensions.annotations.Listen;
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
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.jgit.lib.Repository;

@Listen
@Singleton
public class MergeUserValidator implements MergeValidationListener {

  private final IdentifiedUser.GenericFactory identifiedUserFactory;
  private final SchemaFactory<ReviewDb> schemaFactory;
  private ReviewDb db;
  private final ApprovalsUtil approvalsUtil;

  @Inject
  MergeUserValidator(IdentifiedUser.GenericFactory identifiedUserFactory,
      SchemaFactory<ReviewDb> schemaFactory,
      ApprovalsUtil approvalsUtil) {
    this.identifiedUserFactory = identifiedUserFactory;
    this.schemaFactory = schemaFactory;
    this.approvalsUtil = approvalsUtil;
  }

  /**
   * Reject all merges if the submitter is not an administrator
   */
  @Override
  public void onPreMerge(Repository repo, CodeReviewCommit commit,
      ProjectState destProject, NameKey destBranch, Id patchSetId)
      throws MergeValidationException {
    try {
      db = schemaFactory.open();
      PatchSetApproval psa =
          approvalsUtil.getSubmitter(db, commit.notes(), patchSetId);
      if (psa == null) {
        throw new MergeValidationException(
            CommitMergeStatus.REJECTED_BY_PLUGIN);
      }
      final IdentifiedUser submitter =
          identifiedUserFactory.create(psa.getAccountId());
      if (!submitter.getCapabilities().canAdministrateServer()) {
        throw new MergeValidationException(CommitMergeStatus.REJECTED_BY_PLUGIN);
      }
    } catch (OrmException e) {
      throw new MergeValidationException(CommitMergeStatus.REJECTED_BY_PLUGIN);
    } finally {
      if (db != null) {
        db.close();
      }
    }
  }

}

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

package com.googlesource.gerrit.plugins.cookbook.chefs;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Predicate;
import com.google.gerrit.common.data.GroupDescription;
import com.google.gerrit.common.data.GroupReference;
import com.google.gerrit.extensions.api.changes.Changes;
import com.google.gerrit.extensions.api.changes.Changes.QueryRequest;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.google.gerrit.reviewdb.client.AccountGroup;
import com.google.gerrit.reviewdb.client.AccountGroup.UUID;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.server.CurrentUser;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.account.GroupBackend;
import com.google.gerrit.server.account.GroupMembership;
import com.google.gerrit.server.account.FilterableListGroupMembership;
import com.google.gerrit.server.project.ProjectControl;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class ChefsGroupBackend implements GroupBackend {

  public static class Module extends AbstractModule {
    @Override
    protected void configure() {
      DynamicSet.bind(binder(), GroupBackend.class).to(ChefsGroupBackend.class);
    }
  }

  private static final String GROUP_NAME = "cookbook:chefs";
  private static final AccountGroup.UUID UUID = new AccountGroup.UUID(
      GROUP_NAME);
  private static final GroupDescription.Basic BASIC = new GroupDescription.Basic() {

    @Override
    public String getUrl() {
      return null;
    }

    @Override
    public String getName() {
      return GROUP_NAME;
    }

    @Override
    public UUID getGroupUUID() {
      return UUID;
    }

    @Override
    public String getEmailAddress() {
      return null;
    }
  };

  private static final Iterable<UUID> UUID_LIST = Arrays.asList(UUID);
  private static final int MIN_FOR_CHEF = 50;

  private final Changes changes;

  @Inject
  public ChefsGroupBackend(Changes changes) {
    this.changes = changes;
  }

  @Override
  public boolean handles(UUID uuid) {
    return UUID.equals(uuid);
  }

  @Override
  public GroupDescription.Basic get(UUID uuid) {
    if (!handles(uuid)) {
      return null;
    }

    return BASIC;
  }

  @Override
  public Collection<GroupReference> suggest(String prefix,
      ProjectControl project) {
    if (prefix != null
        && !prefix.isEmpty()
        && GROUP_NAME.toLowerCase(Locale.US).startsWith(
            prefix.toLowerCase(Locale.US))) {
      return Collections.singletonList(new GroupReference(UUID, GROUP_NAME));
    }
    return Collections.emptyList();
  }

  @Override
  public GroupMembership membershipsOf(IdentifiedUser user) {
    return GroupMembership.EMPTY;
  }

  public static GroupReference getGroup() {
    return GroupReference.forGroup(BASIC);
  }

  @Override
  public GroupMembership membershipsOf(ProjectControl projectControl) {
    final Project project = projectControl.getProject();
    final CurrentUser user = projectControl.getCurrentUser();

    return new FilterableListGroupMembership(UUID_LIST, new Predicate<AccountGroup.UUID>() {
      @Override
      public boolean apply(AccountGroup.UUID group) {
        return isChef(project, user);
      }
    });
  }

  private boolean isChef(Project project, CurrentUser currentUser) {
    String pattern = "owner:\"{0}\" AND project:\"{1}\" AND status:merged AND limit:{2}";
    QueryRequest request = changes.query(MessageFormat.format(pattern,
        currentUser.getUserName(), project.getName(), MIN_FOR_CHEF));
    List<ChangeInfo> infos;
    try {
      infos = request.get();
      return infos.size() >= MIN_FOR_CHEF;
    } catch (RestApiException e) {
      return false;
    }
  }
}

// Copyright (C) 2013 The Android Open Source Project
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

import static com.google.gerrit.server.change.RevisionResource.REVISION_KIND;
import static com.google.gerrit.server.project.ProjectResource.PROJECT_KIND;

import com.google.common.collect.ImmutableList;
import com.google.gerrit.extensions.annotations.Exports;
import com.google.gerrit.extensions.api.projects.ProjectConfigEntryType;
import com.google.gerrit.extensions.client.InheritableBoolean;
import com.google.gerrit.extensions.config.ExternalIncludedIn;
import com.google.gerrit.extensions.events.LifecycleListener;
import com.google.gerrit.extensions.events.NewProjectCreatedListener;
import com.google.gerrit.extensions.events.UsageDataPublishedListener;
import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.gerrit.extensions.webui.BranchWebLink;
import com.google.gerrit.extensions.webui.FileHistoryWebLink;
import com.google.gerrit.extensions.webui.GwtPlugin;
import com.google.gerrit.extensions.webui.JavaScriptPlugin;
import com.google.gerrit.extensions.webui.PatchSetWebLink;
import com.google.gerrit.extensions.webui.ProjectWebLink;
import com.google.gerrit.extensions.webui.TopMenu;
import com.google.gerrit.extensions.webui.WebUiPlugin;
import com.google.gerrit.server.config.ProjectConfigEntry;
import com.google.gerrit.server.git.validators.CommitValidationListener;
import com.google.gerrit.server.git.validators.MergeValidationListener;
import com.google.gerrit.server.git.validators.RefOperationValidationListener;
import com.google.gerrit.server.git.validators.UploadValidationListener;
import com.google.gerrit.server.plugins.ServerPluginProvider;
import com.google.gerrit.server.query.change.ChangeQueryBuilder.ChangeOperatorFactory;
import com.google.gerrit.server.validators.HashtagValidationListener;
import com.google.inject.AbstractModule;
import com.googlesource.gerrit.plugins.cookbook.pluginprovider.HelloSshPluginProvider;

public class Module extends AbstractModule {

  @Override
  protected void configure() {
    DynamicSet.bind(binder(), TopMenu.class).to(HelloTopMenu.class);
    DynamicSet.bind(binder(), PatchSetWebLink.class).to(HelloWeblink.class);
    DynamicSet.bind(binder(), ProjectWebLink.class).to(HelloWeblink.class);
    DynamicSet.bind(binder(), BranchWebLink.class).to(HelloWeblink.class);
    DynamicSet.bind(binder(), FileHistoryWebLink.class).to(HelloWeblink.class);
    DynamicSet.bind(binder(), ServerPluginProvider.class).to(HelloSshPluginProvider.class);
    DynamicSet.bind(binder(), UsageDataPublishedListener.class).to(UsageDataLogger.class);
    DynamicSet.bind(binder(), LifecycleListener.class).to(ConsoleMetricReporter.class);
    install(
        new RestApiModule() {
          @Override
          protected void configure() {
            post(REVISION_KIND, "hello-revision").to(HelloRevisionAction.class);
            post(PROJECT_KIND, "hello-project").to(HelloProjectAction.class);
            get(REVISION_KIND, "greetings").to(Greetings.class);
          }
        });
    DynamicSet.bind(binder(), UploadValidationListener.class).to(DenyUploadExample.class);
    DynamicSet.bind(binder(), MergeValidationListener.class).to(MergeUserValidator.class);
    DynamicSet.bind(binder(), HashtagValidationListener.class).to(HashtagValidator.class);
    DynamicSet.bind(binder(), CommitValidationListener.class).to(CommitValidator.class);
    DynamicSet.bind(binder(), NewProjectCreatedListener.class).to(ProjectCreatedListener.class);
    DynamicSet.bind(binder(), RefOperationValidationListener.class)
        .to(RefOperationValidationExample.class);
    configurePluginParameters();
    DynamicSet.bind(binder(), ExternalIncludedIn.class).to(DeployedOnIncludedInExtension.class);

    bind(ChangeOperatorFactory.class)
        .annotatedWith(Exports.named("sample"))
        .to(SampleOperator.class);

    DynamicSet.bind(binder(), WebUiPlugin.class).toInstance(new JavaScriptPlugin("greetings.js"));
    DynamicSet.bind(binder(), WebUiPlugin.class)
        .toInstance(new JavaScriptPlugin("hello-change.js"));
    DynamicSet.bind(binder(), WebUiPlugin.class)
        .toInstance(new JavaScriptPlugin("hello-project.js"));
    DynamicSet.bind(binder(), WebUiPlugin.class)
        .toInstance(new JavaScriptPlugin("hello-revision.js"));
    DynamicSet.bind(binder(), WebUiPlugin.class).toInstance(new GwtPlugin("cookbook"));
  }

  private void configurePluginParameters() {
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("enable-hello"))
        .toInstance(new ProjectConfigEntry("Enable Greeting", true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("enable-goodbye"))
        .toInstance(
            new ProjectConfigEntry(
                "Enable Say Goodbye", InheritableBoolean.TRUE, InheritableBoolean.class, true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("default-greeting"))
        .toInstance(new ProjectConfigEntry("Default Greeting", "Hey dude, how are you?", true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("language"))
        .toInstance(
            new ProjectConfigEntry(
                "Preferred Language", "en", ImmutableList.of("en", "de", "fr"), true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("greet-number-per-week"))
        .toInstance(new ProjectConfigEntry("Greets Per Week", 42, true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("greet-number-per-year"))
        .toInstance(new ProjectConfigEntry("Greets Per Year", 4711L, true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("reviewers"))
        .toInstance(
            new ProjectConfigEntry(
                "Reviewers",
                null,
                ProjectConfigEntryType.ARRAY,
                null,
                false,
                "Users or groups can be provided as reviewers"));
  }
}

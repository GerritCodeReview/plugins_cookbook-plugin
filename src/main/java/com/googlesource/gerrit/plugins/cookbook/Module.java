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
import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.gerrit.extensions.webui.TopMenu;
import com.google.gerrit.reviewdb.client.Project.InheritableBoolean;
import com.google.gerrit.server.config.ProjectConfigEntry;
import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

  @Override
  protected void configure() {
    DynamicSet.bind(binder(), TopMenu.class)
        .to(HelloTopMenu.class);
    install(new RestApiModule() {
      @Override
      protected void configure() {
        post(REVISION_KIND, "hello-revision").to(HelloRevisionAction.class);
        post(PROJECT_KIND, "hello-project").to(HelloProjectAction.class);
        get(REVISION_KIND, "greetings").to(Greetings.class);
      }
    });
    configurePluginParameters();    
  }

  private void configurePluginParameters() {
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("enabled-hello"))
        .toInstance(new ProjectConfigEntry("Enable Greeting", true));
    bind(ProjectConfigEntry.class)
       .annotatedWith(Exports.named("enabled-goodby"))
       .toInstance(new ProjectConfigEntry("Enable Say Good By",
           InheritableBoolean.TRUE,
           InheritableBoolean.class, true));
    bind(ProjectConfigEntry.class)
       .annotatedWith(Exports.named("default-greet"))
       .toInstance(new ProjectConfigEntry("Default Greet",
           "Hey dude, how are you?", true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("language"))
        .toInstance(new ProjectConfigEntry("Preferred Language", "en",
            ImmutableList.of("en", "de", "fr"), true));
    bind(ProjectConfigEntry.class)
        .annotatedWith(Exports.named("greet-number-per-week"))
        .toInstance(new ProjectConfigEntry("Greets Per Week", 42, true));
    bind(ProjectConfigEntry.class)
       .annotatedWith(Exports.named("greet-number-per-year"))
       .toInstance(new ProjectConfigEntry("Greets Per Year", 4711L, true));
  }
}

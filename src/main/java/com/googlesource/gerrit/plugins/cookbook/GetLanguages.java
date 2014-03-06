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

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.server.change.RevisionResource;

public class GetLanguages implements RestReadView<RevisionResource> {
  // Languages of the European Union
  protected final static List<String> LANGUAGES =
      new ImmutableList.Builder<String>()
          .add("Bulgarian")
          .add("Croatian")
          .add("Czech")
          .add("Danish")
          .add("Dutch")
          .add("English")
          .add("Finnish")
          .add("French")
          .add("German")
          .add("Greek")
          .add("Hungarian")
          .add("Irish")
          .add("Italian")
          .add("Latvian")
          .add("Lithuanian")
          .add("Maltese")
          .add("Polish")
          .add("Portuguese")
          .add("Romanian")
          .add("Slovak")
          .add("Slovene")
          .add("Spanish")
          .add("Swedish")
          .build();

  @Override
  public Response<List<String>> apply(RevisionResource rev) {
    return Response.ok(LANGUAGES);
  }
}

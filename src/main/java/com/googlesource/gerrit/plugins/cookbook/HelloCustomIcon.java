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

import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.CustomIconInfo;
import com.google.gerrit.extensions.webui.ProjectCustomIcon;

import java.util.List;

class HelloCustomIcon implements ProjectCustomIcon {

  CustomIconInfo icon1 = new CustomIconInfo("my icon",
                               "plugins/cookbook/static/myicon.png");
  CustomIconInfo icon2 = new CustomIconInfo("my 2nd icon",
                               "plugins/cookbook/static/myicon2.png");

  String[] projectsWithIcon1 = {"All-Projects", "All-Users"};
  String[] projectsWithIcon2 = {"All-Projects"};

  @Override
  public Iterable<CustomIconInfo> getIcons(String project) {
    List<CustomIconInfo> icons = Lists.newArrayList();
    for (String p : projectsWithIcon1) {
      if (p.equals(project)) {
        icons.add(icon1);
        break;
      }
    }
    for (String p : projectsWithIcon2) {
      if (p.equals(project)) {
        icons.add(icon2);
        break;
      }
    }
    return icons;
  }
}

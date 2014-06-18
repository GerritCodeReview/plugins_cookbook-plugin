package com.googlesource.gerrit.plugins.cookbook;

import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.CustomIconInfo;
import com.google.gerrit.extensions.webui.ProjectCustomIcon;

import java.util.List;

public class HelloCustomIcon implements ProjectCustomIcon {

  CustomIconInfo icon1 = new CustomIconInfo("my icon","plugins/cookbook/static/myicon.png");
  CustomIconInfo icon2 = new CustomIconInfo("my 2nd icon", "plugins/cookbook/static/myicon2.png");

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

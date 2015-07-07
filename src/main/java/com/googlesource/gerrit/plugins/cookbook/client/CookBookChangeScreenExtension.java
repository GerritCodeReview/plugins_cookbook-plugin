// Copyright (C) 2015 The Android Open Source Project
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

package com.googlesource.gerrit.plugins.cookbook.client;

import com.google.gerrit.client.GerritUiExtensionPoint;
import com.google.gerrit.plugin.client.extension.Panel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwtexpui.clippy.client.CopyableLabel;

/**
 * Extension for change screen that displays the numeric change ID with
 * copy-to-clipboard icon.
 */
public class CookBookChangeScreenExtension extends VerticalPanel {
  static class Factory implements Panel.EntryPoint {
    @Override
    public void onLoad(Panel panel) {
      panel.setWidget(new CookBookChangeScreenExtension(panel));
    }
  }

  CookBookChangeScreenExtension(Panel panel) {
    Grid g = new Grid(1, 2);
    g.addStyleName("infoBlock");
    CellFormatter fmt = g.getCellFormatter();

    g.setText(0, 0, "Numeric Change ID");
    fmt.addStyleName(0, 0, "header");
    g.setWidget(0, 1,
        new CopyableLabel(Integer.toString(panel.getInt(
            GerritUiExtensionPoint.Key.CHANGE_ID, -1))));
    add(g);

    fmt.addStyleName(0, 0, "topmost");
    fmt.addStyleName(0, 1, "topmost");
    fmt.addStyleName(0, 0, "bottomheader");
  }
}

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

import com.google.gerrit.plugin.client.Plugin;
import com.google.gerrit.plugin.client.extension.Panel;
import com.google.gerrit.plugin.client.rpc.RestApi;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Map;

public class ChangeScreenPreferencePanel extends VerticalPanel {
  private final static String DEFAULT = "DEFAULT";
  private final static String COOKBOOK = "COOKBOOK";
  private final static String OTHER = "OTHER";
  private final static String DEFAULT_URL_PATH = "/c/";
  private final static String COOKBOOK_URL_PATH = "/x/" + Plugin.get().getName() +  "/c/";

  static class Factory implements Panel.EntryPoint {
    @Override
    public void onLoad(Panel panel) {
      panel.setWidget(new ChangeScreenPreferencePanel());
    }
  }

  ChangeScreenPreferencePanel() {
    new RestApi("accounts").id("self").view("preferences")
        .get(new AsyncCallback<Preferences>() {
          @Override
          public void onSuccess(Preferences result) {
            display(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            // never invoked
          }
        });
  }

  private void display(final Preferences info) {
    HorizontalPanel p = new HorizontalPanel();
    add(p);

    Label label = new Label("Change Screen:");
    p.add(label);
    label.getElement().getStyle().setMarginRight(5, Unit.PX);
    label.getElement().getStyle().setMarginTop(2, Unit.PX);
    final ListBox box = new ListBox();
    p.add(box);

    box.addItem(DEFAULT, DEFAULT);
    box.addItem(COOKBOOK, COOKBOOK);

    String selected = DEFAULT;
    if (info.urlAliases().containsKey(DEFAULT_URL_PATH)) {
      String urlPath = info.urlAliases().get(DEFAULT_URL_PATH);
      if (urlPath.equals(COOKBOOK_URL_PATH)) {
        selected = COOKBOOK;
      } else if (!urlPath.equals(DEFAULT_URL_PATH)) {
        box.addItem(OTHER, OTHER);
        selected = OTHER;
      }
    }

    for (int i = 0; i < box.getItemCount(); i++) {
      if (selected.equals(box.getValue(i))) {
        box.setSelectedIndex(i);
        break;
      }
    }

    box.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        if (box.getSelectedValue().equals(OTHER)) {
          return;
        }

        Map<String, String> urlAliases = info.urlAliases();
        if (box.getSelectedValue().equals(COOKBOOK)) {
          urlAliases.put(DEFAULT_URL_PATH, COOKBOOK_URL_PATH);
        } else {
          urlAliases.remove(DEFAULT_URL_PATH);
        }
        info.setUrlAliases(urlAliases);

        new RestApi("accounts").id("self").view("preferences")
            .put(info, new AsyncCallback<Preferences>() {
              @Override
              public void onSuccess(Preferences result) {
                Plugin.get().refreshUrlAliases();
              }

              @Override
              public void onFailure(Throwable caught) {
                // never invoked
              }
            });
      }
    });
  }
}

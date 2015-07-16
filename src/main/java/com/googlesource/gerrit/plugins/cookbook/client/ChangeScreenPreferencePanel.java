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

import com.google.gerrit.client.info.AccountPreferencesInfo;
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
  private static final String DEFAULT = "DEFAULT";
  private static final String COOKBOOK = "COOKBOOK";
  private static final String OTHER = "OTHER";
  private static final String DEFAULT_URL_MATCH = "/c/(.*)";
  private static final String COOKBOOK_URL_TOKEN =
      "/x/" + Plugin.get().getName() + "/c/$1";

  static class Factory implements Panel.EntryPoint {
    @Override
    public void onLoad(Panel panel) {
      panel.setWidget(new ChangeScreenPreferencePanel());
    }
  }

  ChangeScreenPreferencePanel() {
    new RestApi("accounts").id("self").view("preferences")
        .get(new AsyncCallback<AccountPreferencesInfo>() {
          @Override
          public void onSuccess(AccountPreferencesInfo result) {
            display(result);
          }

          @Override
          public void onFailure(Throwable caught) {
            // never invoked
          }
        });
  }

  private void display(final AccountPreferencesInfo info) {
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
    if (info.urlAliases().containsKey(DEFAULT_URL_MATCH)) {
      String token = info.urlAliases().get(DEFAULT_URL_MATCH);
      if (token.equals(COOKBOOK_URL_TOKEN)) {
        selected = COOKBOOK;
      } else if (!token.equals(DEFAULT_URL_MATCH)) {
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
          urlAliases.put(DEFAULT_URL_MATCH, COOKBOOK_URL_TOKEN);
        } else {
          urlAliases.remove(DEFAULT_URL_MATCH);
        }
        info.setUrlAliases(urlAliases);

        new RestApi("accounts").id("self").view("preferences")
            .put(info, new AsyncCallback<AccountPreferencesInfo>() {
              @Override
              public void onSuccess(AccountPreferencesInfo result) {
                Plugin.get().refreshUserPreferences();
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

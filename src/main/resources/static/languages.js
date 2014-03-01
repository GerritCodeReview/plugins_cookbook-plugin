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

Gerrit.install(function(self) {
    function onAddLanguages(c, r) {
      var url = "changes/"
          + c._number
          + "/revisions/"
          + r._number
          + "/"
          + self.getPluginName()
          + "~"
          + "languages";
      var change_plugins = document
          .getElementById('change_plugins');
      Gerrit.get(url, function(r) {
         var doc = document;
         var frg = doc.createDocumentFragment();
         var lang = doc.createElement("input");
         frg.appendChild(lang);
         lang.setAttribute("id", "cookbook-plugin~languages");
         lang.setAttribute("type", "hidden");
         lang.setAttribute("value", r);
         // add frg to #change_plugins container
         change_plugins.appendChild(frg);
      });
    }
    Gerrit.on('showchange', onAddLanguages);
  });

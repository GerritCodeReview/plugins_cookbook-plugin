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

Gerrit.install(function(self) {
    function onShowChange(c, r) {
      console.log("Show change: "
          + c.id
          + ", revision: " + r.name);
    }
    function onSubmitChange(c, r) {
      var l = document.getElementById('cookbook-plugin~languages');
      return confirm("Really submit change:\n"
          + c.id + "\n"
          + "revision: " + r.name + "\n"
          + "(supported languages: " + l.getAttribute("value").replace(/,/g, ' ') + ")"
          + "?");
    }
    function onHistory(t) {
      console.log("History: " + t);
    }
    Gerrit.on('showchange', onShowChange);
    Gerrit.on('submitchange', onSubmitChange);
    Gerrit.on('history', onHistory);
  });

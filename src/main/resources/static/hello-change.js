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
    function onOpenChange(c, r) {
      alert("Hello from change\n"
          + c.id + "\n"
          + "revision: " + r.name);
    }
    function onSubmit(c, r) {
      return confirm("Really submit change:\n"
          + c.id + "\n"
          + "revision: " + r.name
          + "?");
    }
    function onViewChanged(t) {
      alert("View changed:\n"
          + t);
    }
    Gerrit.on('showchange', onOpenChange);
    Gerrit.on('submitchange', onSubmit);
    Gerrit.on('history', onViewChanged);
  });

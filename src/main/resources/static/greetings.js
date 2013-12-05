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
      console.log("onOpenChange:");
      var rsrc = "changes/"
          + c._number
          + "/revisions/"
          + r._number
          + "/";
      var url = rsrc
          + self.getPluginName()
          + "~"
          + "greetings";
      self.get_core(url, function(r) {
         var doc = document;
         var frg = doc.createDocumentFragment();
         for (var i = 0; i < r.length; i++) {
           // row
           var tr = doc.createElement('tr');
           // greet
           var g = r[i];
           // first column: message
           var td = doc.createElement('td');
           td.appendChild(doc.createTextNode(g.message));
           tr.appendChild(td);
           // second column country
           td = doc.createElement('td');
           var a = doc.createElement('a');
           a.href = g.href;
           a.appendChild(doc.createTextNode(g.country));
           td.appendChild(a);
           tr.appendChild(td);
           // add row to the fragment
           frg.appendChild(tr);
         }
         // add fragment to #change_plugin container
         var table = doc.createElement('table');
         table.appendChild(tr);
         table.appendChild(frg);
         doc.getElementById('change_plugins').appendChild(table);
      });
    }
    Gerrit.on('showchange', onOpenChange);
  });

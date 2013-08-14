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

package com.googlesource.gerrit.plugins.cookbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.errors.ConfigInvalidException;

import com.google.gerrit.extensions.annotations.Export;
import com.google.gerrit.server.CurrentUser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class HelloServlet extends HttpServlet{
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    CurrentUser currentUser;
    res.setContentType("text/plain");
    res.setCharacterEncoding("UTF-8");
    currentUser = (CurrentUser) req.getAttribute("currentUser");
    res.getWriter().write("Hello " + (currentUser.getUserName() == null ? "Unknown User" : currentUser.getUserName()));
  }
}

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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.errors.ConfigInvalidException;

import com.google.gerrit.server.CurrentUser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class HelloFilter implements Filter{
  private final Provider<CurrentUser> currentUserProvider;

  @Inject
  HelloFilter(final Provider<CurrentUser> cu)
      throws ConfigInvalidException, IOException {
    currentUserProvider = cu;
  }

  @Override public void doFilter(
     ServletRequest request,  ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    if (httpRequest != null){
      httpRequest.setAttribute(
          "currentUser",
          currentUserProvider.get());
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() { }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { }
}

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

package com.googlesource.gerrit.plugins.cookbook.fortune;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

public class FortuneGame {

  private static final Logger log = LoggerFactory.getLogger(FortuneGame.class);
  private static final String FORTUNE = "fortune";

  public static String getMyFortune() {
    try {
      return fortune();
    } catch (IOException e) {
      log.warn("Don't have fortune today: ", e);
      return "Well, there is no fortunte to obtain fortunte today ;-(";
    }
  }

  private static String fortune() throws IOException {
    ProcessBuilder proc = new ProcessBuilder(FORTUNE)
        .redirectErrorStream(true);
    Process fortune = proc.start();
    byte[] out;
    InputStream in = fortune.getInputStream();
    try {
      out = ByteStreams.toByteArray(in);
    } finally {
      fortune.getOutputStream().close();
      in.close();
    }
    int status;
    try {
      status = fortune.waitFor();
    } catch (InterruptedException e) {
      throw new InterruptedIOException("interrupted waiting for " + FORTUNE);
    }
    String res = new String(out, Charsets.UTF_8);
    if (status != 0) {
      throw new IOException(res);
    }
    return res;
  }
}

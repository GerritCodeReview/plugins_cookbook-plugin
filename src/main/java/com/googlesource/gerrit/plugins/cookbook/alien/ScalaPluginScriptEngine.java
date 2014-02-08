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

package com.googlesource.gerrit.plugins.cookbook.alien;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import scala.Option;
import scala.collection.Iterator;
import scala.collection.mutable.Map;
import scala.reflect.io.AbstractFile;
import scala.tools.nsc.Settings;
import scala.tools.nsc.interpreter.IMain;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class ScalaPluginScriptEngine {
  private final IMain scalaEngine;
  private final ScalaClassLoader classLoader;

  public class ScalaClassLoader extends ClassLoader {
    private Map<String, AbstractFile> scalaClasses;

    public ScalaClassLoader(final IMain scalaEngine) {
      super(ScalaClassLoader.class.getClassLoader());
      scalaClasses =
          scalaEngine.virtualDirectory()
              .scala$reflect$io$VirtualDirectory$$files();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      Option<AbstractFile> classFile = scalaClasses.get(name + ".class");
      if (classFile.isEmpty()) {
        throw new ClassNotFoundException("Cannot find Scala class " + name);
      }

      byte[] ba;
      try {
        ba = classFile.get().toByteArray();
        return defineClass(name, ba, 0, ba.length);
      } catch (IOException e) {
        throw new ClassNotFoundException("Cannot open Scala class file "
            + classFile.get(), e);
      }
    }

    public Set<String> getAllLoadedClassNames() {
      Set<String> classNames = Sets.newHashSet();
      for (Iterator<String> keysIter = scalaClasses.keys().iterator(); keysIter
          .hasNext();) {
        String classFileName = keysIter.next();
        classNames
            .add(StringUtils.substringBeforeLast(classFileName, ".class"));
      }
      return classNames;
    }
  }

  @Inject
  public ScalaPluginScriptEngine() {
    Settings settings = new Settings();
    settings.classpath().tryToSetFromPropertyValue(bootstrapClasspath());
    settings.usejavacp().tryToSetFromPropertyValue("true");
    scalaEngine = new IMain(settings);
    classLoader = new ScalaClassLoader(scalaEngine);
  }

  private String bootstrapClasspath() {
    StringBuilder classPath = new StringBuilder();
    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
    if (cl instanceof java.net.URLClassLoader) {
      @SuppressWarnings("resource")
      URLClassLoader ucl = (URLClassLoader) cl;
      for (URL u : ucl.getURLs()) {
        if ("file".equals(u.getProtocol())) {
          if (classPath.length() > 0) {
            classPath.append(java.io.File.pathSeparatorChar);
          }
          classPath.append(u);
        }
      }
    }
    return classPath.toString();
  }

  public Set<Class<?>> eval(File scalaFile) throws IOException, ClassNotFoundException {
    Set<Class<?>> classes = Sets.newHashSet();

    if (!scalaEngine.compileString(readScalaFile(scalaFile))) {
      throw new IOException("Invalid Scala file " + scalaFile);
    }

    for (String className : classLoader.getAllLoadedClassNames()) {
      Class<?> clazz = classLoader.loadClass(className);
      classes.add(clazz);
    }
    return classes;
  }

  private String readScalaFile(File scalaFile) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(scalaFile));
    StringBuilder scalaCode = new StringBuilder();
    try {
      String line;
      while(null != (line = reader.readLine())) {
        scalaCode.append(line);
        scalaCode.append("\n");
      }
    } finally {
       reader.close();
    }
    return scalaCode.toString();
  }

  public ScalaClassLoader getClassLoader() {
    return classLoader;
  }
}

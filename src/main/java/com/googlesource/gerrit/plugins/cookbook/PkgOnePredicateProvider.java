package com.googlesource.gerrit.plugins.cookbook;

import com.google.common.collect.ImmutableSet;
import com.google.gerrit.extensions.annotations.Listen;
import com.google.gerrit.rules.PredicateProvider;

@Listen
public class PkgOnePredicateProvider implements PredicateProvider {

  @Override
  public ImmutableSet<String> getPackages() {
    return ImmutableSet.of("pkg_one");
  }
}


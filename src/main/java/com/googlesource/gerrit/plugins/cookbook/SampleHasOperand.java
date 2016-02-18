// Copyright (C) 2016 The Android Open Source Project
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

package com.google.gerrit.plugins;

import com.google.gerrit.extensions.annotations.Exports;
import com.google.gerrit.server.query.OperatorPredicate;
import com.google.gerrit.server.query.Predicate;
import com.google.gerrit.server.query.QueryParseException;
import com.google.gerrit.server.query.change.ChangeData;
import com.google.gerrit.server.query.change.ChangeQueryBuilder;
import com.google.gerrit.server.query.change.ChangeQueryBuilder.ChangeHasOperandFactory;
import com.google.gwtorm.server.OrmException;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

@Singleton
public class SampleHasOperand implements ChangeHasOperandFactory {
  public static class HasEvenPredicate extends OperatorPredicate<ChangeData> {
    HasEvenPredicate() {
      super(ChangeQueryBuilder.FIELD_HAS, "even");
    }

    @Override
    public boolean match(final ChangeData change) throws OrmException {
      return (change.getId().get() % 2 == 0);
    }

    @Override
    public int getCost() {
      return 1;
    }
  }

  Override
  public Predicate<ChangeData> create(ChangeQueryBuilder builder)
      throws QueryParseException {
    return new HasEvenPredicate();
  }
}


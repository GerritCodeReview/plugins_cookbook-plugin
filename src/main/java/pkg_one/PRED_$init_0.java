/*
 * Copyright 2014 CollabNet, Inc. All rights reserved. http://www.collab.net
 */
package pkg_one; 

import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.PrologException;

public class PRED_$init_0 extends Predicate.P1 {

  public PRED_$init_0(Operation n) {
    cont = n;
  }

  @Override
  public Operation exec(Prolog engine) throws PrologException {
    engine.setB0();
    return cont;
  }
}

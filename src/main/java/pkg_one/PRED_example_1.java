package pkg_one;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.prolog_cafe.lang.Operation;
import com.googlecode.prolog_cafe.lang.Predicate;
import com.googlecode.prolog_cafe.lang.Prolog;
import com.googlecode.prolog_cafe.lang.PrologException;
import com.googlecode.prolog_cafe.lang.Term;

public class PRED_example_1 extends Predicate.P1 {

  private static Logger LOG = LoggerFactory.getLogger(PRED_example_1.class);

  public PRED_example_1(Term term, Operation op) {
    this.arg1 = term;
    this.cont = op;
  }

  @Override
  public Operation exec(Prolog engine) throws PrologException {

    engine.cont = cont;
    engine.setB0();
    LOG.info("Executing example predicate");
    return engine.cont;
  }
}

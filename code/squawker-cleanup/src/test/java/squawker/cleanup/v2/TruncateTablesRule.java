package squawker.cleanup.v2;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.skife.jdbi.v2.DBI;

// tag::rule-impl[]
public class TruncateTablesRule implements TestRule {

  private final DBI dbi;

  public TruncateTablesRule(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public Statement apply(final Statement base, Description description) { // <1>
    return new Statement() { // <2>
      @Override public void evaluate() throws Throwable {
        try {
          base.evaluate(); // <3>
        } finally {
          truncateTables(); // <4>
        }
      }
    };
  }

  private void truncateTables() {
    dbi.withHandle(handle -> {
      handle.execute("delete from following");
      handle.execute("delete from message");
      handle.execute("delete from user");
      return null;
    });
  }
}
// end::rule-impl[]

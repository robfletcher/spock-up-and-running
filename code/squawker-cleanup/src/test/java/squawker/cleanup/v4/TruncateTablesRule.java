package squawker.cleanup.v4;

import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import static com.oreilly.spock.extension.db.TableTruncator.truncateTables;

// tag::generic-rule[]
public class TruncateTablesRule extends ExternalResource {

  private final DBI dbi;

  public TruncateTablesRule(DBI dbi) {
    this.dbi = dbi;
  }

  @Override protected void after() {
    dbi.withHandle(handle -> {
      truncateTables(handle.getConnection());
      return null; // <1>
    });
  }
}
// end::generic-rule[]

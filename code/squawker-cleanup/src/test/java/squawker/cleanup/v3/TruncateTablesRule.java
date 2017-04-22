package squawker.cleanup.v3;

import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;

// tag::external-resource[]
public class TruncateTablesRule extends ExternalResource {

  private final DBI dbi;

  public TruncateTablesRule(DBI dbi) {
    this.dbi = dbi;
  }

  @Override protected void after() {
    dbi.withHandle(handle -> {
      handle.execute("delete from following");
      handle.execute("delete from message");
      handle.execute("delete from user");
      return null;
    });
  }
}
// end::external-resource[]

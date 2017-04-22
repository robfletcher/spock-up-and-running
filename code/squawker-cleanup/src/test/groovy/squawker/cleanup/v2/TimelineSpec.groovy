package squawker.cleanup.v2

import org.junit.Rule
import org.skife.jdbi.v2.DBI
import squawker.Specification

class TimelineSpec extends Specification {

  DBI dbi = new DBI(dataSource)

  // tag::rule-declaration[]
  @Rule TruncateTablesRule dataCleanup = new TruncateTablesRule(dbi)
  // end::rule-declaration[]

}


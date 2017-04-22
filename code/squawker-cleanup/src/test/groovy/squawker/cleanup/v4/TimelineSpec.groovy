package squawker.cleanup.v4

import org.junit.Rule
import org.skife.jdbi.v2.DBI
import squawker.Specification

class TimelineSpec extends Specification {

  DBI dbi = new DBI(dataSource)

  @Rule TruncateTablesRule dataCleanup = new TruncateTablesRule(dbi)

}


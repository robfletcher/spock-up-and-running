package squawker.cleanup.v5

import org.skife.jdbi.v2.DBI
import squawker.Specification

class TimelineSpec extends Specification {

  // tag::extension-declaration[]
  @TruncateTables
  DBI dbi = new DBI(dataSource)
  // end::extension-declaration[]

}

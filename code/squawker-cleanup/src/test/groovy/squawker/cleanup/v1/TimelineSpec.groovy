package squawker.cleanup.v1

import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import squawker.Specification

class TimelineSpec extends Specification {

  // tag::data-cleanup-1[]
  DBI dbi = new DBI(dataSource)
  // end::data-cleanup-1[]

  // tag::data-cleanup-2[]
  def cleanup() {
    dbi.withHandle { Handle handle ->
      handle.execute("delete from following")
      handle.execute("delete from message")
      handle.execute("delete from user")
    }
  }
  // end::data-cleanup-2[]
}

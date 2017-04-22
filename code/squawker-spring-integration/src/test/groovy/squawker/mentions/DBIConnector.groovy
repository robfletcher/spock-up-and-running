package squawker.mentions

import java.sql.Connection
import com.oreilly.spock.TypedConnector
import org.skife.jdbi.v2.DBI

class DBIConnector extends TypedConnector<DBI> {
  DBIConnector() { super(DBI) }

  @Override
  protected Connection apply(DBI source) {
    source.open().connection
  }
}

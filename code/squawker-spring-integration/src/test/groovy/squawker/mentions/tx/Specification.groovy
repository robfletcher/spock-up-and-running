package squawker.mentions.tx

import com.oreilly.spock.TruncateTables
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.tweak.HandleCallback
import org.springframework.beans.factory.annotation.Autowired
import squawker.mentions.DBIConnector
import squawker.mentions.TableHelper

abstract class Specification extends spock.lang.Specification implements TableHelper {

  @Autowired @TruncateTables(DBIConnector) DBI dbi

  @Override
  <R> R withHandle(HandleCallback<R> closure) {
    dbi.withHandle(closure)
  }
}


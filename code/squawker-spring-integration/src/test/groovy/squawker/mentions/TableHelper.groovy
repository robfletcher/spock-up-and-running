package squawker.mentions

import org.skife.jdbi.v2.tweak.HandleCallback
import org.skife.jdbi.v2.util.IntegerColumnMapper

trait TableHelper {

  abstract <R> R withHandle(HandleCallback<R> closure)

  int count(String tableName) {
    withHandle { handle ->
      handle
        .createQuery("select count(*) from $tableName")
        .map(IntegerColumnMapper.PRIMITIVE)
        .first()
    }
  }
}

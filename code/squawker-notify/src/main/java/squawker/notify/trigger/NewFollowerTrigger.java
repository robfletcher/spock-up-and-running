package squawker.notify.trigger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import com.google.common.eventbus.EventBus;
import org.h2.api.Trigger;
import org.skife.jdbi.v2.DBI;
import squawker.User;
import squawker.jdbi.UserStore;
import squawker.notify.NewFollowerEvent;

public class NewFollowerTrigger implements Trigger {

  private final EventBus eventBus;

  public NewFollowerTrigger() {
    this.eventBus = TriggerInit.eventBus();
  }

  @Override
  public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
  }

  @Override
  public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
    UserStore userStore = DBI.open(conn).attach(UserStore.class);
    User user = userStore.get((Serializable) newRow[1]);
    User newFollower = userStore.get((Serializable) newRow[0]);
    eventBus.post(new NewFollowerEvent(user, newFollower));
  }

  @Override public void close() throws SQLException {

  }

  @Override public void remove() throws SQLException {

  }
}

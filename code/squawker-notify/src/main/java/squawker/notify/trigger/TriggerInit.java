package squawker.notify.trigger;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.eventbus.EventBus;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import static java.lang.String.format;

public class TriggerInit implements Closeable {

  private static AtomicReference<EventBus> eventBus = new AtomicReference<>();
  private final DBI dbi;

  TriggerInit(EventBus eventBus, DBI dbi) {
    TriggerInit.eventBus.set(eventBus);
    this.dbi = dbi;
  }

  public void init() {
    dbi.useHandle(this::createTrigger);
  }

  @Override
  public void close() {
    dbi.useHandle(this::dropTrigger);
  }

  private void createTrigger(Handle handle) {
    handle.execute(
      format(
        "create trigger following_insert after insert on following for each row call \"%s\"",
        NewFollowerTrigger.class.getName()
      )
    );
  }

  private void dropTrigger(Handle handle) {
    handle.execute("drop trigger following_insert if exists");
  }

  static EventBus eventBus() {
    return eventBus.get();
  }
}

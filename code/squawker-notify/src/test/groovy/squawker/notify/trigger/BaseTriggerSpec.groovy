package squawker.notify.trigger

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import com.google.common.eventbus.AsyncEventBus
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import squawker.jdbi.FollowingStore
import squawker.jdbi.UserStore
import static java.util.concurrent.Executors.newSingleThreadExecutor
import static java.util.concurrent.TimeUnit.SECONDS

abstract class BaseTriggerSpec extends Specification {

  @Shared dbi = new DBI("jdbc:h2:mem:test")
  @Shared Handle handle
  @Shared UserStore userStore
  @Shared FollowingStore followingStore

  // tag::event-bus[]
  def executor = newSingleThreadExecutor()
  def eventBus = new AsyncEventBus(executor) // <1>
  // end::event-bus[]
  @AutoCleanup def triggerInit = new TriggerInit(eventBus, dbi)

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())

    handle = dbi.open()
    userStore = handle.attach(UserStore)
    followingStore = handle.attach(FollowingStore)
    userStore.createUserTable();
    followingStore.createFollowingTable();
  }

  def cleanupSpec() {
    handle.execute("drop table following if exists")
    handle.execute("drop table user if exists")
    handle.close()
  }

  def setup() {
    triggerInit.init()
  }

  def cleanup() {
    executor.shutdownNow()
    assert executor.awaitTermination(1, SECONDS)
    handle.execute("delete from following")
    handle.execute("delete from user")
  }

}

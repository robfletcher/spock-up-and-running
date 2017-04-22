package squawker.grammar

import co.freeside.jdbi.time.TimeTypesArgumentFactory
import co.freeside.jdbi.time.TimeTypesMapperFactory
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import spock.lang.AutoCleanup
import spock.lang.Shared
import squawker.helpers.FixturesDelegate
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore

class Specification extends spock.lang.Specification {

  @Shared dbi = new DBI("jdbc:h2:mem:test")

  def setupSpec() {
    dbi.registerArgumentFactory(new TimeTypesArgumentFactory())
    dbi.registerMapper(new TimeTypesMapperFactory())
  }

  @AutoCleanup Handle handle
  UserStore userStore
  MessageStore messageStore
  FollowingStore followingStore

  @Delegate FixturesDelegate fixtures

  def setup() {
    handle = dbi.open()

    userStore = handle.attach(UserStore)
    userStore.createUserTable()

    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()

    fixtures = new FixturesDelegate(
      messageStore,
      userStore,
      followingStore,
      null
    )
  }

  def cleanup() {
    handle.execute("drop table user if exists")
    handle.execute("drop table message if exists")
    handle.execute("drop table following if exists")
  }

}

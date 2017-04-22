package squawker.jdbi.async

import spock.lang.Shared
import spock.lang.Subject
import squawker.jdbi.BasePersistenceSpec
import squawker.jdbi.FollowingStore
import squawker.jdbi.MessageStore
import static java.util.concurrent.Executors.newSingleThreadExecutor

abstract class BaseAsyncMessageStoreSpec extends BasePersistenceSpec {

  @Shared MessageStore messageStore
  @Shared FollowingStore followingStore

  def setupSpec() {
    messageStore = handle.attach(MessageStore)
    messageStore.createMessageTable()

    followingStore = handle.attach(FollowingStore)
    followingStore.createFollowingTable()
  }

  def cleanupSpec() {
    handle.execute("drop table message if exists")
    handle.execute("drop table following if exists")
  }

  @Subject AsyncMessageStore asyncMessageStore

  def setup() {
    messageStore = handle.attach(MessageStore)
    asyncMessageStore = new AsyncMessageStore(messageStore, newSingleThreadExecutor())
  }

  def cleanup() {
    handle.execute("delete from message")
    handle.execute("delete from following")
  }

}

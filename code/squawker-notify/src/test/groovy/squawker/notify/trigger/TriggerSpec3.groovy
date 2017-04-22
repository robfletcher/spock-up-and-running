package squawker.notify.trigger

import com.google.common.eventbus.Subscribe
import groovy.transform.CompileStatic
import spock.util.concurrent.BlockingVariable
import squawker.notify.NewFollowerEvent

class TriggerSpec3 extends BaseTriggerSpec {

  @CompileStatic
  // tag::no-stub-required[]
  static class AsyncSubscriber<T> extends BlockingVariable<T> {
    @Subscribe
    @Override
    void set(T event) {
      super.set(event)
    }
  }

  def "publishes event when a user follows another"() {
    given:
    def user1 = userStore.insert(username1)
    def user2 = userStore.insert(username2)

    and:
    def subscriber = new AsyncSubscriber<NewFollowerEvent>()
    eventBus.register(subscriber)

    when:
    user2.follow(user1)

    then:
    with(subscriber.get()) {
      user == user1
      newFollower == user2
    }

    where:
    username1 = "spock"
    username2 = "kirk"
  }
  // end::no-stub-required[]
}

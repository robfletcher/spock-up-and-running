package squawker.notify.trigger

import com.google.common.eventbus.Subscribe
import spock.util.concurrent.BlockingVariable
import squawker.notify.NewFollowerEvent

class TriggerSpec2 extends BaseTriggerSpec {

  interface Subscriber {
    @Subscribe
    void onEvent(NewFollowerEvent event)
  }

  // tag::blocking-argument-captor[]
  def "publishes event when a user follows another"() {
    given:
    def user1 = userStore.insert(username1)
    def user2 = userStore.insert(username2)

    and:
    def event = new BlockingVariable<NewFollowerEvent>() // <1>
    def subscriber = Stub(Subscriber) {
      onEvent(_) >> { event.set(it[0]) } // <2>
    }
    eventBus.register(subscriber)

    when:
    user2.follow(user1)

    then:
    with(event.get()) { // <3>
      user == user1
      newFollower == user2
    }

    where:
    username1 = "spock"
    username2 = "kirk"
  }
  // end::blocking-argument-captor[]
}

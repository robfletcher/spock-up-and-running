package squawker.notify.trigger

import com.google.common.eventbus.Subscribe
import org.spockframework.mock.TooFewInvocationsError
import spock.lang.FailsWith
import spock.lang.Ignore
import squawker.notify.NewFollowerEvent

class TriggerSpec extends BaseTriggerSpec {

  // tag::async-mock-interaction[]
  interface Subscriber {
    @Subscribe
    // <2>
    void onEvent(NewFollowerEvent event)
  }

  // end::async-mock-interaction[]
  @Ignore
  // fails inconsistently
  @FailsWith(TooFewInvocationsError)
  // tag::async-mock-interaction[]
  def "publishes event when a user follows another"() {
    given:
    def user1 = userStore.insert(username1)
    def user2 = userStore.insert(username2)

    and:
    def subscriber = Mock(Subscriber)
    eventBus.register(subscriber) // <3>

    when:
    user2.follow(user1)

    then:
    1 * subscriber.onEvent(new NewFollowerEvent(user1, user2)) // <4>

    where:
    username1 = "spock"
    username2 = "kirk"
  }
  // end::async-mock-interaction[]

  def "sleepy version"() {
    given:
    def user1 = userStore.insert(username1)
    def user2 = userStore.insert(username2)

    and:
    def subscriber = Mock(Subscriber)
    eventBus.register(subscriber)

    // tag::sleepy-version[]
    when:
    user2.follow(user1)

    and:
    sleep 1000 // <1>

    then:
    1 * subscriber.onEvent(new NewFollowerEvent(user1, user2))
    // end::sleepy-version[]

    where:
    username1 = "spock"
    username2 = "kirk"
  }
}

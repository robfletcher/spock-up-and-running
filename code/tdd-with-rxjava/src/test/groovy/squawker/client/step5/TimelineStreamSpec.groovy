/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package squawker.client.step5

import rx.functions.Action1
import rx.schedulers.Schedulers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import squawker.Message
import squawker.User
import squawker.client.SquawkerApi
import static java.time.Instant.now
import static java.util.concurrent.TimeUnit.SECONDS

class TimelineStreamSpec extends Specification {

  @Shared interval = 5
  def scheduler = Schedulers.test()
  def squawker = Mock(SquawkerApi)
  def subscriber = Mock(Action1) // <1>
  @Subject timeline = new TimelineStream(
    scheduler, "spock", interval, SECONDS, squawker, subscriber
  )

  @Shared user = new User("spock")

  // tag::no-new-messages[]
  def "if no new messages are received the subscriber is not called"() {
    given:
    squawker.getTimeline(*_) >> [] // <1>

    when:
    timeline.start()
    waitForTicks(1)

    then:
    0 * subscriber.call(_) // <2>
  }
  // end::no-new-messages[]

  def "passes each message to the subscriber"() {
    given:
    squawker.getTimeline(*_) >> [message1] >> [message2]

    when:
    timeline.start()
    waitForTicks(ticks)

    // tag::unchunked-messages[]
    then:
    1 * subscriber.call(message1)
    1 * subscriber.call(message2)
    // end::unchunked-messages[]

    where:
    ticks = 2
    message1 = new Message(
      1L, user, "fascinating", now()
    )
    message2 = new Message(
      2L, user, "The complexities of human pranks escape me", now()
    )
  }

  // tag::coalesced-stream[]
  def "transforms chunked messages into a continuous stream"() {
    given:
    squawker.getTimeline(*_) >> [message1] >> [] >> [message2, message3] // <1>

    when:
    timeline.start()
    waitForTicks(chunks)

    then: // <2>
    1 * subscriber.call(message1)
    1 * subscriber.call(message2)
    1 * subscriber.call(message3)

    and:
    0 * subscriber.call(_) // <3>

    where:
    chunks = 3
    message1 = new Message(
      1L, user, "fascinating", now()
    )
    message2 = new Message(
      2L, user, "The complexities of human pranks escape me", now()
    )
    message3 = new Message(
      3L, user,
      "the statistical likelihood that our plan will succeed is less than 4.3%",
      now()
    )
  }
  // end::coalesced-stream[]

  private void waitForTicks(int ticks) {
    scheduler.advanceTimeBy(ticks * interval, SECONDS)
  }
}

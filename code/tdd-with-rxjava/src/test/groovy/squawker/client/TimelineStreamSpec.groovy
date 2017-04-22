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

package squawker.client

import rx.functions.Action1
import rx.schedulers.Schedulers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import squawker.Message
import squawker.User
import static java.time.Instant.now
import static java.util.concurrent.TimeUnit.SECONDS

class TimelineStreamSpec extends Specification {

  @Shared interval = 5
  def scheduler = Schedulers.test()
  def squawker = Mock(SquawkerApi)
  def subscriber = Mock(Action1)
  @Subject timeline = new TimelineStream(
    scheduler, "spock", interval, SECONDS, squawker, subscriber
  )

  @Shared user = new User("spock")

  @Unroll
  def "polls Squawker #ticks times in #delay seconds"() {
    when:
    timeline.start()
    scheduler.advanceTimeBy(delay, SECONDS)

    then:
    ticks * squawker.getTimeline(*_)

    where:
    ticks << [2, 3]
    delay = ticks * interval
  }

  def "passes each message to the subscriber"() {
    given:
    squawker.getTimeline(*_) >> [message1] >> [message2]

    when:
    timeline.start()
    scheduler.advanceTimeBy(ticks * interval, SECONDS)

    then:
    1 * subscriber.call(message1)
    1 * subscriber.call(message2)

    where:
    ticks = 2
    message1 = new Message(1L, user, "fascinating", now())
    message2 = new Message(2L, user, "The complexities of human pranks escape me", now())
  }

  def "if no new messages are received the subscriber is not called"() {
    given:
    squawker.getTimeline(*_) >> []

    when:
    timeline.start()
    scheduler.advanceTimeBy(interval, SECONDS)

    then:
    0 * subscriber.call(_)
  }

  def "transforms chunked messages into a continuous stream"() {
    given:
    squawker.getTimeline(*_) >> [message1] >> [] >> [message2, message3]

    when:
    timeline.start()
    scheduler.advanceTimeBy(chunks * interval, SECONDS)

    then:
    1 * subscriber.call(message1)
    1 * subscriber.call(message2)
    1 * subscriber.call(message3)

    and:
    0 * subscriber.call(_)

    where:
    chunks = 3
    message1 = new Message(1L, user, "fascinating", now())
    message2 = new Message(2L, user, "The complexities of human pranks escape me", now())
    message3 = new Message(3L, user, "the statistical likelihood that our plan will succeed is less than 4.3%", now())
  }

  def "passes the id of the last message seen on each poll"() {
    when:
    timeline.start()
    scheduler.advanceTimeBy(ticks * interval, SECONDS)

    then:
    1 * squawker.getTimeline(_, null) >> [message1]
    1 * squawker.getTimeline(_, message1.id) >> [message2, message3]
    1 * squawker.getTimeline(_, message3.id)

    where:
    ticks = 3
    message1 = new Message(1L, user, "fascinating", now())
    message2 = new Message(2L, user, "The complexities of human pranks escape me", now())
    message3 = new Message(3L, user, "the statistical likelihood that our plan will succeed is less than 4.3%", now())
  }

  def "continues polling if the API experiences an error"() {
    given:
    squawker.getTimeline(*_) >> { throw unavailable() } >> [message] // <1>

    when:
    timeline.start()
    scheduler.advanceTimeBy(2 * interval, SECONDS)

    then:
    1 * subscriber.call(message) // <2>

    where:
    message = new Message(1L, user, "fascinating", now())
  }

  def "stops processing if the subscriber throws an unrecoverable error"() {
    given:
    subscriber.call(_) >> { throw new RuntimeException("unrecoverable") }

    when:
    timeline.start()
    scheduler.advanceTimeBy(2 * interval, SECONDS)

    then:
    1 * squawker.getTimeline(*_) >> [message]

    where:
    message = new Message(1L, user, "fascinating", now())
  }

  def "re-tries the chunk if the subscriber throws an recoverable error"() {
    given:
    subscriber.call(_) >> { throw new RecoverableSubscriberException() }

    and:
    def messageIds = []
    squawker.getTimeline(*_) >> { username, messageId ->
      messageIds << messageId
      [message]
    }

    when:
    timeline.start()
    scheduler.advanceTimeBy(2 * interval, SECONDS)

    then:
    messageIds[0] == messageIds[1]

    where:
    message = new Message(1L, user, "fascinating", now())
  }

  Throwable unavailable() {
    throw new RuntimeException("HTTP 503: Service Unavailable")
  }
}

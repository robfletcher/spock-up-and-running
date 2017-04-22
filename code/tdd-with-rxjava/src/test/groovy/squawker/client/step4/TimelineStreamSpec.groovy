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

package squawker.client.step4

import rx.exceptions.OnErrorNotImplementedException
import rx.functions.Action1
import rx.schedulers.Schedulers
import spock.lang.*
import squawker.Message
import squawker.User
import squawker.client.SquawkerApi
import static java.time.Instant.now
import static java.util.concurrent.TimeUnit.SECONDS

class TimelineStreamSpec extends Specification {

  @Shared interval = 5
  def scheduler = Schedulers.test()
  def squawker = Mock(SquawkerApi)

  // tag::helper-method[]
  @Unroll
  def "polls Squawker #ticks times in #delay seconds"() {
    when:
    timeline.start()
    waitForTicks(ticks) // <1>

    then:
    ticks * squawker.getTimeline(*_)

    where:
    ticks << [2, 3]
    delay = ticks * interval
  }

  private void waitForTicks(int ticks) {
    scheduler.advanceTimeBy(ticks * interval, SECONDS)
  }
  // end::helper-method[]

  // tag::mock-subscriber[]
  def subscriber = Mock(Action1) // <1>
  @Subject timeline = new TimelineStream(
    scheduler, "spock", interval, SECONDS, squawker, subscriber
  )

  @Shared user = new User("spock")

  def "passes each message to the subscriber"() {
    given:
    squawker.getTimeline(*_) >> [message1] >> [message2] // <2>

    when:
    timeline.start()
    waitForTicks(ticks)

    then: // <3>
    1 * subscriber.call([message1])
    1 * subscriber.call([message2])

    where:
    ticks = 2
    message1 = new Message(1L, user, "fascinating", now())
    message2 = new Message(2L, user, "The complexities of human pranks escape me", now())
  }
  // end::mock-subscriber[]

  @FailsWith(OnErrorNotImplementedException)
  def "if no new messages are received the subscriber is not called"() {
    given:
    squawker.getTimeline(*_) >> []

    when:
    timeline.start()
    waitForTicks(1)

    then:
    0 * subscriber.call(_)
  }
}

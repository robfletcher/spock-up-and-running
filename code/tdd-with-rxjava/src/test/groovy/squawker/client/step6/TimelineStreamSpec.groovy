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

package squawker.client.step6

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

  // tag::last-message-id[]
  def "passes the id of the last message seen on each poll"() {
    when:
    timeline.start()
    waitForTicks(ticks)

    then:
    1 * squawker.getTimeline(_, null) >> [message1] // <1>
    1 * squawker.getTimeline(_, message1.id) >> [message2, message3] // <2>
    1 * squawker.getTimeline(_, message3.id) // <3>

    where:
    ticks = 3
    message1 = new Message(1L, user, "fascinating", now())
    message2 = new Message(2L, user, "The complexities of human pranks escape me", now())
    message3 = new Message(3L, user, "the statistical likelihood that our plan will succeed is less than 4.3%", now())
  }
  // end::last-message-id[]

  private void waitForTicks(int ticks) {
    scheduler.advanceTimeBy(ticks * interval, SECONDS)
  }
}

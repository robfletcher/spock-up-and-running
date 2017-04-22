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

package squawker.client.step7

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

  // tag::resilience[]
  def "continues polling if the API experiences an error"() {
    given:
    squawker.getTimeline(*_) >> { throw unavailable() } >> [message] // <1>

    when:
    timeline.start()
    waitForTicks(2)

    then:
    1 * subscriber.call(message) // <2>

    where:
    message = new Message(1L, user, "fascinating", now())
  }

  Throwable unavailable() { // <3>
    throw new RuntimeException("HTTP 503: Service Unavailable")
  }
  // end::resilience[]

  private void waitForTicks(int ticks) {
    scheduler.advanceTimeBy(ticks * interval, SECONDS)
  }
}

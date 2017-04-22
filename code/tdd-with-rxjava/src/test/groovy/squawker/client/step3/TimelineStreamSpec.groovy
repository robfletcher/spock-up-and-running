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

package squawker.client.step3

import rx.schedulers.Schedulers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import squawker.client.SquawkerApi
import static java.util.concurrent.TimeUnit.SECONDS

class TimelineStreamSpec extends Specification {

  @Shared interval = 5
  def squawker = Mock(SquawkerApi)

  // tag::test-scheduler[]
  def scheduler = Schedulers.test() // <1>
  @Subject timeline = new TimelineStream(
    scheduler, "spock", interval, SECONDS, squawker
  )

  @Unroll
  def "polls Squawker #ticks times in #delay seconds"() {
    when:
    timeline.start()
    scheduler.advanceTimeBy(delay, SECONDS) // <2>

    then:
    ticks * squawker.getTimeline(*_)

    where:
    ticks << [2, 3]
    delay = ticks * interval
  }
  // end::test-scheduler[]
}

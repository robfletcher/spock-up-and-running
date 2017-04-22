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

package squawker.client.step1

import org.spockframework.runtime.SpockAssertionError
import spock.lang.*
import squawker.client.SquawkerApi
import static java.util.concurrent.TimeUnit.SECONDS

@FailsWith(SpockAssertionError)
class TimelineStreamSpec extends Specification {

  static void sleep(long millis) {
    // no-op because we don't really want to sleep
  }

  // tag::first-test[]
  @Shared interval = 1 // <1>
  def squawker = Mock(SquawkerApi) // <2>
  @Subject timeline = new TimelineStream("spock", interval, SECONDS, squawker)
  // <3>

  @Unroll
  def "polls Squawker #ticks times in #delay seconds"() {
    when:
    timeline.start() // <4>
    sleep SECONDS.toMillis(delay) // <5>

    then:
    ticks * squawker.getTimeline(*_) // <6>

    where:
    ticks << [2, 3] // <7>
    delay = ticks * interval // <8>
  }
  // end::first-test[]
}

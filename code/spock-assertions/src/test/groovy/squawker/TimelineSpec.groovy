/*
 * Copyright 2013 the original author or authors.
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
package squawker

import java.time.*
import spock.lang.*
import static java.time.Instant.*
import static java.time.ZoneOffset.*
import static java.time.temporal.ChronoUnit.*

class TimelineSpec extends Specification {

  // <1>
  @Subject user = new User("khan")
  def followedUser = new User("kirk")
  def otherUser = new User("spock")

  // <2>
  def setup() {
    user.follow(followedUser) // <3>

    def now = Instant.now()
    // <4>
    postMessage(otherUser, now.minus(6, MINUTES),
      "His pattern indicates two-dimensional thinking.")
    postMessage(user, now.minus(5, MINUTES),
      "@kirk You're still alive, my old friend?")
    postMessage(followedUser, now.minus(4, MINUTES),
      "@khan KHAAANNNN!")
    postMessage(followedUser, now.minus(3, MINUTES),
      "@scotty I need warp speed in three minutes or we're all dead!")
    postMessage(otherUser, now.minus(2, MINUTES),
      "@bones I'm sorry, Doctor, I have no time to explain this logically.")
    postMessage(user, now.minus(1, MINUTES),
      "It is very cold in space!")
  }

  def "a user's timeline contains posts from themselves and followed users"() {
    expect:
    with(user.timeline()) {
      size() == 4

      // <5>
      it*.postedBy.every {
        it in [user, followedUser]
      }

      // <6>
      !it*.postedBy.any {
        it == otherUser
      }
    }
  }

  def "a user's timeline is ordered most recent first"() {
    expect:
    with(user.timeline()) {
      postedAt == postedAt.sort().reverse() // <7>
    }
  }

  def "a timeline cannot be modified directly"() {
    when:
    user.timeline() << new Message(user, "@kirk You're still alive, my old friend?", now())

    then:
    thrown(UnsupportedOperationException)
  }

  private void postMessage(User poster, Instant at, String text) {
    def clock = Clock.fixed(at, UTC)
    poster.post(text, clock.instant())
  }
}

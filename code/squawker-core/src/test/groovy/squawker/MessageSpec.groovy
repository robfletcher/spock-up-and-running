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

import java.time.Clock
import spock.lang.Specification
import squawker.time.*
import static java.time.Instant.now
import static java.time.ZoneOffset.UTC

class MessageSpec extends Specification {

  def user = new User("spock")
  def clock = Clock.fixed(now(), UTC)

  def "messages are ordered most recent first"() {
    given:
    def olderMessage = new Message(user, "Fascinating.", clock.instant())

    and:
    def newerMessage = new Message(user, "Live long and prosper.", clock.instant().plusSeconds(1))

    expect:
    newerMessage < olderMessage

    and:
    olderMessage > newerMessage
  }

}

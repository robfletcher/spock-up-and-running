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
import squawker.time.*
import static java.time.Instant.*
import static java.time.ZoneOffset.*
import static java.time.temporal.ChronoUnit.*

class MessageSpec extends Specification {

  // tag::assert-comparable[]
  def user = new User("spock")

  def "messages are ordered most recent first"() {
    given:
    def clock = Clock.fixed(now(), UTC)
    def olderMessage = new Message(user, "Fascinating.", clock.instant())

    and:
    clock = Clock.offset(clock, Duration.of(1, MINUTES))
    def newerMessage = new Message(user, "Live long and prosper.", clock.instant())

    expect:
    newerMessage < olderMessage

    and:
    olderMessage > newerMessage
  }
  // end::assert-comparable[]

}

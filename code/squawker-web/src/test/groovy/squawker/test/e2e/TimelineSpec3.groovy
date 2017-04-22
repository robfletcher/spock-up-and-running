/*
 * Copyright 2016 the original author or authors.
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

package squawker.test.e2e

import squawker.test.e2e.pages1.TimelinePage

class TimelineSpec3 extends BaseWebSpec {
  def "a user can see their own messages in their timeline"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    and:
    loginAs("spock")

    // tag::delegation[]
    when:
    to(TimelinePage)

    then:
    messageText == "Fascinating!"
    postedBy == "@spock"
    // end::delegation[]
  }

  // tag::different-timelines[]
  def "different users see different timelines"() {
    given:
    createUser("kirk")
    createUser("spock")
    createMessage("kirk", "Report, Mr Spock.")
    createMessage("spock", "Fascinating!")

    when:
    loginAs("kirk")

    then:
    at(TimelinePage) // <1>
    messageText == "Report, Mr Spock."
    postedBy == "@kirk"

    when: // <2>
    loginAs("spock")

    then:
    at(TimelinePage)
    messageText == "Fascinating!"
    postedBy == "@spock"
  }
  // end::different-timelines[]
}

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

import squawker.test.e2e.pages1.UserPage
import squawker.test.e2e.pages2.TimelinePage

class TimelineSpec4 extends BaseWebSpec {
  // tag::nested-modules[]
  def "a user can see their own messages in their timeline"() {
    given:
    createUser("spock")
    messages.each {
      createMessage("spock", it)
    }

    and:
    loginAs("spock")

    when:
    to(TimelinePage)

    then:
    timeline.messages.size() == messages.size() // <1>
    timeline.messages[0].text == messages[1] // <2>
    timeline.messages[1].text == messages[0]
    timeline.messages*.postedBy.every { it == "@spock" } // <3>

    where:
    messages = [
      "Fascinating!",
      "I remind you that this is a silicon-based form of life."
    ]
  }
  // end::nested-modules[]

  // tag::user-page-spec[]
  def "a user can see their own messages on their user page"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    when:
    to(UserPage)

    then:
    recent.messages.size() == 1
    recent.messages[0].text == "Fascinating!"
  }
  // end::user-page-spec[]
}

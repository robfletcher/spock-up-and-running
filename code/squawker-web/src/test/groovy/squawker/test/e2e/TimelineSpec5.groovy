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

import squawker.test.e2e.pages2.UserPage

class TimelineSpec5 extends BaseWebSpec {
  def "a user can see their own messages on their user page"() {
    given:
    createUser("spock")
    createMessage("spock", "Fascinating!")

    // tag::to-parameters[]
    when:
    to(UserPage, "spock")
    // end::to-parameters[]

    then:
    recent.messages[0].text == "Fascinating!"
  }
}

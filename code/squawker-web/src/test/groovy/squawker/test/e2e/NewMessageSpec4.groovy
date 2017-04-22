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

import squawker.test.e2e.pages3.NewMessagePage

class NewMessageSpec4 extends BaseWebSpec {

  // tag::validation-errors-1[]
  def "a user cannot post a message with too much text"() {
    given:
    createUser("spock")

    // end::validation-errors-1[]
    and:
    loginAs("spock")

    // tag::validation-errors-2[]
    and:
    to(NewMessagePage)

    when:
    postMessage(text)

    then:
    at(NewMessagePage)
    errors == ["Messages cannot be longer than 140 characters."]

    where:
    text = "You find it easier to understand the death of one than the death " +
      "of a million. You speak about the objective hardness of the Vulcan " +
      "heart, yet how little room there seems to be in yours."
  }
  // end::validation-errors-2[]
}

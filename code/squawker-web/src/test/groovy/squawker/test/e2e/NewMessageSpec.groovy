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

import squawker.test.e2e.pages1.MessagePage
import squawker.test.e2e.pages1.NewMessagePage

class NewMessageSpec extends BaseWebSpec {

  // tag::form-interaction-1[]
  def "a user can post a message"() {
    given:
    createUser("spock")

    // end::form-interaction-1[]
    and:
    loginAs("spock")

    // tag::form-interaction-2[]
    and:
    to(NewMessagePage)

    when:
    textarea.value(text) // <1>
    postButton.click()   // <2>

    then:
    // ...
    // end::form-interaction-2[]
    // tag::verification-page[]
    at(MessagePage)      // <1>
    message.text == text // <2>
    // end::verification-page[]
    // tag::form-interaction-3[]

    where:
    text = "Fascinating!"
  }
  // end::form-interaction-3[]

}

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
import squawker.test.e2e.pages2.NewMessagePage

class NewMessageSpec2 extends BaseWebSpec {

  // tag::form-shortcuts-1[]
  def "a user can post a message"() {
    given:
    createUser("spock")

    // end::form-shortcuts-1[]
    and:
    loginAs("spock")

    // tag::form-shortcuts-2[]
    and:
    to(NewMessagePage)

    when:
    form.text = text      // <1>
    form.submit().click() // <2>

    then:
    at(MessagePage)
    message.text == text

    where:
    text = "Fascinating!"
  }
  // end::form-shortcuts-2[]

}

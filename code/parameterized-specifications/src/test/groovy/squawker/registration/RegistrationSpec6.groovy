/*
 * Copyright 2014 the original author or authors.
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
package squawker.registration

import spock.lang.*
import squawker.*
import squawker.jdbi.*

class RegistrationSpec6 extends Specification {

  def dataStore = Mock(DataStore)
  @Subject service = new RegistrationService(dataStore)

  // tag::multiple-where-parameters[]
  @Unroll
  def "a new user cannot register with the username '#username'"() {
    given:
    dataStore.usernameInUse("spock") >> true

    when:
    service.register(username)

    then:
    thrown(exceptionType) // <2>

    and:
    0 * dataStore.insert(_ as User)

    where:
    username << [null, "", "     ", "@&%\$+[", "spock"]
    exceptionType << [
      MissingUsernameException,
      MissingUsernameException,
      InvalidCharactersInUsernameException,
      InvalidCharactersInUsernameException,
      UsernameAlreadyInUseException
    ] // <1>
  }
  // end::multiple-where-parameters[]
}

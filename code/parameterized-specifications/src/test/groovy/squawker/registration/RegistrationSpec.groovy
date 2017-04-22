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

import spock.lang.Specification
import spock.lang.Subject
import squawker.User
import squawker.jdbi.DataStore

class RegistrationSpec extends Specification {

  def dataStore = Mock(DataStore)
  @Subject service = new RegistrationService(dataStore)

  def "a valid registration inserts a user to the database"() {
    when:
    def user = service.register("spock")

    then:
    1 * dataStore.insert(_ as User)

    and:
    user.username == "spock"
  }

  // tag::naive-specs[]
  def "a new user cannot register with a null username"() {
    when:
    service.register(null) // <1>

    then:
    thrown(RegistrationException) // <2>

    and:
    0 * dataStore.insert(_ as User) // <3>
  }

  def "a new user cannot register with a blank username"() {
    when:
    service.register("")

    then:
    thrown(RegistrationException)

    and:
    0 * dataStore.insert(_ as User)
  }

  def "a new user cannot register with an empty username"() {
    when:
    service.register("     ")

    then:
    thrown(RegistrationException)

    and:
    0 * dataStore.insert(_ as User)
  }

  def "a new user cannot register with a username containing illegal characters"() {
    when:
    service.register("@&%\$+[")

    then:
    thrown(RegistrationException)

    and:
    0 * dataStore.insert(_ as User)
  }

  def "a new user cannot register with the same username as an existing user"() {
    given:
    dataStore.usernameInUse("spock") >> true // <4>

    when:
    service.register("spock")

    then:
    thrown(RegistrationException)

    and:
    0 * dataStore.insert(_ as User)
  }
  // end::naive-specs[]
}

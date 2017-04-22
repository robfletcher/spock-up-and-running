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

package squawker.api

import org.skife.jdbi.v2.DBI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification
import squawker.jdbi.UserStore
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.OK

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Main])
class MessageEndpointSpec2 extends Specification {

  @Autowired TestRestTemplate template

  // tag::remote-cleanup[]
  @Autowired DBI dbi

  def cleanup() {
    dbi.open().withCloseable { handle ->
      handle.execute("delete from user")
    }
  }
  // end::remote-cleanup[]

  // tag::test-with-data[]
  @Autowired UserStore userStore // <1>

  def "returns an empty array for a user who has not posted any messages"() {
    given:
    userStore.insert(username) // <2>

    when:
    def response = template.getForEntity("/api/$username/messages", List) // <3>

    then:
    response.statusCode == OK // <4>
    response.body == [] // <5>

    where:
    username = "spock"
  }
  // end::test-with-data[]
}

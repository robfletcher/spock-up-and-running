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

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.NOT_FOUND

// tag::failure-case[]
@SpringBootTest(
  webEnvironment = RANDOM_PORT,
  classes = [Main] // <1>
)
class MessageEndpointSpec extends Specification { // <2>

  @Autowired TestRestTemplate client // <3>

  def "returns a not found response for a non-existent user"() {
    when:
    def entity = client.getForEntity("/api/$username/messages", Map) // <4>

    then:
    entity.statusCode == NOT_FOUND // <5>
    entity.body.message == "No user $username found"

    where:
    username = "spock"
  }
}
// tag::failure-case[]

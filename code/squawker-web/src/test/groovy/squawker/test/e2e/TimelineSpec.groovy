/*
 * Copyright 2015 the original author or authors.
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

import geb.spock.GebSpec
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import squawker.api.Main
import squawker.jdbi.ApiTokenStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

// tag::first-web-test-1[]
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Main])
class TimelineSpec extends GebSpec { // <1>

  @LocalServerPort int port

  @Autowired DBI dbi
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired ApiTokenStore apiTokenStore

  def cleanup() {
    dbi.open().withCloseable { Handle handle ->
      handle.execute("delete from message")
      handle.execute("delete from following")
      handle.execute("delete from api_token")
      handle.execute("delete from user")
    }
  }

  Serializable createUser(String username) {
    def user = userStore.insert(username)
    apiTokenStore.generateTokenFor(user)
    return user.id
  }

  Serializable createMessage(String username, String text) {
    def user = userStore.find(username)
    messageStore.insert(user, text, now()).id
  }

  void loginAs(String username) {
    // ...
    // end::first-web-test-1[]
    go("http://localhost:$port/#/login")
    $("form.sq-login").username = username
    $("form.sq-login").password = "whatever"
    $("form.sq-login [type=submit]").click()
    // tag::first-web-test-2[]
  }

  def "a user can see their own messages in their timeline"() {
    given:
    createUser("spock") // <2>
    createMessage("spock", "Fascinating!")

    and:
    loginAs("spock") // <3>

    when:
    go("http://localhost:$port/#/timeline") // <4>

    then:
    waitFor {
      $(".page-header").text() == "Timeline" // <5>
    }
    $(".sq-message-text").text() == "Fascinating!" // <6>
    $(".sq-posted-by").text() == "@spock"
  }
}
// end::first-web-test-2[]

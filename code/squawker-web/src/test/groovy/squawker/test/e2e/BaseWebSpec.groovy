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

import geb.spock.GebSpec
import org.skife.jdbi.v2.DBI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import squawker.api.Main
import squawker.jdbi.ApiTokenStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import squawker.test.e2e.pages1.LoginPage
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Main])
abstract class BaseWebSpec extends GebSpec implements TestDataFixtures {

  @Autowired DBI dbi
  @Autowired UserStore userStore
  @Autowired MessageStore messageStore
  @Autowired ApiTokenStore apiTokenStore

  // tag::base-url[]
  @LocalServerPort int port

  def setup() {
    baseUrl = "http://localhost:$port"
  }
  // end::base-url[]

  def cleanup() {
    deleteAll()
  }

  // tag::login-method[]
  void loginAs(String username, String password = "password") {
    to(LoginPage)
      .login(username, password)
  }
  // end::login-method[]
}

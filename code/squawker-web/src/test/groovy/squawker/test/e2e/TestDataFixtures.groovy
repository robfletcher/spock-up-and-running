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

import java.time.Instant
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.Handle
import squawker.jdbi.ApiTokenStore
import squawker.jdbi.MessageStore
import squawker.jdbi.UserStore
import static java.time.Instant.now

trait TestDataFixtures {

  abstract DBI getDbi()

  abstract UserStore getUserStore()

  abstract MessageStore getMessageStore()

  abstract ApiTokenStore getApiTokenStore()

  void deleteAll() {
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

  Serializable createMessage(String username, String text, Instant timestamp = now()) {
    def user = userStore.find(username)
    messageStore.insert(user, text, timestamp).id
  }

}

/*
 * Copyright 2013 the original author or authors.
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
package squawker.jdbi;

import java.time.*;
import java.util.*;
import org.skife.jdbi.v2.exceptions.*;
import squawker.*;

class PersistentUser extends User {

  private final DataStore dataStore;

  PersistentUser(DataStore dataStore, String username) {
    super(username);
    this.dataStore = dataStore;
  }

  PersistentUser(DataStore dataStore, String username, Instant registered) {
    super(username, registered);
    this.dataStore = dataStore;
  }

  @Override
  public void follow(User user) {
    super.follow(user);
    dataStore.follow(this, user);
  }

  @Override
  public List<Message> timeline() {
    return dataStore.timeline(this);
  }

  @Override
  public Set<User> following() {
    if (following.isEmpty()) {
      following.addAll(dataStore.findFollowing(this));
    }
    return super.following();
  }

  @Override
  public Message post(String messageText) {
    Message message = new Message(this, messageText);
    dataStore.insert(message);
    return message;
  }

  @Override
  public List<Message> posts() {
    if (posts.isEmpty()) {
      try {
        posts.addAll(dataStore.postsBy(this));
      } catch (UnableToCreateStatementException e) {
        throw new IllegalStateException("database connection is stale", e);
      }
    }
    return super.posts();
  }
}

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

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import squawker.Message;
import squawker.User;

class PersistentUser extends User {

  private final FollowingStore followingStore;
  private final MessageStore messageStore;

  PersistentUser(long id, Handle handle, String username, Instant registered) {
    super(id, username, registered);
    followingStore = handle.attach(FollowingStore.class);
    messageStore = handle.attach(MessageStore.class);
  }

  @Override
  public List<Message> timeline() {
    return messageStore.timeline(this);
  }

  @Override
  public Set<User> following() {
    if (following.isEmpty()) {
      following.addAll(followingStore.findFollowing(this));
    }
    return super.following();
  }

  @Override
  public Message post(String messageText, Instant postedAt) {
    if (messageText.length() > Message.MAX_TEXT_LENGTH) {
      throw new IllegalArgumentException("Message text is too long");
    }
    return messageStore.insert(this, messageText, postedAt);
  }

  @Override
  public void follow(User user) {
    super.follow(user);
    followingStore.follow(this, user);
  }

  @Override
  public List<Message> posts() {
    if (posts.isEmpty()) {
      try {
        posts.addAll(messageStore.postsBy(this));
      } catch (UnableToCreateStatementException e) {
        throw new IllegalStateException("database connection is stale", e);
      }
    }
    return super.posts();
  }
}

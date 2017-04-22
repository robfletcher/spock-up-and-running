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
package squawker;

import java.time.*;
import java.util.*;

public class User {

  private final String username;
  protected final Set<User> following = new HashSet<>();
  protected final List<Message> posts = new ArrayList<>();
  private final Instant registered;

  public User(String username) {
    this(username, Instant.now());
  }

  protected User(String username, Instant registered) {
    if (username == null || username.length() == 0) {
      throw new IllegalArgumentException("username must be at least 1 character long");
    }
    this.username = username;
    this.registered = registered;
  }

  public String getUsername() {
    return username;
  }

  public Instant getRegistered() {
    return registered;
  }

  public Set<User> following() {
    return Collections.unmodifiableSet(following);
  }

  public void follow(User user) {
    if (this.equals(user)) {
      throw new IllegalArgumentException("a user cannot follow themself");
    }
    following.add(user);
  }

  public boolean follows(User user) {
    return following.contains(user);
  }

  public Message post(String messageText) {
    Message message = new Message(this, messageText);
    posts.add(0, message);
    return message;
  }

  public List<Message> posts() {
    return Collections.unmodifiableList(posts);
  }

  public List<Message> timeline() {
    List<Message> timeline = new ArrayList<Message>();
    timeline.addAll(posts);
    for (User user : following) {
      timeline.addAll(user.posts);
    }
    Collections.sort(timeline);
    return Collections.unmodifiableList(timeline);
  }

  @Override
  public String toString() {
    return "@" + username;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o instanceof User) {
      User other = (User) o;
      return username.equals(other.username);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

}

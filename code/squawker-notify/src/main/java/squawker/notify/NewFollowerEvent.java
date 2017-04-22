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

package squawker.notify;

import java.util.Objects;
import squawker.User;
import static java.lang.String.format;

public class NewFollowerEvent {

  private final User user;
  private final User newFollower;

  public NewFollowerEvent(User user, User newFollower) {
    this.user = user;
    this.newFollower = newFollower;
  }

  public User getUser() {
    return user;
  }

  public User getNewFollower() {
    return newFollower;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NewFollowerEvent that = (NewFollowerEvent) o;
    return Objects.equals(user, that.user) &&
      Objects.equals(newFollower, that.newFollower);
  }

  @Override public int hashCode() {
    return Objects.hash(user, newFollower);
  }

  @Override public String toString() {
    return format("NewFollowerEvent{%s is now followed by %s}", user, newFollower);
  }
}

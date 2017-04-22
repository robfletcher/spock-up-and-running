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

public class Message implements Comparable<Message> {

  public static final int MAX_TEXT_LENGTH = 140;

  private final User postedBy;
  private final String text;
  private final Instant postedAt;

  public Message(User postedBy, String text, Instant postedAt) {
    if (text.length() > MAX_TEXT_LENGTH) {
      throw new IllegalArgumentException("Message text cannot be longer than 140 characters");
    }
    this.postedBy = postedBy;
    this.text = text;
    this.postedAt = postedAt;
  }

  public User getPostedBy() {
    return postedBy;
  }

  public String getText() {
    return text;
  }

  public Instant getPostedAt() {
    return postedAt;
  }

  @Override
  public int compareTo(Message other) {
    return -postedAt.compareTo(other.postedAt);
  }

}

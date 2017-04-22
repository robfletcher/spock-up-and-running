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

import java.util.*;
import org.junit.*;
import static java.time.Instant.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MessagePostingTest {

  private User user = new User("spock");

  @Test
  public void aUserCanPostAMessage() {
    String messageText = "@kirk that is illogical, Captain!";

    user.post(messageText, now());

    // tag::assert-list-java[]
    List<String> messageTexts = new ArrayList<>();
    for (Message message : user.getPosts()) {
      messageTexts.add(message.getText());
    }
    List<String> expected = Collections.singletonList(messageText);
    assertThat(messageTexts, equalTo(expected));
    // end::assert-list-java[]
  }
}
